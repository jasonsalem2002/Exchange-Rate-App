import pandas as pd
import numpy as np
import torch
import torch.nn as nn
import torch.optim as optim
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from torch.utils.data import DataLoader, TensorDataset
from joblib import dump

# Load and preprocess data
data = pd.read_csv('Backend/files/data.csv')
data['added_date'] = pd.to_datetime(data['added_date'])
data['year'] = data['added_date'].dt.year
data['month'] = data['added_date'].dt.month
data['day'] = data['added_date'].dt.day
data['hour'] = data['added_date'].dt.hour
data['day_of_week'] = data['added_date'].dt.dayofweek
data['lbp_amount_lagged'] = data['lbp_amount'].shift(1).fillna(method='bfill')

# Select features and target
features = data[['year', 'month', 'day', 'hour', 'day_of_week', 'lbp_amount_lagged']]
labels = data['lbp_amount']

# Normalize the dataset
scaler = MinMaxScaler()
features_scaled = scaler.fit_transform(features)
dump(scaler, 'scaler.joblib')  # Saving the scaler

# Convert to tensors
features_scaled = torch.tensor(features_scaled, dtype=torch.float32)
labels = torch.tensor(labels.values, dtype=torch.float32).view(-1, 1)
X_train, X_test, y_train, y_test = train_test_split(features_scaled, labels, test_size=0.2, random_state=42)
train_dataset = TensorDataset(X_train, y_train)
test_dataset = TensorDataset(X_test, y_test)
train_loader = DataLoader(train_dataset, batch_size=4096, shuffle=True)
test_loader = DataLoader(test_dataset, batch_size=4096, shuffle=False)

# Define the LSTM network model
class LSTMPredictor(nn.Module):
    def __init__(self):
        super(LSTMPredictor, self).__init__()
        self.lstm = nn.LSTM(input_size=6, hidden_size=50, num_layers=10, batch_first=True, dropout=0.2, bidirectional=True)
        self.output_layer = nn.Linear(100, 1)  # Output layer size doubled due to bidirectionality

    def forward(self, x):
        x, _ = self.lstm(x)
        x = x[:, -1, :]  # Selecting the last timestep from both directions
        return self.output_layer(x)

device = torch.device("mps")
model = LSTMPredictor().to(device)
criterion = nn.MSELoss()
optimizer = optim.Adam(model.parameters(), lr=0.0001, weight_decay=1e-5)  # Using L2 regularization

# Training loop
num_epochs = 1500  # Increased epochs for better training
best_loss = float('inf')
for epoch in range(num_epochs):
    model.train()
    for inputs, targets in train_loader:
        inputs = inputs.view(inputs.shape[0], 1, inputs.shape[1]).to(device)
        targets = targets.to(device)
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, targets)
        loss.backward()
        optimizer.step()
    print(f'Epoch {epoch+1}, Loss: {loss.item()}')
    if loss.item() < best_loss:
        best_loss = loss.item()
        torch.save(model.state_dict(), 'LSTM_Exchange_Rate_Predictor.pth')
        print("Model saved with improved loss:", best_loss)

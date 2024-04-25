import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
import torch
import torch.nn as nn
from torch.utils.data import DataLoader, TensorDataset

# Check if GPU is available
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
print(f"Training will utilize {device}.")

# Load the dataset
df = pd.read_csv("Backend/NeuralNetwork/VersionF/output.csv")
df['added_date'] = pd.to_datetime(df['added_date'])  # Convert added_date to datetime

# Prepare the data
scaler = MinMaxScaler()
scaled_data = scaler.fit_transform(df[['lbp_amount']])

# Train-test split
train_size = int(len(scaled_data) * 0.8)
train_data = scaled_data[:train_size]
test_data = scaled_data[train_size:]

# Define sequence generator
def create_sequences(data, seq_length):
    sequences = []
    for i in range(len(data) - seq_length):
        seq = data[i:i+seq_length]
        label = data[i+seq_length:i+seq_length+1]
        sequences.append((seq, label))
    return sequences

n_input = 30  # Choose a suitable window size
train_sequences = create_sequences(train_data, n_input)

# Convert sequences to PyTorch tensors
X_train = torch.tensor([seq[0] for seq in train_sequences], dtype=torch.float32)
y_train = torch.tensor([seq[1] for seq in train_sequences], dtype=torch.float32)

# Define LSTM model
class LSTMModel(nn.Module):
    def __init__(self, input_size, hidden_size, output_size):
        super(LSTMModel, self).__init__()
        self.hidden_size = hidden_size
        self.lstm = nn.LSTM(input_size, hidden_size, batch_first=True)
        self.fc = nn.Linear(hidden_size, output_size)

    def forward(self, x):
        out, _ = self.lstm(x)
        out = self.fc(out[:, -1, :])
        return out

model = LSTMModel(n_features, 100, 1).to(device)

# Define loss function and optimizer
criterion = nn.MSELoss()
optimizer = torch.optim.Adam(model.parameters(), lr=0.001)

# Train the model
batch_size = 32
train_loader = DataLoader(TensorDataset(X_train, y_train), batch_size=batch_size, shuffle=True)

num_epochs = 50
for epoch in range(num_epochs):
    for inputs, labels in train_loader:
        inputs, labels = inputs.to(device), labels.to(device)
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, labels)
        loss.backward()
        optimizer.step()
    print(f'Epoch [{epoch+1}/{num_epochs}], Loss: {loss.item():.4f}')

# Save the trained model
torch.save(model.state_dict(), 'exchangeRate.pt')

# Load the trained model
model = LSTMModel(n_features, 100, 1).to(device)
model.load_state_dict(torch.load('exchangeRate.pt'))
model.eval()

# Example prediction from user input
user_input = "2024-04-05 14:00:00"
user_input = pd.to_datetime(user_input)
index = df.index[df['added_date'] == user_input][0]
data_for_prediction = df['lbp_amount'][index-n_input:index].values.reshape((1, n_input, n_features))
scaled_input = scaler.transform(data_for_prediction)
X_test = torch.tensor(scaled_input, dtype=torch.float32).to(device)
predicted_value_scaled = model(X_test)
predicted_value = scaler.inverse_transform(predicted_value_scaled.cpu().detach().numpy())
print("Predicted exchange rate at", user_input, ":", predicted_value[0][0])

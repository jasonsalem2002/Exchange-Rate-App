import torch
import torch.nn as nn
import pandas as pd
import numpy as np
from joblib import load

class LSTMPredictor(nn.Module):
    def __init__(self):
        super(LSTMPredictor, self).__init__()
        self.lstm = nn.LSTM(input_size=6, hidden_size=50, num_layers=3, batch_first=True, dropout=0.2, bidirectional=True)
        self.output_layer = nn.Linear(100, 1)

    def forward(self, x):
        x, _ = self.lstm(x)
        x = x[:, -1, :]
        return self.output_layer(x)

def predict_lbp_amount(date, model_path, scaler_path):
    scaler = load(scaler_path)
    date_parsed = pd.to_datetime(date)
    features = np.array([[date_parsed.year, date_parsed.month, date_parsed.day, date_parsed.hour, date_parsed.dayofweek, 4230]])  # Example placeholder, replace 4230 with actual lag
    print("Features before scaling:", features)  #  chfu features
    features_scaled = scaler.transform(features)
    print("Features after scaling:", features_scaled)  # chuf features
    features_tensor = torch.tensor(features_scaled, dtype=torch.float32).view(1, 1, 6)
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = LSTMPredictor().to(device)
    model.load_state_dict(torch.load(model_path))
    model.eval()
    with torch.no_grad():
        features_tensor = features_tensor.to(device)
        prediction = model(features_tensor)
        return prediction.item()

model_path = 'LSTM_Exchange_Rate_Predictor.pth'
scaler_path = 'scaler.joblib'
date = '2021-05-14 15:11:00'
predicted_lbp = predict_lbp_amount(date, model_path, scaler_path)
print(f'Predicted LBP amount for 1 USD on {date}: {predicted_lbp}')

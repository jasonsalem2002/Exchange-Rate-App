import numpy as np
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import LSTM, Dense
from sklearn.preprocessing import MinMaxScaler
import pandas as pd

full_data = pd.read_csv('Backend/NeuralNetwork/data.csv')
# Assuming 'full_data' is your original DataFrame with 'lbp_amount' and 'added_date'
scaler = MinMaxScaler()
data_scaled = scaler.fit_transform(full_data['lbp_amount'].values.reshape(-1, 1))
np.savetxt('full_data_scaled.csv', data_scaled, delimiter=',')
np.savetxt('dates.csv', full_data['added_date'].dt.strftime('%Y-%m-%d').values, delimiter=',', fmt='%s')

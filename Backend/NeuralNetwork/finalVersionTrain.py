import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from keras.models import Sequential
from keras.layers import LSTM, Dense
import pickle

# Load the data
df = pd.read_csv('output.csv', index_col='added_date', parse_dates=True)

# Assuming 'lbp amount' is the target column
target_column = 'lbp amount'
df = df[[target_column]]  # Keep only the target column

# Data scaling
scaler = MinMaxScaler()
scaled_data = scaler.fit_transform(df)

# Prepare time series generator
n_input = 12
n_features = 1
generator = TimeseriesGenerator(scaled_data, scaled_data, length=n_input, batch_size=1)

# Model definition
model = Sequential([
    LSTM(100, activation='relu', input_shape=(n_input, n_features)),
    Dense(1)
])
model.compile(optimizer='adam', loss='mse')

# Fit model
model.fit(generator, epochs=50)

# Save model and scaler
model.save('lstm_model.h5')
with open('scaler.pkl', 'wb') as f:
    pickle.dump(scaler, f)

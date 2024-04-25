import numpy as np
import pandas as pd
from sklearn.preprocessing import MinMaxScaler

data = pd.read_csv("Backend/NeuralNetwork/VersionF/output.csv")

# Convert 'lbp_amount' to a numpy array
exchange_rates = data['lbp_amount'].values.reshape(-1, 1)

# Scale the data
scaler = MinMaxScaler(feature_range=(0, 1))
exchange_rates_scaled = scaler.fit_transform(exchange_rates)

# Define the number of timesteps
# For LSTM, we need to define the number of previous time steps to use for prediction
# Here, let's use 30 days of previous data to predict the exchange rate for the next day
timesteps = 30

# Create sequences of data
X, y = [], []
for i in range(timesteps, len(exchange_rates_scaled)):
    X.append(exchange_rates_scaled[i - timesteps:i, 0])
    y.append(exchange_rates_scaled[i, 0])

# Convert lists to numpy arrays
X, y = np.array(X), np.array(y)

# Reshape X to be 3-dimensional (samples, timesteps, features)
X = np.reshape(X, (X.shape[0], X.shape[1], 1))

# Split the dataset into training and testing sets (80% train, 20% test)
split_index = int(len(X) * 0.8)
X_train, X_test = X[:split_index], X[split_index:]
y_train, y_test = y[:split_index], y[split_index:]

from keras.models import Sequential
from keras.layers import LSTM, Dense

# Define the LSTM model architecture
model = Sequential()

# Add the LSTM layer with 50 units and input shape (timesteps, features)
model.add(LSTM(units=50, input_shape=(X_train.shape[1], 1)))

# Add the output layer with one neuron
model.add(Dense(units=1))

# Compile the model
model.compile(optimizer='adam', loss='mean_squared_error')
# Train the model
model.fit(X_train, y_train, epochs=100, batch_size=128)

# Save the trained model to disk
model.save("exchange_rate_lstm_model.h5")

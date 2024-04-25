from sklearn.metrics import mean_absolute_error, mean_squared_error

from keras.models import load_model
import matplotlib.pyplot as plt
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

# Load the saved model
loaded_model = load_model("Backend/NeuralNetwork/VersionF/exchange_rate_lstm_model.h5")
# Calculate predictions for the testing set
predicted_scaled = loaded_model.predict(X_test)
predicted = scaler.inverse_transform(predicted_scaled)

# Calculate MAE and MSE
mae = mean_absolute_error(y_test, predicted)
mse = mean_squared_error(y_test, predicted)

print("Mean Absolute Error (MAE):", mae)
print("Mean Squared Error (MSE):", mse)
from keras.models import Sequential
from keras.layers import LSTM, Dense

# Define the LSTM model architecture with multiple layers
model = Sequential()

# Add the first LSTM layer with 100 units and input shape (timesteps, features)
model.add(LSTM(units=100, return_sequences=True, input_shape=(X_train.shape[1], 1)))

# Add a second LSTM layer with 50 units
model.add(LSTM(units=50))

# Add the output layer with one neuron
model.add(Dense(units=1))

# Compile the model
model.compile(optimizer='adam', loss='mean_squared_error')

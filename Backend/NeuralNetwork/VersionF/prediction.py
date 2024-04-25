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

# Use the loaded model to make predictions
predicted_scaled = loaded_model.predict(X_test)

# Inverse scaling to get the original scale of exchange rates
predicted = scaler.inverse_transform(predicted_scaled)

# Plot the actual vs. predicted exchange rates
plt.figure(figsize=(12, 6))
plt.plot(data.index[split_index + timesteps:], y_test, label='Actual Exchange Rate (LBP/USD)', color='blue')
plt.plot(data.index[split_index + timesteps:], predicted, label='Predicted Exchange Rate (LBP/USD)', color='red')
plt.title('Actual vs. Predicted Exchange Rate')
plt.xlabel('Date')
plt.ylabel('Exchange Rate (LBP/USD)')
plt.legend()
plt.grid(True)
plt.show()

# Convert the predicted_scaled array back to the original scale
predicted = scaler.inverse_transform(predicted_scaled)

# Create a DataFrame to display the actual and predicted exchange rates along with dates
predicted_df = pd.DataFrame({'Date': data.index[split_index + timesteps:],
                             'Actual Exchange Rate (LBP/USD)': y_test,
                             'Predicted Exchange Rate (LBP/USD)': predicted.flatten()})

# Display the DataFrame
print(predicted_df)

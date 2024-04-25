import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.preprocessing.sequence import TimeseriesGenerator
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, LSTM
import tensorflow as tf
from sklearn.metrics import mean_absolute_error, mean_squared_error

# Load the dataset
df = pd.read_csv("Backend/NeuralNetwork/VersionF/output.csv")
df['added_date'] = pd.to_datetime(df['added_date'])  # Convert added_date to datetime

# Plot the dataset
# plt.figure(figsize=(12, 6))
# plt.plot(df['added_date'], df['lbp_amount'])
# plt.title('Exchange Rate')
# plt.xlabel('Date')
# plt.ylabel('LBP Amount')
# plt.show()

# Prepare the data
scaler = MinMaxScaler()
scaled_data = scaler.fit_transform(df[['lbp_amount']])

# Train-test split
train_size = int(len(scaled_data) * 0.8)
train = scaled_data[:train_size]
test = scaled_data[train_size:]

# Define generator
n_input = 30  # Choose a suitable window size
n_features = 1
generator = TimeseriesGenerator(train, train, length=n_input, batch_size=64)

# Define model
model = Sequential()
model.add(LSTM(100, activation='relu', input_shape=(n_input, n_features)))
model.add(Dense(1))
model.compile(optimizer='adam', loss='mse')

# Fit model
history = model.fit(generator, epochs=2, verbose=1)

# Plot loss curve
# plt.plot(history.history['loss'])
# plt.title('Model Loss')
# plt.xlabel('Epoch')
# plt.ylabel('Loss')
# plt.show()

# Predictions
test_generator = TimeseriesGenerator(test, test, length=n_input, batch_size=16)
test_predictions = model.predict(test_generator)

# Inverse transform predictions
test_predictions_inverse = scaler.inverse_transform(test_predictions)

# Evaluate model
mae = mean_absolute_error(df['lbp_amount'].iloc[train_size + n_input:], test_predictions_inverse)
mse = mean_squared_error(df['lbp_amount'].iloc[train_size + n_input:], test_predictions_inverse)
rmse = np.sqrt(mse)

print("Mean Absolute Error:", mae)
print("Mean Squared Error:", mse)
print("Root Mean Squared Error:", rmse)

# Plot predictions
plt.figure(figsize=(14, 5))
plt.plot(df.index[train_size + n_input:], df['lbp_amount'][train_size + n_input:], label='True Values')
plt.plot(df.index[train_size + n_input:], test_predictions_inverse, label='Predictions')
plt.title('Exchange Rate Prediction')
plt.xlabel('Date')
plt.ylabel('LBP Amount')
plt.legend()
plt.show()


# Define a function to predict exchange rate for a given date
def predict_exchange_rate(model, scaler, df, target_date, n_input):
    # Find the index of the target date in the dataframe
    print(df['added_date'].min())  # Minimum date
    print(df['added_date'].max())  # Maximum date

    target_index = df.index[df['added_date'] == target_date].tolist()[0]
    
    # Create the input data for prediction
    input_data = df['lbp_amount'].values[target_index - n_input: target_index]
    input_data = input_data.reshape((1, n_input, 1))  # Reshape for LSTM input
    
    # Scale the input data
    input_data_scaled = scaler.transform(input_data)
    
    # Make the prediction
    predicted_value = model.predict(input_data_scaled)
    
    # Inverse transform the prediction
    predicted_value_inverse = scaler.inverse_transform(predicted_value)
    
    return predicted_value_inverse[0][0]

# Define the target date for prediction
target_date = pd.to_datetime('2021-10-06')  # Change this to your desired date

# Predict exchange rate for the target date
predicted_exchange_rate = predict_exchange_rate(model, scaler, df, target_date, n_input)

print("Predicted Exchange Rate for", target_date, ":", predicted_exchange_rate)

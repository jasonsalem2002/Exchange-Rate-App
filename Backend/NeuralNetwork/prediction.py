import numpy as np
from tensorflow.keras.models import load_model
from datetime import datetime, timedelta

# Define the dates for the entire dataset
all_dates = [
    '2024-03-12', '2024-03-13', '2024-03-14', '2024-03-15', '2024-03-16',
    '2024-03-17', '2024-03-18', '2024-03-19', '2024-03-20',
    '2024-03-21', '2024-03-22', '2024-03-23', '2024-03-24', '2024-03-25',
    '2024-03-26', '2024-03-27', '2024-03-28', '2024-03-29', '2024-03-30',
    '2024-03-31', '2024-04-01', '2024-04-02', '2024-04-03', '2024-04-04',
    '2024-04-05', '2024-04-06', '2024-04-07', '2024-04-08', '2024-04-09',
    '2024-04-10', '2024-04-11'
]

# Assuming you have scaled values saved, load them
scaled_values = np.loadtxt('Backend/NeuralNetwork/X_train.csv', delimiter=',')

# Prepare the date and scaled values arrays
dates_array = np.array([datetime.strptime(date, '%Y-%m-%d') for date in all_dates])
scaled_values_array = np.array(scaled_values)

# Define the window size and the date you want to predict
window_size = 30
predict_date = datetime(2024, 3, 12)  # Adjust to the optimal prediction date

# Find the index for the predict_date - 30 days (to have enough data to form a window)
start_index = np.where(dates_array == (predict_date - timedelta(days=window_size)))[0][0]
input_data = scaled_values_array[start_index:start_index + window_size].reshape(1, window_size, 1)

# Load the model
model = load_model('lstm_model.h5')

# Make predictions
prediction = model.predict(input_data)

# Print the prediction
print("Predicted LBP amount for", predict_date.strftime('%Y-%m-%d'), "is:", prediction[0][0])

import pandas as pd
from datetime import timedelta

# Load the data
data = pd.read_csv('Backend/NeuralNetwork/data.csv')
data['added_date'] = pd.to_datetime(data['added_date'])
all_dates = data['added_date'].dt.strftime('%Y-%m-%d').tolist()

# Find the last date
last_date = data['added_date'].max()
optimal_predict_date = last_date - timedelta(days=30)

# Now, optimal_predict_date is the date you should use for prediction.
print("Last date in dataset:", last_date)
print("Optimal date for prediction:", optimal_predict_date)

import pandas as pd
import matplotlib.pyplot as plt

# Load the dataset
data = pd.read_csv("Backend/NeuralNetwork/VersionF/output.csv")

# Convert 'added_date' to datetime format
data['added_date'] = pd.to_datetime(data['added_date'])

# Set 'added_date' as index
data.set_index('added_date', inplace=True)

# Plot the time series data
plt.figure(figsize=(12, 6))
plt.plot(data.index, data['lbp_amount'], label='Exchange Rate (LBP/USD)')
plt.title('Exchange Rate Over Time')
plt.xlabel('Date')
plt.ylabel('Exchange Rate (LBP/USD)')
plt.legend()
plt.grid(True)
plt.show()

# Check for trends, seasonality, and stationarity
# You can use techniques like decomposition and ADF test for this analysis

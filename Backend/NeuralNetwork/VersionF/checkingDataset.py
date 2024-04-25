import pandas as pd

# Load the dataset
data = pd.read_csv("Backend/NeuralNetwork/VersionF/output.csv")

# Display the first few rows of the dataset
print("First few rows of the dataset:")
print(data.head())

# Check for missing values
print("\nMissing values:")
print(data.isnull().sum())

# Check the data types of each column
print("\nData types:")
print(data.dtypes)

# Summary statistics
print("\nSummary statistics:")
print(data.describe())

# Visualize the data (optional)
# You can use libraries like matplotlib or seaborn to create plots for visualization

# Check for any trends or seasonality in the data
# You can use time series analysis techniques like decomposition to analyze trends and seasonality

# Check for autocorrelation
# Autocorrelation plots can help in understanding if there's any correlation between past and current exchange rates

# Check for stationarity
# Stationarity tests like Augmented Dickey-Fuller test can be used to check if the data is stationary or not

import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.dates as mdates

# Load the data
df = pd.read_csv("predicted_exchange_rates.csv")
df["added_date"] = pd.to_datetime(df["added_date"])

# Set the plot size
plt.figure(figsize=(14, 7))

# Plotting the data
plt.plot(
    df["added_date"], df["lbp_amount"], label="LBP Amount", marker="o", color="blue"
)

# Formatting the date on the x-axis
plt.gca().xaxis.set_major_formatter(mdates.DateFormatter("%Y-%m-%d"))
plt.gca().xaxis.set_major_locator(
    mdates.DayLocator(interval=360)
)  # Adjust interval to your needs

# Formatting the y-axis to avoid scientific notation
plt.gca().get_yaxis().get_major_formatter().set_scientific(False)

# Adding labels and title
plt.xlabel("Date")
plt.ylabel("LBP Amount")
plt.title("LBP Amounts Historic and Future Predictions")

# Display the grid
plt.grid(True)

# Show the plot with legend
plt.legend()
plt.show()

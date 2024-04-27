import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from tensorflow.keras.preprocessing.sequence import TimeseriesGenerator
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, LSTM
from sklearn.metrics import mean_absolute_error, mean_squared_error

import matplotlib.pyplot as plt

df = pd.read_csv("Backend/NeuralNetwork/VersionF/modified_juicyFile.csv")
df["added_date"] = pd.to_datetime(df["added_date"])

scaler = MinMaxScaler()
scaled_data = scaler.fit_transform(df[["lbp_amount"]])

train_size = int(len(scaled_data) * 0.8)
train = scaled_data[:train_size]
test = scaled_data[train_size:]

n_input = 30
n_features = 1
generator = TimeseriesGenerator(train, train, length=n_input, batch_size=1)

model = Sequential()
model.add(LSTM(100, activation="relu", input_shape=(n_input, n_features)))
model.add(Dense(1))
model.compile(optimizer="adam", loss="mse")

history = model.fit(generator, epochs=10, verbose=1)
model.save("finalMODEL.keras")
model.save("FinalMODELLLL.h5")
test_generator = TimeseriesGenerator(test, test, length=n_input, batch_size=16)
test_predictions = model.predict(test_generator)
test_predictions_inverse = scaler.inverse_transform(test_predictions)

mae = mean_absolute_error(
    df["lbp_amount"].iloc[train_size + n_input :], test_predictions_inverse
)
mse = mean_squared_error(
    df["lbp_amount"].iloc[train_size + n_input :], test_predictions_inverse
)
rmse = np.sqrt(mse)

all_predictions = []
for i in range(len(df) - n_input):
    x, y = TimeseriesGenerator(scaled_data, scaled_data, length=n_input, batch_size=1)[
        i
    ]
    all_predictions.append(model.predict(x)[0][0])

all_predictions_inverse = scaler.inverse_transform(
    np.array(all_predictions).reshape(-1, 1)
)

prediction_dates = df["added_date"][n_input:]
predicted_df = pd.DataFrame(
    {"added_date": prediction_dates, "lbp_amount": all_predictions_inverse.flatten()}
)
predicted_df.to_csv("predicted_exchange_rates.csv", index=False)

cutoff_date = pd.to_datetime("2024-05-26")
plt.figure(figsize=(14, 5))
plt.plot(
    df["added_date"][n_input:],
    df["lbp_amount"][n_input:],
    label="True Values",
    marker="o",
)
plt.plot(
    prediction_dates[prediction_dates <= cutoff_date],
    predicted_df["lbp_amount"][prediction_dates <= cutoff_date],
    label="Model Predictions (Historic)",
    marker="x",
    color="orange",
)
plt.plot(
    prediction_dates[prediction_dates > cutoff_date],
    predicted_df["lbp_amount"][prediction_dates > cutoff_date],
    label="Model Predictions (Future)",
    marker="x",
    color="red",
)
plt.title("Exchange Rate Prediction")
plt.xlabel("Date")
plt.ylabel("LBP Amount")
plt.legend()
plt.grid(True)
plt.show()


def predict_exchange_rate(model, scaler, df, target_date, n_input):
    target_index = df.index[df["added_date"] == target_date].tolist()[0]
    input_data = df["lbp_amount"].values[target_index - n_input : target_index]
    input_data_scaled = scaler.transform(input_data.reshape((1, n_input, 1)))
    predicted_value = model.predict(input_data_scaled)
    return scaler.inverse_transform(predicted_value)[0][0]


target_date = pd.to_datetime("2021-10-06")
predicted_exchange_rate = predict_exchange_rate(model, scaler, df, target_date, n_input)
print("Predicted Exchange Rate for", target_date, ":", predicted_exchange_rate)

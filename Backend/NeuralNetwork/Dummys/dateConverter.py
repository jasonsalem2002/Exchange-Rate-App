import pandas as pd

def convert_date_format(date_str):
    # Convert the incoming date format to the desired format
    return pd.to_datetime(date_str).strftime('%Y-%m-%d %H:%M:%S')

def main():
    # Load the dataset
    df = pd.read_csv('normalized_output.csv')

    # Convert 'added_date' from format YYYY/MM/DD HH:MM:SS to YYYY-MM-DD HH:MM:SS
    df['added_date'] = df['added_date'].apply(convert_date_format)

    # Save the updated DataFrame to a new CSV file
    df.to_csv('updated_dates.csv', index=False)
    print("Dates have been converted and saved to 'updated_dates.csv'.")

if __name__ == "__main__":
    main()

import pandas as pd
from datetime import datetime

def parse_date(date_str):
    # Try parsing the date with and without seconds
    for fmt in ("%m/%d/%Y %H:%M:%S", "%m/%d/%Y %I:%M:%S %p", "%m/%d/%Y %H:%M", "%m/%d/%Y %I:%M %p"):
        try:
            return datetime.strptime(date_str, fmt)
        except ValueError:
            continue
    raise ValueError(f"Date format not supported: {date_str}")

def main():
    # Load the dataset
    df = pd.read_csv('output.csv')

    # Normalize 'added_date' to the desired format
    df['added_date'] = df['added_date'].apply(parse_date)
    df['added_date'] = df['added_date'].dt.strftime('%Y/%m/%d %H:%M:%S')

    # Save the normalized data to a new CSV file
    df.to_csv('normalized_output.csv', index=False)
    print("Data has been normalized and saved to 'normalized_output.csv'.")

if __name__ == "__main__":
    main()

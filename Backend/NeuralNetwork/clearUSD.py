import pandas as pd

def remove_column(csv_file_path, column_name, output_file_path):
    # Load the CSV file into a DataFrame
    df = pd.read_csv(csv_file_path)
    
    # Check if the column exists
    if column_name not in df.columns:
        print(f"Column '{column_name}' does not exist in the CSV file.")
        return
    
    # Remove the column
    df.drop(columns=[column_name], inplace=True)
    
    # Save the modified DataFrame to a new CSV file
    df.to_csv(output_file_path, index=False)
    print(f"Column '{column_name}' removed successfully. Output saved to '{output_file_path}'.")

# Example usage:
csv_file_path = "Backend/NeuralNetwork/data.csv"
output_file_path = "output.csv"
column_name = "usd_amount"

remove_column(csv_file_path, column_name, output_file_path)

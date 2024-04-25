import requests
import random
import json

# URL of your Flask endpoint
URL = "http://localhost:4820/api/transaction"

# Function to generate a random transaction data
def generate_transaction():
    usd_amount = random.uniform(1, 100)  # Random USD amount between 1 and 100
    lbp_amount = random.randint(85000, 90000)  # Random LBP amount between 85000 and 90000
    usd_to_lbp = random.choice([True, False])  # Random choice for usd_to_lbp
    
    return {
        "usd_amount": usd_amount,
        "lbp_amount": lbp_amount,
        "usd_to_lbp": usd_to_lbp
    }

# Create 50 transactions
for _ in range(50):
    transaction_data = generate_transaction()
    
    # Send POST request to Flask endpoint
    response = requests.post(URL, json=transaction_data)
    
    if response.status_code == 201:
        print("Transaction created successfully:", response.json())
    else:
        print("Failed to create transaction:", response.json())

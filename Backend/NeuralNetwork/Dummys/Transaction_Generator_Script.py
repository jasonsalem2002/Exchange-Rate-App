
import requests
import random
from datetime import datetime, timedelta

AUTH_URL = "http://127.0.0.1:4820/authentication"
TRANSACT_URL = "http://127.0.0.1:4820/transaction"
USERNAME = "admin"
PASSWORD = "JasonSalem2002!"
RATE_LOW = 87000
RATE_HIGH = 90000
TRANSACTION_COUNT = 10000
START_DATE = datetime(2018, 1, 1)
END_DATE = datetime(2024, 12, 31)
DATE_SPAN_DAYS = (END_DATE - START_DATE).days

def authenticate():
    data = {'user_name': USERNAME, 'password': PASSWORD}
    response = requests.post(AUTH_URL, json=data)
    if response.status_code == 200:
        return response.json()['token']
    else:
        raise Exception(f"Failed to authenticate: {response.json()}")

def create_transactions(token):
    total_usd = 0
    total_lbp = 0
    for _ in range(TRANSACTION_COUNT):
        usd_amount = random.uniform(10, 1000)
        rate = random.randint(RATE_LOW, RATE_HIGH)
        lbp_amount = usd_amount * rate
        days_offset = random.randint(0, DATE_SPAN_DAYS)
        transaction_date = START_DATE + timedelta(days=days_offset)
        transaction_date_str = transaction_date.strftime('%Y-%m-%d')

        total_usd += usd_amount
        total_lbp += lbp_amount

        transaction_data = {
            'usd_amount': usd_amount,
            'lbp_amount': lbp_amount,
            'usd_to_lbp': False,
            'date': transaction_date_str
        }
        headers = {'Authorization': f'Bearer {token}'}
        response = requests.post(TRANSACT_URL, json=transaction_data, headers=headers)
        if response.status_code != 201:
            print(f"Failed to create transaction: {response.json()}")

    print("Average Exchange Rate:", total_lbp / total_usd)

def main():
    token = authenticate()
    create_transactions(token)

if __name__ == "__main__":
    main()

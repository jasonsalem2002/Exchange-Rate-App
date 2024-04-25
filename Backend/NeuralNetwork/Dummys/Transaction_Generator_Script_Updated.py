
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

def random_datetime(start_date, end_date):
    """ Generate a random datetime between start_date and end_date """
    delta = end_date - start_date
    random_days = random.randrange(delta.days + 1)
    random_seconds = random.randrange(86400)  # Number of seconds in a day
    random_date = start_date + timedelta(days=random_days, seconds=random_seconds)
    return random_date

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
        usd_amount = random.uniform(10, 1000)  # Random USD amount between 10 and 1000
        rate = random.randint(RATE_LOW, RATE_HIGH)  # Random rate between RATE_LOW and RATE_HIGH
        lbp_amount = usd_amount * rate
        transaction_date = random_datetime(START_DATE, END_DATE)
        transaction_date_str = transaction_date.strftime('%Y-%m-%d %H:%M:%S')
        usd_to_lbp = random.choice([True, False])  # Randomly choose between buy and sell

        transaction_data = {
            'usd_amount': usd_amount,
            'lbp_amount': lbp_amount,
            'usd_to_lbp': usd_to_lbp,
            'date': transaction_date_str
        }
        headers = {'Authorization': f'Bearer {token}'}
        response = requests.post(TRANSACT_URL, json=transaction_data, headers=headers)
        if response.status_code != 201:
            print(f"Failed to create transaction: {response.json()}")

    print("Average Exchange Rate (for sells):", total_lbp / total_usd)


def main():
    token = authenticate()
    create_transactions(token)

if __name__ == "__main__":
    main()

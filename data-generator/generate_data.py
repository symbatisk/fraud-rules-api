import requests
import random
from faker import Faker
from datetime import datetime
import time

fake = Faker()

API_URL = "http://localhost:8080/api/transactions"

# ID пользователей, которых мы создали в БД вручную
USER_IDS = [1, 2, 3, 4, 5, 6]  # подставь реальные id из SELECT выше

NORMAL_COUNTRIES = ["US", "GB", "DE", "FR", "CA", "MX"]
BLOCKED_COUNTRIES = ["KP", "IR", "SY"]
MERCHANTS = ["Amazon", "Walmart", "Starbucks", "Uber", "Netflix", "Apple Store"]


def send_transaction(user_id, amount, country, merchant):
    payload = {
        "userId": user_id,
        "amount": amount,
        "currency": "USD",
        "country": country,
        "merchant": merchant
    }
    response = requests.post(API_URL, json=payload)
    status = response.status_code
    print(f"User {user_id} | ${amount} | {country} | {merchant} -> {status}")
    return response


def generate_normal_transactions(count=50):
    print(f"\n--- Generating {count} normal transactions ---")
    for _ in range(count):
        user_id = random.choice(USER_IDS)
        amount = round(random.uniform(5, 500), 2)
        country = random.choice(NORMAL_COUNTRIES)
        merchant = random.choice(MERCHANTS)
        send_transaction(user_id, amount, country, merchant)
        time.sleep(random.uniform(1.5, 4.0))

def generate_amount_threshold_anomalies(count=5):
    print(f"\n--- Generating {count} AMOUNT_THRESHOLD anomalies ---")
    for _ in range(count):
        user_id = random.choice(USER_IDS)
        amount = round(random.uniform(10001, 50000), 2)
        country = random.choice(NORMAL_COUNTRIES)
        merchant = random.choice(MERCHANTS)
        send_transaction(user_id, amount, country, merchant)
        time.sleep(0.05)


def generate_velocity_anomalies():
    print(f"\n--- Generating VELOCITY_CHECK anomaly ---")
    user_id = random.choice(USER_IDS)
    # 7 транзакций подряд без паузы -> должно триггернуть velocity check (порог 5 за 10 минут)
    for _ in range(7):
        amount = round(random.uniform(20, 200), 2)
        country = random.choice(NORMAL_COUNTRIES)
        merchant = random.choice(MERCHANTS)
        send_transaction(user_id, amount, country, merchant)
        time.sleep(0.05)


def generate_blocked_country_anomalies(count=3):
    print(f"\n--- Generating {count} BLOCKED_COUNTRY anomalies ---")
    for _ in range(count):
        user_id = random.choice(USER_IDS)
        amount = round(random.uniform(20, 500), 2)
        country = random.choice(BLOCKED_COUNTRIES)
        merchant = random.choice(MERCHANTS)
        send_transaction(user_id, amount, country, merchant)
        time.sleep(0.05)


def generate_duplicate_payment_anomalies(count=3):
    print(f"\n--- Generating {count} DUPLICATE_PAYMENT anomalies ---")
    for _ in range(count):
        user_id = random.choice(USER_IDS)
        amount = round(random.uniform(20, 300), 2)
        merchant = random.choice(MERCHANTS)
        country = random.choice(NORMAL_COUNTRIES)

        # Отправляем одну и ту же транзакцию дважды подряд -> должно триггернуть duplicate check
        send_transaction(user_id, amount, country, merchant)
        time.sleep(0.05)
        send_transaction(user_id, amount, country, merchant)
        time.sleep(0.05)


if __name__ == "__main__":
    print(f"Starting data generation at {datetime.now()}")

    generate_normal_transactions(count=50)
    generate_amount_threshold_anomalies(count=5)
    generate_velocity_anomalies()
    generate_blocked_country_anomalies(count=3)
    generate_duplicate_payment_anomalies(count=3)

    print(f"\nDone at {datetime.now()}")
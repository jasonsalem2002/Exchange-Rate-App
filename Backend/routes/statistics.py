from flask import Blueprint, jsonify, request
import datetime
from collections import defaultdict
from ..models.Transaction import Transaction

statistics_bp = Blueprint('statistics_bp', __name__)

@statistics_bp.route('/number_of_transactions', methods=['GET'])
def get_statistics():
    start_date = request.args.get('start_date')
    end_date = request.args.get('end_date')

    start_date = datetime.datetime.strptime(start_date, '%Y-%m-%d')
    end_date = datetime.datetime.strptime(end_date, '%Y-%m-%d')

    transactions = Transaction.query.filter(Transaction.added_date >= start_date, Transaction.added_date <= end_date).all()
    transactions_per_day = defaultdict(int)

    for transaction in transactions:
        transaction_date = transaction.added_date.date()
        transactions_per_day[transaction_date] += 1

    response = {
        "transactions_per_day": {str(date): count for date, count in transactions_per_day.items()},
        "total_transactions": sum(transactions_per_day.values())
    }

    return jsonify(response)
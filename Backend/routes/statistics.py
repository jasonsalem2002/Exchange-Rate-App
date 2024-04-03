from flask import Blueprint, jsonify, request
import datetime
from ..models.Transaction import Transaction

statistics_bp = Blueprint('statistics_bp', __name__)

@statistics_bp.route('/statistics', methods=['GET'])
def get_statistics():
    # Get start and end dates from query parameters
    start_date = request.args.get('start_date')
    end_date = request.args.get('end_date')

    # Convert start and end dates to datetime objects
    start_date = datetime.datetime.strptime(start_date, '%Y-%m-%d')
    end_date = datetime.datetime.strptime(end_date, '%Y-%m-%d')

    # Get transactions within the specified period
    transactions = Transaction.query.filter(Transaction.added_date >= start_date, Transaction.added_date <= end_date).all()

    # Initialize variables to calculate LBP to USD and USD to LBP totals
    lbp_to_usd_total = 0
    usd_to_lbp_total = 0

    # Calculate total LBP to USD and USD to LBP
    for transaction in transactions:
        if transaction.usd_to_lbp:
            usd_to_lbp_total += transaction.usd_amount
        else:
            lbp_to_usd_total += transaction.usd_amount

    # Construct the JSON response
    response = {
        "lbp_to_usd": round(lbp_to_usd_total, 2),
        "usd_to_lbp": round(usd_to_lbp_total, 2)
    }

    # Return the JSON response
    return jsonify(response)
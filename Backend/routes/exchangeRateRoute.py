from flask import Blueprint, jsonify
import datetime
from ..models.Transaction import Transaction

exchange_rate_bp = Blueprint('exchange_rate_bp', __name__)

@exchange_rate_bp.route('/exchangeRate', methods=['GET'])
def get_exchange_rate():
    try:
        end_date =datetime.datetime.now()
        start_date =end_date - datetime.timedelta(hours=72)
        usd_to_lbp_transactions = Transaction.query.filter(Transaction.added_date.between(start_date, end_date), Transaction.usd_to_lbp == True).all()
        lbp_to_usd_transactions = Transaction.query.filter(Transaction.added_date.between(start_date, end_date), Transaction.usd_to_lbp == False).all()

        total_lbp_to_usd = 0
        for transaction in lbp_to_usd_transactions:
            total_lbp_to_usd += transaction.lbp_amount / transaction.usd_amount

        total_usd_to_lbp = 0
        for transaction in usd_to_lbp_transactions:
            total_usd_to_lbp += transaction.lbp_amount / transaction.usd_amount

        if len(lbp_to_usd_transactions) > 0:
            average_lbp_to_usd = total_lbp_to_usd / len(lbp_to_usd_transactions)
        else:
            average_lbp_to_usd=None

        if len(usd_to_lbp_transactions) > 0:
            average_usd_to_lbp = total_usd_to_lbp / len(usd_to_lbp_transactions) 
        else:
            average_usd_to_lbp=None

        if average_usd_to_lbp is not None:
            average_usd_to_lbp = round(average_usd_to_lbp, 2)
        if average_lbp_to_usd is not None:
            average_lbp_to_usd = round(average_lbp_to_usd, 2)
        
        exchange_rates = {
            "usd_to_lbp": average_usd_to_lbp,
            "lbp_to_usd": average_lbp_to_usd
        }

        return jsonify(exchange_rates)

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
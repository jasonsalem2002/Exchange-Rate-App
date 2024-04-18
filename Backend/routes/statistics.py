from flask import Blueprint, jsonify, request
import datetime
from collections import defaultdict
from ..models.Transaction import Transaction

statistics_bp = Blueprint("statistics_bp", __name__)


@statistics_bp.route("/number_of_transactions", methods=["GET"])
def get_statistics():
    start_date = request.args.get("start_date")
    end_date = request.args.get("end_date")

    start_date = datetime.datetime.strptime(start_date, "%Y-%m-%d")
    end_date = datetime.datetime.strptime(end_date, "%Y-%m-%d")

    transactions = Transaction.query.filter(
        Transaction.added_date >= start_date, Transaction.added_date <= end_date
    ).all()
    transactions_per_day = defaultdict(int)

    for transaction in transactions:
        transaction_date = transaction.added_date.date()
        transactions_per_day[transaction_date] += 1

    response = {
        "transactions_per_day": {
            str(date): count for date, count in transactions_per_day.items()
        },
        "total_transactions": sum(transactions_per_day.values()),
    }

    return jsonify(response)


@statistics_bp.route("/average_exchange_rate", methods=["GET"])
def get_average_exchange_rate():
    start_date = request.args.get("start_date")
    end_date = request.args.get("end_date")
    granularity = request.args.get("granularity", "daily")  # lal defualts

    start_date = datetime.datetime.strptime(start_date, "%Y-%m-%d")
    end_date = datetime.datetime.strptime(end_date, "%Y-%m-%d")

    transactions = Transaction.query.filter(
        Transaction.added_date >= start_date, Transaction.added_date <= end_date
    ).all()

    lbp_to_usd_totals = defaultdict(float)
    usd_to_lbp_totals = defaultdict(float)
    counts = defaultdict(int)

    for transaction in transactions:
        if transaction.usd_to_lbp:
            usd_amount = transaction.usd_amount
            lbp_amount = transaction.lbp_amount
        else:
            usd_amount = transaction.lbp_amount
            lbp_amount = transaction.usd_amount

        transaction_date = transaction.added_date.date()

        if granularity == "daily":
            period = transaction_date
        elif granularity == "monthly":
            period = (transaction_date.year, transaction_date.month)
        elif granularity == "yearly":
            period = transaction_date.year
        else:
            return (
                jsonify(
                    {
                        "error": "Invalid granularity. Please choose either 'daily', 'monthly', or 'yearly'."
                    }
                ),
                400,
            )

        lbp_to_usd_totals[period] += lbp_amount / usd_amount
        usd_to_lbp_totals[period] += usd_amount / lbp_amount
        counts[period] += 1

    average_lbp_to_usd = {
        str(period): lbp_to_usd_totals[period] / counts[period]
        for period in lbp_to_usd_totals
    }
    average_usd_to_lbp = {
        str(period): usd_to_lbp_totals[period] / counts[period]
        for period in usd_to_lbp_totals
    }

    response = {"lbp_to_usd": average_lbp_to_usd, "usd_to_lbp": average_usd_to_lbp}

    return jsonify(response)

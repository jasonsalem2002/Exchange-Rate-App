from flask import Blueprint, request, jsonify
from ..app import db
from ..utils.util import extract_auth_token, decode_token
from ..models.Transaction import Transaction
from collections import defaultdict
import datetime


statistics_bp = Blueprint("statistics_bp", __name__)

@statistics_bp.route("/number_of_transactions", methods=["GET"])
def get_statistics():
    try:
        start_date = request.args.get("start_date")
        end_date = request.args.get("end_date")
        granularity = request.args.get("granularity", "daily")  # defaults to daily

        if not start_date or not end_date:
            return jsonify({"error": "Start date and end date are required."}), 400

        start_date = datetime.datetime.strptime(start_date, "%Y-%m-%d")
        end_date = datetime.datetime.strptime(end_date, "%Y-%m-%d")

        transactions = Transaction.query.filter(
            Transaction.added_date >= start_date, Transaction.added_date <= end_date
        ).all()
        
        if not transactions:
            return jsonify({"error": "No transactions found for the specified period."}), 404

        transactions_per_period = defaultdict(int)

        for transaction in transactions:
            transaction_date = transaction.added_date.date()

            if granularity == "daily":
                period = transaction_date.strftime("%Y-%m-%d")
            elif granularity == "weekly":
                # Calculate the week number of the transaction date
                week_number = transaction_date.isocalendar()[1]
                period = f"{transaction_date.year}-W{week_number:02d}"
            elif granularity == "monthly":
                period = transaction_date.strftime("%Y-%m")
            elif granularity == "yearly":
                period = transaction_date.year
            else:
                return (
                    jsonify(
                        {
                            "error": "Invalid granularity. Please choose either 'daily', 'weekly', 'monthly', or 'yearly'."
                        }
                    ),
                    400,
                )

            transactions_per_period[period] += 1

        response = {
            "transactions_per_period": {
                period: count for period, count in transactions_per_period.items()
            },
            "total_transactions": sum(transactions_per_period.values()),
        }

        return jsonify(response), 200

    except ValueError:
        return jsonify({"error": "Invalid date format. Please use YYYY-MM-DD."}), 400

    except Exception as e:
        print(e)
        return jsonify({"error": "Internal server error."}), 500


@statistics_bp.route("/average_exchange_rate", methods=["GET"])
def get_average_exchange_rate():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({"error": "Unauthorized, no token was provided"}), 401
        
        user_id = decode_token(token)
        if not user_id:
            return jsonify({"error": "Unauthorized, invalid token"}), 401

        start_date = request.args.get("start_date")
        end_date = request.args.get("end_date")
        granularity = request.args.get("granularity", "daily")  # defaults to daily

        if not start_date or not end_date:
            return jsonify({"error": "Start date and end date are required."}), 400

        start_date = datetime.datetime.strptime(start_date, "%Y-%m-%d")
        end_date = datetime.datetime.strptime(end_date, "%Y-%m-%d")

        transactions = Transaction.query.filter(
            Transaction.added_date >= start_date, Transaction.added_date <= end_date
        ).all()

        if not transactions:
            return jsonify({"error": "No transactions found for the specified period."}), 404

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
                period = transaction_date.strftime("%Y-%m-%d")
            elif granularity == "weekly":
                # Calculate the week number of the transaction date
                week_number = transaction_date.isocalendar()[1]
                period = f"{transaction_date.year}-W{week_number:02d}"
            elif granularity == "monthly":
                period = transaction_date.strftime("%Y-%m")
            elif granularity == "yearly":
                period = transaction_date.year
            else:
                return (
                    jsonify(
                        {
                            "error": "Invalid granularity. Please choose either 'daily', 'weekly', 'monthly', or 'yearly'."
                        }
                    ),
                    400,
                )

            lbp_to_usd_totals[period] += lbp_amount / usd_amount
            usd_to_lbp_totals[period] += usd_amount / lbp_amount
            counts[period] += 1

        average_lbp_to_usd = {
            period: lbp_to_usd_totals[period] / counts[period]
            for period in lbp_to_usd_totals
        }
        average_usd_to_lbp = {
            period: usd_to_lbp_totals[period] / counts[period]
            for period in usd_to_lbp_totals
        }

        response = {"lbp_to_usd": average_lbp_to_usd, "usd_to_lbp": average_usd_to_lbp}
        print(response)
        return jsonify(response), 200

    except ValueError:
        return jsonify({"error": "Invalid date format. Please use YYYY-MM-DD."}), 400

    except Exception as e:
        print(e)
        return jsonify({"error": "Internal server error."}), 500
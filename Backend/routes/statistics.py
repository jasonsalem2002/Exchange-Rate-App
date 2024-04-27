from flask import Blueprint, request, jsonify
from ..app import db
from ..utils.util import extract_auth_token, decode_token
from ..models.Transaction import Transaction
from collections import defaultdict
import datetime
from datetime import datetime, timedelta

statistics_bp = Blueprint("statistics_bp", __name__)

@statistics_bp.route("/number_of_transactions", methods=["GET"])
def get_statistics():
    try:
        start_date = request.args.get("start_date")
        end_date = request.args.get("end_date")
        granularity = request.args.get("granularity", "daily")  # daily el default

        if not start_date or not end_date:
            return jsonify({"error": "Start date and end date are required."}), 400

        start_date = datetime.strptime(start_date, "%Y-%m-%d")
        end_date = datetime.strptime(end_date, "%Y-%m-%d")

        if granularity == "daily":
            max_start_date = end_date - timedelta(days=30)
        elif granularity == "weekly":
            max_start_date = end_date - timedelta(weeks=52)  
        elif granularity == "monthly":
            max_start_date = end_date - timedelta(days=1096)
        else:
            max_start_date = None

        if max_start_date and start_date < max_start_date:
            start_date = max_start_date

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
            }
            # "total_transactions": sum(transactions_per_period.values()),
        }
        print(response)
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
        granularity = request.args.get("granularity", "daily")

        if not start_date or not end_date:
            return jsonify({"error": "Start date and end date are required."}), 400

        start_date = datetime.strptime(start_date, "%Y-%m-%d")
        end_date = datetime.strptime(end_date, "%Y-%m-%d")

        max_duration = None
        if granularity == "daily":
            max_duration = timedelta(days=90)
        elif granularity == "weekly":
            max_duration = timedelta(weeks=52)
        elif granularity == "monthly":
            max_duration = timedelta(days=36 * 30)

        if max_duration is not None and (end_date - start_date) > max_duration:
            end_date = start_date + max_duration

        if granularity not in ["daily", "weekly", "monthly", "yearly"]:
            return jsonify({"error": "Invalid granularity. Please choose either 'daily', 'weekly', 'monthly', or 'yearly'."}), 400

        transactions = Transaction.query.filter(
            Transaction.added_date >= start_date, Transaction.added_date <= end_date
        ).all()

        if not transactions:
            return jsonify({"error": "No transactions found for the specified period."}), 404

        lbp_to_usd_totals = defaultdict(float)
        usd_to_lbp_totals = defaultdict(float)
        lbp_to_usd_counts = defaultdict(int)
        usd_to_lbp_counts = defaultdict(int)

        for transaction in transactions:
            transaction_date = transaction.added_date.date()
            period = format_period(transaction_date, granularity)

            if transaction.usd_to_lbp:
                usd_to_lbp_totals[period] += transaction.lbp_amount / transaction.usd_amount
                usd_to_lbp_counts[period] += 1
            else:
                lbp_to_usd_totals[period] += transaction.lbp_amount / transaction.usd_amount
                lbp_to_usd_counts[period] += 1

        average_lbp_to_usd = {period: total / lbp_to_usd_counts[period] for period, total in lbp_to_usd_totals.items() if lbp_to_usd_counts[period] > 0}
        average_usd_to_lbp = {period: total / usd_to_lbp_counts[period] for period, total in usd_to_lbp_totals.items() if usd_to_lbp_counts[period] > 0}

        response = {"lbp_to_usd": average_lbp_to_usd, "usd_to_lbp": average_usd_to_lbp}
        return jsonify(response), 200

    except ValueError:
        return jsonify({"error": "Invalid date format. Please use YYYY-MM-DD."}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500

def format_period(date, granularity):
    if granularity == "daily":
        return date.strftime("%Y-%m-%d")
    elif granularity == "weekly":
        week_number = date.isocalendar()[1]
        return f"{date.year}-W{week_number:02d}"
    elif granularity == "monthly":
        return date.strftime("%Y-%m")
    elif granularity == "yearly":
        return str(date.year)


@statistics_bp.route("/highest_transaction_today", methods=["GET"])
def get_highest_transaction_today():
    try:
        today = datetime.today().date()
        start_of_day = datetime.combine(today, datetime.min.time())
        end_of_day = datetime.combine(today, datetime.max.time())

        highest_transaction = Transaction.query.filter(
            Transaction.added_date >= start_of_day,
            Transaction.added_date <= end_of_day
        ).order_by(Transaction.usd_amount.desc()).first()

        if not highest_transaction:
            return jsonify({"error": "No transactions found for today."}), 404

        response = {
            "highest_transaction": {
                "usd_amount": highest_transaction.usd_amount,
                "lbp_amount": highest_transaction.lbp_amount,
                "usd_to_lbp": highest_transaction.usd_to_lbp,
            }
        }

        return jsonify(response), 200

    except Exception as e:
        print(e)
        return jsonify({"error": "Internal server error."}), 500



@statistics_bp.route("/volume_of_transactions", methods=["GET"])
def get_volume_of_transactions():
    try:
        start_date = request.args.get("start_date")
        end_date = request.args.get("end_date")

        if not start_date or not end_date:
            return jsonify({"error": "Start date and end date are required."}), 400

        start_date = datetime.strptime(start_date, "%Y-%m-%d")
        end_date = datetime.strptime(end_date, "%Y-%m-%d")

        usd_volume = db.session.query(db.func.sum(Transaction.usd_amount)).filter(
            Transaction.usd_to_lbp == True,
            Transaction.added_date >= start_date,
            Transaction.added_date <= end_date
        ).scalar() or 0

        lbp_volume = db.session.query(db.func.sum(Transaction.lbp_amount)).filter(
            Transaction.usd_to_lbp == False,
            Transaction.added_date >= start_date,
            Transaction.added_date <= end_date
        ).scalar() or 0

        response = {
            "usd_volume": int(usd_volume),
            "lbp_volume": int(lbp_volume)
        }
        print(response)
        return jsonify(response), 200

    except ValueError:
        return jsonify({"error": "Invalid date format. Please use YYYY-MM-DD."}), 400

    except Exception as e:
        print(e)
        return jsonify({"error": "Internal server error."}), 500


@statistics_bp.route("/largest_transaction_amount", methods=["GET"])
def get_largest_transaction_amount():
    try:
        start_date = request.args.get("start_date")
        end_date = request.args.get("end_date")

        if not start_date or not end_date:
            return jsonify({"error": "Start date and end date are required."}), 400

        start_date = datetime.strptime(start_date, "%Y-%m-%d")
        end_date = datetime.strptime(end_date, "%Y-%m-%d")

        largest_transaction = Transaction.query.filter(
            Transaction.added_date >= start_date,
            Transaction.added_date <= end_date
        ).order_by(Transaction.usd_amount.desc()).first()

        if not largest_transaction:
            return jsonify({"error": "No transactions found for the specified period."}), 404

        response = {
            "largest_transaction": {
                "usd_amount": largest_transaction.usd_amount,
                "lbp_amount": largest_transaction.lbp_amount,
                "usd_to_lbp": largest_transaction.usd_to_lbp,
                "added_date": largest_transaction.added_date.strftime("%Y-%m-%d %H:%M:%S")
            }
        }

        return jsonify(response), 200

    except ValueError:
        return jsonify({"error": "Invalid date format. Please use YYYY-MM-DD."}), 400

    except Exception as e:
        print(e)
        return jsonify({"error": "Internal server error."}), 500
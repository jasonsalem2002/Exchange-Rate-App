from flask import Blueprint, jsonify, request
import datetime
from ..models.ExchangeRate import ExchangeRate

predicted_exchange_rate_bp = Blueprint("predicted_exchange_rate_bp", __name__)


@predicted_exchange_rate_bp.route("/predictRate", methods=["GET"])
def get_exchange_rate_by_date():
    try:
        requested_date_str = request.args.get("date")
        if requested_date_str:
            requested_date = datetime.datetime.strptime(requested_date_str, "%Y-%m-%d")
        else:
            return jsonify({"error": "Date parameter is missing"}), 400

        if requested_date > datetime.datetime(2030, 12, 30):
            return jsonify({"message": "I am not a GOD"}), 400

        start_of_day = requested_date
        end_of_day = requested_date + datetime.timedelta(days=1)

        transactions = ExchangeRate.query.filter(
            ExchangeRate.added_date >= start_of_day,
            ExchangeRate.added_date < end_of_day,
        ).all()

        if not transactions:
            nearest_transaction = (
                ExchangeRate.query.filter(ExchangeRate.added_date >= start_of_day)
                .order_by(ExchangeRate.added_date)
                .first()
                or ExchangeRate.query.filter(ExchangeRate.added_date < start_of_day)
                .order_by(ExchangeRate.added_date.desc())
                .first()
            )

            if not nearest_transaction:
                return (
                    jsonify({"error": "No transactions found near the requested date"}),
                    404,
                )

            nearest_date = nearest_transaction.added_date
            start_of_day = nearest_date.replace(
                hour=0, minute=0, second=0, microsecond=0
            )
            end_of_day = start_of_day + datetime.timedelta(days=1)

            transactions = ExchangeRate.query.filter(
                ExchangeRate.added_date >= start_of_day,
                ExchangeRate.added_date < end_of_day,
            ).all()

        total_amount = sum(t.lbp_amount for t in transactions)
        count = len(transactions)

        average_rate = (total_amount / count) if count > 0 else None
        if average_rate is not None:
            average_rate = round(average_rate, 2)

        return jsonify(
            {
                "date": start_of_day.strftime("%Y-%m-%d"),
                "average_exchange_rate": average_rate,
            }
        )

    except ValueError:
        return (
            jsonify(
                {
                    "error": "Invalid date format. Please provide date in YYYY-MM-DD format."
                }
            ),
            400,
        )
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@predicted_exchange_rate_bp.route("/next30DaysRates", methods=["GET"])
def get_next_30_days_rates():
    try:
        current_date = datetime.datetime.now().date()
        end_date = current_date + datetime.timedelta(days=30)
        results = []

        while current_date <= end_date:
            start_of_day = datetime.datetime.combine(current_date, datetime.time.min)
            end_of_day = start_of_day + datetime.timedelta(days=1)

            transactions = ExchangeRate.query.filter(
                ExchangeRate.added_date >= start_of_day,
                ExchangeRate.added_date < end_of_day,
            ).all()

            if transactions:
                total_amount = sum(t.lbp_amount for t in transactions)
                count = len(transactions)
                if count > 0:
                    average_rate = round((total_amount / count), 2)
                    results.append(
                        {
                            "date": current_date.strftime("%Y-%m-%d"),
                            "average_exchange_rate": average_rate,
                        }
                    )
            current_date += datetime.timedelta(days=1)
        print(results)
        return jsonify(results)

    except Exception as e:
        return jsonify({"error": str(e)}), 500

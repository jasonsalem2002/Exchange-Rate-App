from flask import Blueprint, request, jsonify
from ..app import db
from ..utils.util import extract_auth_token, decode_token
from ..models.Offer import Offer
from ..schemas.offerSchema import offer_schema, offers_schema
import jwt

offers_bp = Blueprint('offers_bp', __name__)

@offers_bp.route('/offers', methods=['POST'])
def create_offer():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        user_id = None
        try:
            user_id = decode_token(token)
        except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
            return jsonify({'error': 'Token is expired or is not valid'}), 403

        data = request.json
        if not data or not isinstance(data, dict):
            return jsonify({'error': 'Invalid data format. JSON object expected.'}), 400

        amount_requested = data.get('amount_requested')
        amount_to_trade = data.get('amount_to_trade')
        usd_to_lbp = data.get('usd_to_lbp')

        if amount_requested is None or amount_to_trade is None or usd_to_lbp is None:
            print(data)
            return jsonify({'error': 'Offer data is incomplete.'}), 400

        if not all(isinstance(val, (int, float)) for val in [amount_requested, amount_to_trade]) or not isinstance(usd_to_lbp, bool):
            return jsonify({'error': 'Invalid data types provided for offer amounts.'}), 400

        if amount_requested <= 0 or amount_to_trade <= 0:
            return jsonify({'error': 'Offer amounts must be positive numbers.'}), 400

        new_offer = Offer(user_id=user_id, amount_requested=amount_requested, amount_to_trade=amount_to_trade, usd_to_lbp=usd_to_lbp)

        db.session.add(new_offer)
        db.session.commit()

        return jsonify(offer_schema.dump(new_offer)), 201

    except Exception as e:
        print(e)  # Log the error
        return jsonify({'error': 'Internal server error.'}), 500


@offers_bp.route('/offers', methods=['GET'])
def get_all_offers():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        user_id = None
        try:
            user_id = decode_token(token)
        except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
            return jsonify({'error': 'Token is expired or is not valid'}), 403

        all_offers = Offer.query.all()
        serialized_offers = offers_schema.dump(all_offers)

        return jsonify(serialized_offers), 200

    except Exception as e:
        print(e)  # Log the error
        return jsonify({'error': 'Internal server error.'}), 500


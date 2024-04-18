from flask import Blueprint, request, jsonify
from ..app import db
from ..utils.util import extract_auth_token, decode_token
from ..models.Offer import Offer
from ..models.User import User
from ..schemas.offerSchema import offer_schema
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

        required_fields = {'amount_requested', 'amount_to_trade', 'usd_to_lbp'}
        unexpected_fields = set(data.keys()) - required_fields
        if unexpected_fields:
            return jsonify({'error': f'Unexpected fields in request body: {", ".join(unexpected_fields)}'}), 400

        amount_requested = data.get('amount_requested')
        amount_to_trade = data.get('amount_to_trade')
        usd_to_lbp = data.get('usd_to_lbp')

        if not all(isinstance(val, (int, float)) for val in [amount_requested, amount_to_trade]) or not isinstance(usd_to_lbp, bool):
            return jsonify({'error': 'Invalid data types provided for offer amounts.'}), 400

        if amount_requested <= 0 or amount_to_trade <= 0:
            return jsonify({'error': 'Offer amounts must be positive numbers.'}), 400

        mark_as = 'incomplete'

        new_offer = Offer(user_id=user_id, amount_requested=amount_requested, amount_to_trade=amount_to_trade, usd_to_lbp=usd_to_lbp, mark_as=mark_as)

        db.session.add(new_offer)
        db.session.commit()

        serialized_offer = offer_schema.dump(new_offer)
        serialized_offer.pop('mark_as')

        return jsonify(serialized_offer), 201

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500


@offers_bp.route('/offers', methods=['GET'])
def get_all_offers():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        incomplete_offers = Offer.query.filter_by(mark_as='incomplete').all()
        
        serialized_offers = []
        for offer in incomplete_offers:
            user = User.query.get(offer.user_id)
            if user:
                serialized_offer = offer_schema.dump(offer)
                serialized_offer['username'] = user.user_name
                serialized_offer.pop('user_id')
                serialized_offer.pop('mark_as')
                serialized_offers.append(serialized_offer)
        return jsonify(serialized_offers), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500


@offers_bp.route('/get_accepted_offers', methods=['GET'])
def get_accepted_offers():
    try:
        token = extract_auth_token(request)
        user_id = decode_token(token)

        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403
        
        complete_offers = Offer.query.filter_by(mark_as='complete').all()
        seruialized_offers = [offer_schema.dump(offer) for offer in complete_offers]
        for offer in seruialized_offers:
            offer.pop('mark_as')
            
        return jsonify(seruialized_offers), 200
    
    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
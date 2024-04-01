from flask import request, jsonify
from app import app, db
from utils.util import extract_auth_token, decode_token
import jwt
from models.Transaction import Transaction
from schemas.transactionSchema import transaction_schema, transactions_schema

@app.route('/transaction', methods=['POST'])
def create_transaction():
    try:
        data = request.json
        if not data or not isinstance(data, dict):
            return jsonify({'error': 'Invalid data format. JSON object expected.'}), 400
        
        usd_amount = data.get('usd_amount')
        lbp_amount = data.get('lbp_amount')
        usd_to_lbp = data.get('usd_to_lbp')

        if usd_amount is None or lbp_amount is None or usd_to_lbp is None:
            return jsonify({'error': 'Transaction data is incomplete.'}), 400

        if not all(isinstance(val, (int, float)) for val in [usd_amount, lbp_amount]) or not isinstance(usd_to_lbp, bool):
            return jsonify({'error': 'Invalid data types provided for transaction amounts.'}), 400

        if usd_amount <= 0 or lbp_amount <= 0:
            return jsonify({'error': 'Transaction amounts must be positive numbers.'}), 400

        user_id = None
        token = extract_auth_token(request)
        if token:
            try:
                user_id = decode_token(token)
            except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
                return jsonify({'error': 'Unauthorized'}), 403

        new_transaction = Transaction(usd_amount=usd_amount, lbp_amount=lbp_amount, usd_to_lbp=usd_to_lbp, user_id=user_id)

        db.session.add(new_transaction)
        db.session.commit()
        
        return jsonify(transaction_schema.dump(new_transaction)), 201
    
    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
    
@app.route('/transaction', methods=['GET'])
def get_user_transactions():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        try:
            user_id = decode_token(token)
        except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
            return jsonify({'error': 'Token is expired or is not valid'}), 403

        user_transactions = Transaction.query.filter_by(user_id=user_id).all()
        serialized_transactions = transactions_schema.dump(user_transactions)

        return jsonify(serialized_transactions), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
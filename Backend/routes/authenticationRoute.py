from flask import Blueprint, request, jsonify
from ..app import bcrypt
from ..models.User import User
from ..utils.util import create_token

auth_bp = Blueprint('auth_bp', __name__)

@auth_bp.route('/authentication', methods=['POST'])
def authenticate_user():
    try:
        data = request.json
        if not data or not isinstance(data, dict) or 'user_name' not in data or 'password' not in data:
            print(user_name, password)
            return jsonify({'error': 'Invalid request format.'}), 400

        user_name = data['user_name']
        password = data['password']

        existing_user = User.query.filter_by(user_name=user_name).first()
        if not existing_user:
            print(user_name, password)
            return jsonify({'error': 'User not found.'}), 404

        if not bcrypt.check_password_hash(existing_user.hashed_password, password):
            print(user_name, password)
            return jsonify({'error': 'Incorrect Username or Password.'}), 403

        token = create_token(existing_user.id)
        print(user_name, password, token)

        return jsonify({'token': token}), 200
        
    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
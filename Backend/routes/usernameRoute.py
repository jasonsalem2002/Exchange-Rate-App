from flask import Blueprint, request, jsonify
from ..models.User import User
from ..utils.util import extract_auth_token, decode_token

usernames_bp = Blueprint('usernames_bp', __name__)

@usernames_bp.route('/api/usernames', methods=['GET'])
def get_all_usernames():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        decode_result = decode_token(token)
        if not decode_result:
            return jsonify({'error': 'Invalid token'}), 403

        usernames = [user.user_name for user in User.query.all()]

        return jsonify(usernames), 200

    except Exception as e:
        print(e)
        return jsonify({'error': 'Internal server error.'}), 500
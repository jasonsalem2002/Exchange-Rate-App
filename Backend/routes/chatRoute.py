from flask import Blueprint, request, jsonify
from ..app import db
from ..models.Message import Message
from ..schemas.messageSchema import message_schema, messages_schema
from ..models.User import User
from ..utils.util import extract_auth_token, decode_token
import jwt

chat_bp = Blueprint('chat_bp', __name__)

@chat_bp.route('/chat', methods=['POST'])
def handle_send_message():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        sender_id = None
        try:
            sender_id = decode_token(token)
        except (jwt.ExpiredSignatureError, jwt.InvalidTokenError):
            return jsonify({'error': 'Token is expired or is not valid'}), 403

        data = request.json
        if not data or not isinstance(data, dict):
            return jsonify({'error': 'Invalid data format. JSON object expected.'}), 400

        recipient_username = data.get('recipient_username')
        content = data.get('content')

        if not recipient_username or not content:
            return jsonify({'error': 'Recipient username and message content are required.'}), 400

        recipient = User.query.filter_by(user_name=recipient_username).first()
        if not recipient:
            return jsonify({'error': 'Recipient not found.'}), 404

        if sender_id == recipient.id:
            return jsonify({'error': 'Sender and recipient cannot be the same.'}), 400

        new_message = Message(sender_id=sender_id, recipient_id=recipient.id, content=content)
        db.session.add(new_message)
        db.session.commit()

        sender_username = User.query.get(sender_id).user_name
        recipient_username = recipient.user_name

        response_data = {
            'added_date': new_message.added_date,
            'content': new_message.content,
            'sender_username': sender_username,
            'recipient_username': recipient_username
        }

        return jsonify(response_data), 201

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500


@chat_bp.route('/chat/<string:username>', methods=['GET'])
def get_user_messages(username):
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        current_user_id = decode_token(token)
        
        user = User.query.filter_by(user_name=username).first()
        if not user:
            return jsonify({'error': 'User not found.'}), 404

        if current_user_id != user.id:
            return jsonify({'error': 'You are not authorized to access this resource.'}), 403

        user_messages = Message.query.filter((Message.sender_id == user.id) | (Message.recipient_id == user.id)).all()

        formatted_messages = []
        for message in user_messages:
            sender_username = User.query.get(message.sender_id).user_name
            recipient_username = User.query.get(message.recipient_id).user_name
            formatted_message = {
                'added_date': message.added_date,
                'content': message.content,
                'sender_username': sender_username,
                'recipient_username': recipient_username
            }
            formatted_messages.append(formatted_message)

        return jsonify(formatted_messages), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

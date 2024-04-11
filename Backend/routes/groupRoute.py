from flask import Blueprint, request, jsonify
from ..app import db
from ..models.Group import Group, GroupMessage
from ..schemas.groupSchema import GroupSchema, GroupMessageSchema
from ..schemas.messageSchema import message_schema, messages_schema
from ..models.User import User
from ..utils.util import extract_auth_token, decode_token
import jwt

group_bp = Blueprint('group_bp', __name__)

def get_user_id(username):
    user = User.query.filter_by(user_name=username).first()
    if user:
        return user.id
    return None

@group_bp.route('/group', methods=['POST'])
def create_group():
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        data = request.json
        if not data or 'name' not in data:
            return jsonify({'error': 'Group name is required.'}), 400

        group_name = data['name']

        existing_group = Group.query.filter_by(name=group_name).first()
        if existing_group:
            return jsonify({'error': 'Sorry, this group name is already taken.'}), 400

        if not group_name.strip():
            return jsonify({'error': 'Group name cannot be empty.'}), 400

        if group_name.isdigit():
            return jsonify({'error': 'Group name cannot contain only numbers.'}), 400

        new_group = Group(name=group_name)
        db.session.add(new_group)
        db.session.commit()

        group_schema = GroupSchema()
        return jsonify(group_schema.dump(new_group)), 201

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

@group_bp.route('/group/<string:group_name>/message', methods=['POST'])
def send_group_message(group_name):
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        data = request.json
        if not data or 'content' not in data:
            return jsonify({'error': 'Message content is required.'}), 400

        content = data['content']

        group = Group.query.filter_by(name=group_name).first()
        if not group:
            return jsonify({'error': 'Group not found.'}), 404

        sender_id = decode_token(token)
        sender = User.query.get(sender_id)
        if not sender:
            return jsonify({'error': 'Sender not found.'}), 404

        new_message = GroupMessage(group_id=group.id, sender_id=sender.id, content=content)
        db.session.add(new_message)
        db.session.commit()

        response_data = {
            'group_name': group.name,
            'added_date': new_message.added_date,
            'sender_username': sender.user_name,
            'content': new_message.content
        }

        return jsonify(response_data), 201

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

@group_bp.route('/group/<string:group_name>/messages', methods=['GET'])
def get_group_messages(group_name):
    try:
        token = extract_auth_token(request)
        if not token:
            return jsonify({'error': 'Unauthorized, no token was provided'}), 403

        group = Group.query.filter_by(name=group_name).first()
        if not group:
            return jsonify({'error': 'Group not found.'}), 404

        group_messages = GroupMessage.query.filter_by(group_id=group.id).all()

        formatted_messages = []
        for message in group_messages:
            sender = User.query.get(message.sender_id)
            formatted_message = {
                'group_name': group.name,
                'added_date': message.added_date,
                'sender_username': sender.user_name,
                'content': message.content
            }
            formatted_messages.append(formatted_message)

        return jsonify(formatted_messages), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
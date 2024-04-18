from flask import Blueprint, request, jsonify
from ..app import db
from ..models.Group import Group, GroupMessage
from ..schemas.groupSchema import GroupSchema
from ..models.User import User
from ..utils.util import extract_auth_token, decode_token

group_bp = Blueprint('group_bp', __name__)

def get_user_id(username):
    user = User.query.filter_by(user_name=username).first()
    if user:
        return user.id
    return None

def authenticate_request():
    token = extract_auth_token(request)
    if not token:
        return jsonify({'error': 'Unauthorized, no token was provided'}), 403

    decode_result = decode_token(token)
    if not decode_result:
        return jsonify({'error': 'Invalid token'}), 403

    return decode_result

@group_bp.route('/group', methods=['POST'])
def create_group():
    try:
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

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

        creator_id = decode_result
        creator = User.query.get(creator_id)
        if not creator:
            return jsonify({'error': 'Creator not found.'}), 404

        new_group.members.append(creator)
        db.session.commit()

        group_schema = GroupSchema()
        return jsonify(group_schema.dump(new_group)), 201

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500


@group_bp.route('/group/<string:group_name>/message', methods=['POST'])
def send_group_message(group_name):
    try:
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

        data = request.json
        if not data or 'content' not in data:
            return jsonify({'error': 'Message content is required.'}), 400

        content = data['content']

        group = Group.query.filter_by(name=group_name).first()
        if not group:
            return jsonify({'error': 'Group not found.'}), 404

        sender_id = decode_result
        sender = User.query.get(sender_id)
        if not sender:
            return jsonify({'error': 'Sender not found.'}), 404

        if sender not in group.members:
            return jsonify({'error': 'You are not a member of this group.'}), 403

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
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

        group = Group.query.filter_by(name=group_name).first()
        if not group:
            return jsonify({'error': 'Group not found.'}), 404

        sender_id = decode_result
        sender = User.query.get(sender_id)
        if not sender:
            return jsonify({'error': 'Sender not found.'}), 404

        if sender not in group.members:
            return jsonify({'error': 'You are not a member of this group.'}), 403

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

@group_bp.route('/groups', methods=['GET'])
def get_group_names():
    try:
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

        groups = Group.query.all()
        group_names = [group.name for group in groups]
        return jsonify(group_names), 200
    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

@group_bp.route('/group/<string:group_name>/join', methods=['POST'])
def join_group(group_name):
    try:
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

        user_id = decode_result
        user = User.query.get(user_id)
        if not user:
            return jsonify({'error': 'User not found.'}), 404

        group = Group.query.filter_by(name=group_name).first()
        if not group:
            return jsonify({'error': 'Group not found.'}), 404

        if user not in group.members:
            group.members.append(user)
            db.session.commit()

        return jsonify({'message': 'User joined the group successfully.'}), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

@group_bp.route('/group/<string:group_name>/leave', methods=['POST'])
def leave_group(group_name):
    try:
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

        user_id = decode_result
        user = User.query.get(user_id)
        if not user:
            return jsonify({'error': 'User not found.'}), 404

        group = Group.query.filter_by(name=group_name).first()
        if not group:
            return jsonify({'error': 'Group not found.'}), 404

        if user in group.members:
            group.members.remove(user)
            db.session.commit()

        return jsonify({'message': 'User left the group successfully.'}), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

@group_bp.route('/my-groups', methods=['GET'])
def get_my_groups():
    try:
        decode_result = authenticate_request()
        if isinstance(decode_result, tuple):
            return decode_result

        sender_id = decode_result
        sender = User.query.get(sender_id)
        if not sender:
            return jsonify({'error': 'Sender not found.'}), 404

        my_groups = sender.groups_joined.all()

        group_names = [group.name for group in my_groups]
        return jsonify(group_names), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500
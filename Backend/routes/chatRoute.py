from flask import Blueprint, request
from Backend import socketio

chat_bp = Blueprint('chat_bp', __name__)

@chat_bp.route('/api/chat', methods=['POST'])
def handle_send_message():
    data = request.json  # Assuming data includes sender_id, recipient_id, content
    socketio.emit('receive_message', data, room=data['recipient_id'])
    return '', 204
from flask import Blueprint, request, jsonify
from ..app import db
from ..utils.util import extract_auth_token, decode_token
from ..models.Offer import Offer

accepted_offer_bp = Blueprint('accepted_offer_bp', __name__)

@accepted_offer_bp.route('/accept_offer/<int:offer_id>', methods=['PUT'])
def accept_offer(offer_id):
    try:
        token = extract_auth_token(request)
        user_id = decode_token(token)
        
        offer = Offer.query.filter_by(id=offer_id, user_id=user_id).first()
        if not offer:
            return jsonify({'error': 'Offer not found or unauthorized'}), 404

        offer.mark_as = 'complete'
        db.session.commit()

        return jsonify({'message': 'Offer accepted successfully'}), 200

    except Exception as e:
        return jsonify({'error': 'Internal server error.'}), 500

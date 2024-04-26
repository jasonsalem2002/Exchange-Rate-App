from flask import Blueprint, request, jsonify
from ..app import db
from ..utils.util import extract_auth_token, decode_token
from ..models.Offer import Offer
from ..models.Transaction import Transaction

accepted_offer_bp = Blueprint("accepted_offer_bp", __name__)


@accepted_offer_bp.route("/accept_offer/<int:offer_id>", methods=["PUT"])
def accept_offer(offer_id):
    try:
        token = extract_auth_token(request)
        user_id = decode_token(token)

        if not token:
            return jsonify({"error": "Unauthorized, no token was provided"}), 403

        offer = Offer.query.filter_by(id=offer_id).first()
        if not offer:
            return jsonify({"error": "Offer not found or unauthorized"}), 404

        if user_id == offer.user_id:
            return jsonify({"error": "Unauthorized, cannot accept your own offer"}), 403

        offer.mark_as = "complete"
        
        new_transaction = Transaction(
            usd_amount=offer.amount_to_trade if offer.usd_to_lbp else offer.amount_requested,
            lbp_amount=offer.amount_requested if offer.usd_to_lbp else offer.amount_to_trade,
            usd_to_lbp=offer.usd_to_lbp,
            user_id=offer.user_id
        )
        db.session.add(new_transaction)
        db.session.commit()

        db.session.delete(offer)
        db.session.commit()

        return jsonify({"message": "Offer accepted successfully"}), 200

    except Exception as e:
        print(e)
        return jsonify({"error": "Internal server error."}), 500

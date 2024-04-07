from ..app import db
import datetime

class Offer(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=True)
    amount_requested = db.Column(db.Float)
    amount_to_trade = db.Column(db.Float)
    usd_to_lbp = db.Column(db.Boolean)
    added_date = db.Column(db.DateTime, default=datetime.datetime.now)

    def __init__(self, user_id, amount_requested, amount_to_trade, usd_to_lbp):
        self.user_id = user_id
        self.amount_requested = amount_requested
        self.amount_to_trade = amount_to_trade
        self.usd_to_lbp = usd_to_lbp
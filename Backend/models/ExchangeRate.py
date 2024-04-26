from ..app import db
from datetime import datetime


class ExchangeRate(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    lbp_amount = db.Column(db.Float)
    added_date = db.Column(db.DateTime)

    def __init__(self, lbp_amount):
        self.lbp_amount = lbp_amount
        self.added_date = datetime.now()

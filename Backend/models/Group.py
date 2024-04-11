from ..app import db
from datetime import datetime

class Group(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), unique=True, nullable=False)

class GroupMessage(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    group_id = db.Column(db.Integer, db.ForeignKey('group.id'), nullable=False)
    sender_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    content = db.Column(db.Text, nullable=False)
    added_date = db.Column(db.DateTime, default=datetime.now)

    group = db.relationship('Group', backref=db.backref('messages', lazy=True))
    sender = db.relationship('User', backref=db.backref('group_messages', lazy=True))
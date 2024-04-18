from ..app import db
from datetime import datetime

group_members = db.Table(
    "group_members",
    db.Column("user_id", db.Integer, db.ForeignKey("user.id"), primary_key=True),
    db.Column("group_id", db.Integer, db.ForeignKey("group.id"), primary_key=True),
)


class Group(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), unique=True, nullable=False)

    members = db.relationship(
        "User",
        secondary=group_members,
        backref=db.backref("groups_joined", lazy="dynamic"),
    )


class GroupMessage(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    group_id = db.Column(db.Integer, db.ForeignKey("group.id"), nullable=False)
    sender_id = db.Column(db.Integer, db.ForeignKey("user.id"), nullable=False)
    content = db.Column(db.Text, nullable=False)
    added_date = db.Column(db.DateTime, default=datetime.now)

    group = db.relationship("Group", backref=db.backref("messages", lazy=True))
    sender = db.relationship("User", backref=db.backref("group_messages", lazy=True))

from ..app import db, bcrypt

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    user_name = db.Column(db.String(30), unique=True)
    hashed_password = db.Column(db.String(128))

    def __init__(self, user_name, password):
        self.user_name = user_name
        self.hashed_password = bcrypt.generate_password_hash(password)
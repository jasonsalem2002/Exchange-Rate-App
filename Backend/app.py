# app.py
from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt

db = SQLAlchemy()
ma = Marshmallow()
bcrypt = Bcrypt()

def create_app():
    app = Flask(__name__)
    app.config.from_pyfile('config.py')

    db.init_app(app)
    ma.init_app(app)
    bcrypt.init_app(app)
    CORS(app)

    from .routes.authenticationRoute import auth_bp
    from .routes.exchangeRateRoute import exchange_rate_bp
    from .routes.transactionRoute import transaction_bp
    from .routes.userRoute import user_bp

    app.register_blueprint(auth_bp, url_prefix='/api')
    app.register_blueprint(exchange_rate_bp, url_prefix='/api')
    app.register_blueprint(transaction_bp, url_prefix='/api')
    app.register_blueprint(user_bp, url_prefix='/api')

    return app
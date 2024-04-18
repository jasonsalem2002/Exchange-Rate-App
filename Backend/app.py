from flask import Flask, request, jsonify
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
from flask_migrate import Migrate

db = SQLAlchemy()
ma = Marshmallow()
bcrypt = Bcrypt()


def create_app():
    from .models import Offer, Transaction, User, Message, Group

    app = Flask(__name__)
    app.config.from_pyfile("config.py")
    db.init_app(app)
    ma.init_app(app)
    bcrypt.init_app(app)
    CORS(app, resources={r"/*": {"origins": "*"}})
    migrate = Migrate(app, db)

    from Backend.routes.authenticationRoute import auth_bp
    from Backend.routes.exchangeRateRoute import exchange_rate_bp
    from Backend.routes.transactionRoute import transaction_bp
    from Backend.routes.userRoute import user_bp
    from Backend.routes.statistics import statistics_bp
    from Backend.routes.offersRoute import offers_bp
    from Backend.routes.acceptedOfferRoute import accepted_offer_bp
    from Backend.routes.chatRoute import chat_bp
    from Backend.routes.usernameRoute import usernames_bp
    from Backend.routes.groupRoute import group_bp

    app.register_blueprint(auth_bp)
    app.register_blueprint(exchange_rate_bp)
    app.register_blueprint(transaction_bp)
    app.register_blueprint(user_bp)
    app.register_blueprint(statistics_bp)
    app.register_blueprint(offers_bp)
    app.register_blueprint(accepted_offer_bp)
    app.register_blueprint(chat_bp)
    app.register_blueprint(usernames_bp)
    app.register_blueprint(group_bp)

    # @app.before_request
    # def block_options():
    #     if request.method == 'OPTIONS':
    #         return jsonify({'error': 'Method Not Allowed'}), 405

    return app


if __name__ == "__main__":
    app = create_app()

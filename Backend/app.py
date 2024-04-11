from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
from flask_socketio import SocketIO
from flask_migrate import Migrate

db = SQLAlchemy()
ma = Marshmallow()
bcrypt = Bcrypt()
socketio = SocketIO()

def create_app():
    from .models import Offer, Transaction, User, Message
    app = Flask(__name__)
    app.config.from_pyfile('config.py')
    db.init_app(app)
    ma.init_app(app)
    bcrypt.init_app(app)
    CORS(app)
    socketio.init_app(app)

    # was used to create the database tables
    migrate = Migrate(app, db)

    from Backend.routes.authenticationRoute import auth_bp
    from Backend.routes.exchangeRateRoute import exchange_rate_bp
    from Backend.routes.transactionRoute import transaction_bp
    from Backend.routes.userRoute import user_bp
    from Backend.routes.statistics import statistics_bp
    from Backend.routes.offersRoute import offers_bp
    from Backend.routes.acceptedOfferRoute import accepted_offer_bp
    from Backend.routes.chatRoute import chat_bp

    app.register_blueprint(auth_bp, url_prefix='/api')
    app.register_blueprint(exchange_rate_bp, url_prefix='/api')
    app.register_blueprint(transaction_bp, url_prefix='/api')
    app.register_blueprint(user_bp, url_prefix='/api')
    app.register_blueprint(statistics_bp, url_prefix='/api')
    app.register_blueprint(offers_bp, url_prefix='/api')
    app.register_blueprint(accepted_offer_bp, url_prefix='/api')
    app.register_blueprint(chat_bp)

    return app

if __name__ == '__main__':
    app = create_app()
    socketio.run(app)  # Use SocketIO's run method instead of Flask's run

from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
# from flask_migrate import Migrate

db = SQLAlchemy()
ma = Marshmallow()
bcrypt = Bcrypt()

def create_app():
    # from .models import Offer, Transaction, User

    app = Flask(__name__)
    app.config.from_pyfile('config.py')
    db.init_app(app)
    ma.init_app(app)
    bcrypt.init_app(app)
    CORS(app)

    # was used to create the database tables
    # migrate = Migrate(app, db)

    from .routes.authenticationRoute import auth_bp
    from .routes.exchangeRateRoute import exchange_rate_bp
    from .routes.transactionRoute import transaction_bp
    from .routes.userRoute import user_bp
    from .routes.statistics import statistics_bp
    from .routes.offersRoute import offers_bp
    from .routes.acceptedOfferRoute import accepted_offer_bp  

    app.register_blueprint(auth_bp, url_prefix='/api')
    app.register_blueprint(exchange_rate_bp, url_prefix='/api')
    app.register_blueprint(transaction_bp, url_prefix='/api')
    app.register_blueprint(user_bp, url_prefix='/api')
    app.register_blueprint(statistics_bp, url_prefix='/api')
    app.register_blueprint(offers_bp, url_prefix='/api')
    app.register_blueprint(accepted_offer_bp, url_prefix='/api')
    return app

if __name__ == '__main__':
    app = create_app()
    app.run()

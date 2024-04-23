from flask import Flask, request, jsonify, send_from_directory
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_marshmallow import Marshmallow
from flask_bcrypt import Bcrypt
from flask_migrate import Migrate
import os

db = SQLAlchemy()
ma = Marshmallow()
bcrypt = Bcrypt()

UPLOAD_FOLDER = 'uploads'  # Define the upload folder

def create_app():
    from .models import Offer, Transaction, User, Message, Group

    app = Flask(__name__)
    app.config.from_pyfile("config.py")
    app.config['UPLOAD_FOLDER'] = "files"  # Set the upload folder in the app config
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

    # Route for file upload
    @app.route('/upload', methods=['POST'])
    def upload_file():
        if 'file' not in request.files:
            return 'No file part in the request', 400
        file = request.files['file']
        if file.filename == '':
            return 'No file selected', 400
        if file:
            filename = file.filename
            file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
            return 'File uploaded successfully', 200

    # Route for file download
    @app.route('/download/<filename>', methods=['GET'])
    def download_file(filename):
        try:
            return send_from_directory(app.config['UPLOAD_FOLDER'], filename, as_attachment=True)
        except FileNotFoundError:
            return 'File not found', 404

    return app


if __name__ == "__main__":
    app = create_app()

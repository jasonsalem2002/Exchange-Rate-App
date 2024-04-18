from flask import Blueprint, request, jsonify
from ..models.User import User
from ..app import db
from ..schemas.userSchema import user_schema
import re

user_bp = Blueprint("user_bp", __name__)


@user_bp.route("/user", methods=["POST"])
def create_user():
    try:
        data = request.json

        if not data or not isinstance(data, dict):
            return jsonify({"error": "Invalid data format. JSON object expected."}), 400

        user_name = data.get("user_name")
        password = data.get("password")

        if not user_name or not password:
            return jsonify({"error": "Username and password are required fields."}), 400

        existing_user = User.query.filter_by(user_name=user_name).first()
        if existing_user:
            return (
                jsonify(
                    {
                        "error": "Username already exists. Please choose a different username."
                    }
                ),
                400,
            )

        if not re.match("^[a-zA-Z0-9_]*$", user_name):
            return (
                jsonify(
                    {
                        "error": "Username can only contain alphanumeric characters and underscores."
                    }
                ),
                400,
            )

        if not re.match(
            r"^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&_])[A-Za-z\d@$!%*?&_]{8,64}$",
            password,
        ):
            return (
                jsonify(
                    {
                        "error": "Password must be between 8 and 64 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
                    }
                ),
                400,
            )

        new_user = User(user_name=user_name, password=password)

        db.session.add(new_user)
        db.session.commit()

        return jsonify(user_schema.dump(new_user)), 201
    except Exception as e:
        db.session.rollback()
        return jsonify({"error": "Internal server error."}), 500

from flask import Flask
from flask_marshmallow import Marshmallow

app = Flask(__name__)
ma = Marshmallow(app)

if __name__ == '__main__':
    app.run(debug=True)
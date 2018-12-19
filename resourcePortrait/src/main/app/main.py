import json

from flask import Flask
#import getData

app = Flask(__name__)


@app.route('/')
@app.route('/index')
def index():
    return ""

app.run(debug = True)
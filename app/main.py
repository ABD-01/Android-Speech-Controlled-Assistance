from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/digitalWrite', methods=['GET'])
def funt(pin=None):
    if request.method == 'GET':

        pin = request.args.get('pin', default=None, type=int)
        if pin is None:
            return jsonify(
                value = "pin arg is missing",
                success = 0
            )

        state = request.args.get('state', default=None, type=str)
        if state is None:
            return jsonify(
                value = "state arg is missing",
                success = 0
            )

        deviceName = request.args.get('deviceName', default=None, type=str)
        if deviceName is None:
            return jsonify(
                value = "Missing deviceID arg",
                success = 0
            )

        return jsonify(
            value = 1,
            success = 1
        )

    else:
        return "Post Method Not Allowed", 405

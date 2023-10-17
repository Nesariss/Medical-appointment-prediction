from flask import Flask,request,jsonify
import numpy as np
import pickle

model = pickle.load(open('xgb.pkl','rb'))

app = Flask(__name__)

@app.route('/')
def index():
    return "Hello world"

@app.route('/predict',methods=['POST'])
def predict():

    data = request.get_json()

    gender = int(data.get('gender'))
    age = int(data.get('age'))
    scholarship = int(data.get('scholarship'))
    hipertension = int(data.get('hipertension'))
    diabetes = int(data.get('diabetic'))
    alcoholism = int(data.get('alcoholic'))
    handicap = int(data.get('handicap'))
    sms_received = int(data.get('SMS'))
    time_to_visit = int(data.get('timetovisit'))

    input_query = np.array([[gender, age, scholarship, hipertension, diabetes, alcoholism, handicap, sms_received, time_to_visit]])
    print(type(input_query))
    
    result = model.predict(input_query)[0]
    print(result)

    return (str(result))

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=5000, debug=True)
import json
import build_model
import data_helper
import matplotlib.pyplot as plt
from keras.models import load_model


train_dir = r'/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/model/model'
parameter_file = r'/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/python/training_config.json'
train_file = "/home/wangyuzhong/ideaProjects/mixed-deployment/loadForecasting/src/main/resource/daily-minimum-temperatures-in-me.csv"

def test():
	params = json.loads(open(parameter_file).read())
	x_train, y_train, x_test, y_test, x_test_raw, y_test_raw, last_window_raw, last_window = data_helper.load_timeseries(
		train_file, params)
	model = load_model(train_dir)
	# Predict next time stamp
	next_timestamp = build_model.predict_next_timestamp(model, last_window)
	next_timestamp_raw = (next_timestamp[0] + 1) * last_window_raw[0][0]
	return next_timestamp_raw


def train_predict():
	"""Train and predict time series data"""

	# Load command line arguments 
	# train_file = sys.argv[1]
	# parameter_file = sys.argv[2]
	train_file = "../daily-minimum-temperatures-in-me.csv"
	parameter_file = "./training_config.json"

	# Load training parameters
	params = json.loads(open(parameter_file).read())

	# Load time series dataset, and split it into train and test
	x_train, y_train, x_test, y_test, x_test_raw, y_test_raw,last_window_raw, last_window = data_helper.load_timeseries(train_file, params)
	lstm_layer = [1, params['window_size'], params['hidden_unit'], 1]
	model = build_model.rnn_lstm(lstm_layer, params)

	# Train RNN (LSTM) model with train set
	#model.fit
	model.fit(
		x_train,
		y_train,
		batch_size=params['batch_size'],
		epochs=params['epochs'],
		validation_split=params['validation_split'])

	model.save(train_dir)
	# Check the model against test set
	predicted = build_model.predict_next_timestamp(model, x_test)        
	predicted_raw = []
	for i in range(len(x_test_raw)):
		predicted_raw.append((predicted[i] + 1) * x_test_raw[i][0])

	# Plot graph: predicted VS actual
	plt.subplot(111)
	plt.plot(predicted_raw, label='Actual')
	plt.plot(y_test_raw, label='Predicted')	
	plt.legend()
	plt.show()

	return last_window,last_window_raw






print "start forecast!"
print('The next time stamp forecasting is: {}'.format(test()))

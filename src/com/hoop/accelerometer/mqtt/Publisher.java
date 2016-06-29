package com.hoop.accelerometer.mqtt;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import android.content.Context;
import android.util.Log;

public class Publisher {

	private static final String TAG = Publisher.class.getName();

	private String host = null;

	private int port = 0;

	private MqttAndroidClient client = null;

	private Context context = null;

	private MqttConnectOptions conOpt;
	
	private boolean connected;

	private static Publisher publisher;

	private Publisher() {
	}

	public synchronized static Publisher getPublisher() {
		if (publisher == null) {
			publisher = new Publisher();
		}
		return publisher;
	}

	public void connect(String host, int port, Context ctx) throws Exception {
		this.host = host;
		this.port = port;
		this.context = ctx;

		String uri = "tcp://" + this.host + ":" + this.port;
		Log.v(TAG, "connecting to " + uri);
		this.client = new MqttAndroidClient(this.context, uri,
				MqttClient.generateClientId());
		this.conOpt = new MqttConnectOptions();
		this.conOpt.setCleanSession(true);
		IMqttToken token = this.client.connect(this.conOpt);
		token.setActionCallback(new IMqttActionListener() {
			@Override
			public void onSuccess(IMqttToken asyncActionToken) {
				// We are connected
				Log.i(TAG, "successfully connected!");
				Publisher.this.connected = true;
			}

			@Override
			public void onFailure(IMqttToken asyncActionToken,
					Throwable exception) {
				// Something went wrong e.g. connection timeout or firewall
				// problems
				Log.e(TAG, "Unable to connect!");
				Publisher.this.connected = false;
			}
		});
		Log.i(TAG, "Connected to " + uri);
	}

	public void disconnect() throws Exception {
		if (this.connected && this.client != null) {
			this.client.disconnect();
			Log.i(TAG, "Disconnected");
		} else {
			Log.i(TAG, "Not Connected!");
		}
	}

	public void publish(float y) {
		if (this.connected && this.client != null) {
			try {
				this.client.publish("accelerometer", String.valueOf(y)
						.getBytes(), 0, false);
			} catch (Exception e) {
				Log.e(TAG, "Error when publishing", e);
			}
		}
	}
}

package com.example.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ConnectedActivity extends Activity implements SensorEventListener {

	private SensorManager senSensorManager;
	private Sensor senAccelerometer;
	
	private long lastUpdate;

	private static final String TAG = ConnectedActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connected);
		senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		senAccelerometer = senSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		senSensorManager.registerListener(this, senAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	protected void onPause() {
		super.onPause();
		Log.v(TAG, "unregistered sensor");
		senSensorManager.unregisterListener(this);
	}

	protected void onResume() {
		super.onResume();
		senSensorManager.registerListener(this, senAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connected, menu);
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		Log.v(TAG, "accuracy changed event");
	}

	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Sensor mySensor = sensorEvent.sensor;

		if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float y = sensorEvent.values[1];
			long curTime = System.currentTimeMillis();
	        if ((curTime - lastUpdate) > 500) {
	        	Log.v(TAG, "y pos: " + y);
	            lastUpdate = curTime;
	            TextView ypos = (TextView)findViewById(R.id.ypos);
				ypos.setText(String.valueOf(y));
	        }			
		}
	}
	
	public void disconnect(View view) {
        Log.i(TAG, "Disconnect clicked");
        finish();
    }

}

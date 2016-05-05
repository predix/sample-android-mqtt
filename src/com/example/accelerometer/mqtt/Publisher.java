package com.example.accelerometer.mqtt;

public class Publisher {

	private static Publisher publisher;

	private Publisher() {
	}

	public synchronized static Publisher getPublisher() {
		if (publisher == null) {
			publisher = new Publisher();
		}
		return publisher;
	}
	
	public void connect(String host, int port) throws Exception {
		
	}
}

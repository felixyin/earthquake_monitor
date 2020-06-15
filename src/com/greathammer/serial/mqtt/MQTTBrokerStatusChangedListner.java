package com.greathammer.serial.mqtt;

public interface MQTTBrokerStatusChangedListner {
	public void connectionLost();

	public void connected();
}

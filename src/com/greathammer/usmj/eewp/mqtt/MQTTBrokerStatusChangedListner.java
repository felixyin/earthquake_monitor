package com.greathammer.usmj.eewp.mqtt;

public interface MQTTBrokerStatusChangedListner {
	public void connectionLost();

	public void connected();
}

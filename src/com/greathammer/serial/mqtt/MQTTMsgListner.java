package com.greathammer.serial.mqtt;

public interface MQTTMsgListner {
	public void messageArrived(MQTTSimpleMessage mqttMsg);
}

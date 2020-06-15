package com.greathammer.usmj.eewp.mqtt;

public interface MQTTMsgListner {
	public void messageArrived(MQTTSimpleMessage mqttMsg);
}

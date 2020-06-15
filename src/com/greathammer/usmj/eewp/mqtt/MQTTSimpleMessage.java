package com.greathammer.usmj.eewp.mqtt;

import java.util.Date;

import com.greathammer.usmj.eewp.time.DateHelper;

/**
 * MQTTSimpleMessage.
 * 
 * @author Zheng Chao
 * @version 0.1.1.2
 */
public class MQTTSimpleMessage {
	private final Date _messageArrivedDate;
	private final MQTTBroker _broker;
	private final String _topicName;
	private final byte[] _payload;
	private final int _qos;
	private final boolean _retained;

	public MQTTSimpleMessage(Date messageArrivedDate, MQTTBroker broker, String topicName, byte[] payload, int qos,
			boolean retained) {
		_messageArrivedDate = messageArrivedDate;
		_broker = broker;
		_topicName = topicName;
		_payload = payload;
		_qos = qos;
		_retained = retained;
	}

	@Override
	public String toString() {
		return getClass().getName() + ", hashcode:" + hashCode() + ":" + "MQTT接收时间:"
				+ DateHelper.dateToStringFormate(_messageArrivedDate) + ";payload长度:" + _payload.length + ";topic:"
				+ _topicName + ";接收broker:" + _broker;
	}

	public Date getMessageArrivedDate() {
		return _messageArrivedDate;
	}

	public MQTTBroker getBroker() {
		return _broker;
	}

	public String getTopicName() {
		return _topicName;
	}

	public byte[] getPayload() {
		return _payload;
	}

	public int getQos() {
		return _qos;
	}

	public boolean isRetained() {
		return _retained;
	}

}

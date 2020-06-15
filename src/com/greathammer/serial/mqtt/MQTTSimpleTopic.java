package com.greathammer.serial.mqtt;

/**
 * MQTTSimpleTopic.
 * 
 * @author Zheng Chao
 * @version 0.1.1.2
 */
public class MQTTSimpleTopic implements Comparable<MQTTSimpleTopic> {

	private final int _qos;
	private final String _topicName;

	public MQTTSimpleTopic(String topicNmae, int qos) {
		_topicName = topicNmae;
		_qos = qos;
	}

	public String getTopicName() {
		return _topicName;
	}

	public int getQOS() {
		return _qos;
	}

	@Override
	public int compareTo(MQTTSimpleTopic o) {
		if (_qos < o._qos)
			return 1;
		if (_qos == o._qos) {
			return _topicName.compareTo(o._topicName);
		}
		return -1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MQTTSimpleTopic other = (MQTTSimpleTopic) obj;
		return _topicName.equals(other._topicName) && _qos == other._qos;
	}

	@Override
	public String toString() {
		return getClass().getName() + ", hashcode:" + hashCode() + ":" + " TopicName: " + _topicName + "; QOS: " + _qos;
	}
}

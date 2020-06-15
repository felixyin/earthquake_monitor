package com.greathammer.usmj.eewp.mqtt;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.greathammer.usmj.eewp.exception.ZCSimpleException;
import com.greathammer.usmj.eewp.log.DefaultLogService;
import com.greathammer.usmj.eewp.log.LogService;
import com.greathammer.usmj.eewp.log.LogSimpleObject;
import com.greathammer.usmj.eewp.time.DateHelper;
import com.ibm.mqtt.MqttAdvancedCallback;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;

/**
 * MQTT Broker. <br>
 * One Broker manages one client. <br>
 * MQTT客户端，负责建立与MQTT服务器连接、维持链路、接收发送消息.
 * 
 * @author Zheng Chao
 * @since Java 7.0
 * @version 0.2.2
 * @version 0.2.3 改变sendMessage方法执行进程
 * @version 0.2.4 增加LogContentTag 对Log的信息进行了分类
 * @version 目前版本:{@value #VERSION}}
 */
public class MQTTBroker implements MqttAdvancedCallback, Comparable<MQTTBroker> {
	public static final String VERSION = "0.2.4.4";

	private final String MQTT_SERVER_URL;
	private final int MQTT_SERVER_PORT;
	private final boolean CLEAN_START;
	private final short KEEP_ALIVE;
	private final String CLIENT_ID;
	private final LogService LOG;
	private final MQTTMsgListner MSGListner;
	private final MQTTBrokerStatusChangedListner STATUSListner;
	// GuardThread
	private final ScheduledExecutorService GuardThread;
	// boolean _waitForCheckedMsg = false;
	// boolean _waitForCheckedTimeOut = false;

	private MqttClient _mqttClient;

	private MQTTBrokerStatus _connectStatus = MQTTBrokerStatus.Unconnected;

	public enum MQTTBrokerStatus {
		Connected, Unconnected, Connecting
	};

	public enum MQTTBrokerLogContentTag {
		MQTTConnection, MQTTLaucher, MQTTMsg, MQTTException
	};

	private ArrayList<MQTTSimpleTopic> _topics = new ArrayList<>();

	public static class InstanceBuilder {
		private final String _mqttServerURL;
		private final int _mqttServerPort;
		// Optional
		private boolean _cleanStart = true;
		private short _keepAlive = 1800;// s
		private String _clientID = "Test" + System.nanoTime();
		private LogService _logServer = new DefaultLogService();
		private MQTTSimpleTopic[] _topics = new MQTTSimpleTopic[] {};
		private MQTTMsgListner _msgListener;
		private MQTTBrokerStatusChangedListner _statusListner;
		private ScheduledExecutorService _guardThread = Executors.newScheduledThreadPool(1);

		public InstanceBuilder(String mqttServerURL, int mqttServerPort) {
			_mqttServerURL = mqttServerURL;
			_mqttServerPort = mqttServerPort;
		}

		public InstanceBuilder setCleanStart(boolean cleanStart) {
			_cleanStart = cleanStart;
			return this;
		}

		public InstanceBuilder setKeepAlive(short keepAlive) {
			_keepAlive = keepAlive;
			return this;
		}

		public InstanceBuilder setClientID(String clientID) {
			_clientID = clientID;
			return this;
		}

		public InstanceBuilder setLogService(LogService logServer) {
			_logServer = logServer;
			return this;
		}

		public InstanceBuilder setTopics(MQTTSimpleTopic[] topics) {
			_topics = topics;
			return this;
		}

		public InstanceBuilder setMsgListener(MQTTMsgListner listener) {
			_msgListener = listener;
			return this;
		}

		public InstanceBuilder setStatusListner(MQTTBrokerStatusChangedListner listner) {
			_statusListner = listner;
			return this;
		}

		public InstanceBuilder setGuardThread(ScheduledExecutorService guardTread) {
			_guardThread = guardTread;
			return this;
		}

		public MQTTBroker buildInstance() {
			return new MQTTBroker(this);
		}
	}

	private MQTTBroker(InstanceBuilder builder) {
		MQTT_SERVER_URL = builder._mqttServerURL;
		MQTT_SERVER_PORT = builder._mqttServerPort;
		CLEAN_START = builder._cleanStart;
		KEEP_ALIVE = builder._keepAlive;
		CLIENT_ID = builder._clientID;
		LOG = builder._logServer;
		MSGListner = builder._msgListener;
		STATUSListner = builder._statusListner;
		GuardThread = builder._guardThread;
		if (builder._topics.length != 0) {
			for (MQTTSimpleTopic topic : builder._topics) {
				_topics.add(topic);
			}
		}
	}

	/**
	 * 启动Broker.
	 */
	public void start() {
		GuardThread.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if ((_mqttClient == null) || (_connectStatus != MQTTBrokerStatus.Connected)) {
					tryToConnect();
				}
				// else {
				// //消息检测链路 判断是否需要
				// String checkMsg = "checkMsg";
				// sendMessage("mqttTest/ad", checkMsg);
				// _waitForCheckedMsg = false;
				// _waitForCheckedTimeOut = false;
				// Timer timer = new Timer("check_ser");
				// timer.schedule(new TimerTask() {
				// @Override
				// public void run() {
				// _waitForCheckedTimeOut = true;
				// }
				// }, 4*1000);
				// sendMessage("mqttTest/ad", checkMsg);
				// }
			}
		}, 0, 15, TimeUnit.SECONDS);
	}

	/**
	 * 尝试连接.
	 */
	public void tryToConnect() {
		try {
			if (_mqttClient == null) {
				_mqttClient = new MqttClient(MQTT_SERVER_URL + ":" + MQTT_SERVER_PORT);

				_mqttClient.registerAdvancedHandler(this);
				toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTLaucher, this));
				// toLog(this);
				// toLog("创建MQ客户端: clientID length is " + CLIENT_ID.length() +
				// ";clientID is " + CLIENT_ID);
			}
			// toLog("客户端尝试连接服务器...");
			toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTConnection, "客户端尝试连接服务器..."));

			_mqttClient.connect(CLIENT_ID, CLEAN_START, KEEP_ALIVE);

			if (_mqttClient.isConnected()) {
				toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTConnection, CLIENT_ID + ": MQTT连接成功"));
				// toLog(CLIENT_ID + ": MQTT连接成功");
				if (_connectStatus == MQTTBrokerStatus.Connecting) {
					if (STATUSListner != null) {
						STATUSListner.connected();
					}
				}
				_connectStatus = MQTTBrokerStatus.Connected;
				try {
					subscribeAllTopics();
				} catch (ZCSimpleException e) {
					toLog(e);
				}
			} else {
				_connectStatus = MQTTBrokerStatus.Unconnected;
				// toLog("MQTT连接失败");
				toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTConnection, "MQTT连接失败"));
			}
		} catch (MqttException e) {
			toLog(e);
		}
	}

	/**
	 * 订阅所有主题.
	 * 
	 * @throws ZCSimpleException
	 */
	public void subscribeAllTopics() throws ZCSimpleException {
		if (_connectStatus != MQTTBrokerStatus.Connected) {
			throw new ZCSimpleException("无法订阅主题，原因:未与服务器连接");
		} else {
			if (_topics.size() == 0) {
				throw new ZCSimpleException("无法订阅主题，原因:无主题可订阅");
			}
			Iterator<MQTTSimpleTopic> iter = _topics.iterator();
			String[] topicNames = new String[_topics.size()];
			int[] qos = new int[_topics.size()];
			int i = 0;
			while (iter.hasNext()) {
				MQTTSimpleTopic topic = iter.next();
				topicNames[i] = topic.getTopicName();
				qos[i] = topic.getQOS();
				i++;
			}
			try {
				_mqttClient.subscribe(topicNames, qos);
			} catch (IllegalArgumentException | MqttException e) {
				toLog(e);
			}

		}
	}

	/**
	 * 订阅单个主题
	 * 
	 * @param topic
	 */
	public void subscribeTopic(MQTTSimpleTopic topic) throws ZCSimpleException {
		if (_connectStatus != MQTTBrokerStatus.Connected) {
			throw new ZCSimpleException("无法订阅主题，原因:未与服务器连接");
		} else {
			try {
				_topics.add(topic);
				String topicName = topic.getTopicName();
				int qos = topic.getQOS();
				_mqttClient.subscribe(new String[] { topicName }, new int[] { qos });
			} catch (IllegalArgumentException | MqttException e) {
				toLog(e);
			}
		}
	}

	/**
	 * 发送消息 _mqttBroker.sendMessage("eewmsg", "123");
	 *
	 * @param topic
	 * @param messageId
	 */
	public void sendMessage(final MQTTSimpleTopic topic, final String message) {
		GuardThread.execute(new Runnable() {
			public void run() {
				try {
					if (_mqttClient.isConnected()) {

						byte[] binfo = new byte[0];
						try {
							binfo = message.getBytes("UTF-8");
						} catch (UnsupportedEncodingException e) {
							// throw new IllegalStateException("不支持UTF-8的编码格式",
							// ue);
							toLog(e);
						}
						toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg, "[Sending]" + " [" + topic + "]"
								+ " [Sender:" + CLIENT_ID + "] [Payload:" + message + "] : 消息字节长" + binfo.length));
						// toLog(new
						// LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg,
						// CLIENT_ID + " 发出消息字节长" + binfo.length + "的消息; At
						// topic:[" + topic + "]; 内容:" + message));
						// toLog(CLIENT_ID + " 发出消息字节长" + binfo.length + "的消息;
						// At topic:[" + topic + "]; 内容:" + message);
						// 发布自己的消息

						_mqttClient.publish(topic.getTopicName(), binfo, topic.getQOS(), false);
					}
				} catch (MqttException e) {
					toLog(e);
				}
			}
		});
	}

	public String getConnectionURL() {
		return MQTT_SERVER_URL + ":" + MQTT_SERVER_PORT;
	}

	public MQTTBrokerStatus getBrokerStatus() {
		return _connectStatus;
	}

	public String getClientID() {
		return CLIENT_ID;
	}

	@Override
	public void connectionLost() throws Exception {
		toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTConnection, CLIENT_ID + " connectionLost!!"));
		// toLog(CLIENT_ID + " connectionLost!!");
		if (STATUSListner != null) {
			STATUSListner.connectionLost();
		}
		_connectStatus = MQTTBrokerStatus.Connecting;
		// 尝试重连接
		tryToConnect();
	}

	@Override
	public void publishArrived(String topicName, byte[] payload, int Qos, boolean retained) throws Exception {
		// toLog(CLIENT_ID +" new msg arrived!! At topic:" + topicName + ";
		// payload:" + new String(payload));
		// toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg,
		// CLIENT_ID +" new msg arrived!! At topic:" + topicName + "; payload:"
		// + new String(payload)));
		toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg,
				"[NewMsg, " + topicName + ", To:" + CLIENT_ID + "]" + ": payload大小:" + payload.length));
		if (MSGListner != null) {
			MQTTSimpleMessage mqttMsg = new MQTTSimpleMessage(DateHelper.nowDate(), this, topicName, payload, Qos,
					retained);
			MSGListner.messageArrived(mqttMsg);
		}
	}

	@Override
	public void published(int arg0) {
		// toLog("published!!" + ";arg0:" + arg0);
		toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg, "published!!" + ";arg0:" + arg0));
	}

	@Override
	public void subscribed(int arg0, byte[] arg1) {
		// toLog("subscribed!!" + ";arg0:" + arg0 + ";arg1:" + arg1[0]);
		toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg,
				"subscribed!!" + ";arg0:" + arg0 + ";arg1:" + arg1[0]));
	}

	@Override
	public void unsubscribed(int arg0) {
		// toLog("unsubcribed!!"+ ";arg0:"+arg0);
		toLog(new LogSimpleObject<>(MQTTBrokerLogContentTag.MQTTMsg, "unsubcribed!!" + ";arg0:" + arg0));
	}

	@Override
	public int compareTo(MQTTBroker o) {
		return this.CLIENT_ID.compareTo(o.CLIENT_ID);
	}

	private <T> void toLog(T target) {
		if (target instanceof Exception) {
			LogSimpleObject<MQTTBrokerLogContentTag, Exception> lso = new LogSimpleObject<>(
					MQTTBrokerLogContentTag.MQTTException, (Exception) target);
			LOG.debugLog(lso);
			LOG.releaseLog(lso);
		} else {
			LOG.debugLog(target);
			LOG.releaseLog(target);
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + ", hashcode:" + hashCode() + ": [\n\t" + "MQTTBroker 对象生成:\n\t" + "MQTTServerURL:"
				+ MQTT_SERVER_URL + "\n\t" + "MQTTServerPort:" + MQTT_SERVER_PORT + "\n\t" + "CleanStart:"
				+ (CLEAN_START ? "true" : "false") + "\n\t" + "KeepAlive:" + KEEP_ALIVE + "\n\t" + "ClinetID:"
				+ CLIENT_ID + "\n\t" + "LOG:" + LOG + "\n\t" + "MsgListner:" + MSGListner + "\n\t" + "StatusListner:"
				+ STATUSListner + "\n\t" + "GuardThread:" + GuardThread;
	}

	// public static void main(String[] args) {
	// String mqttServerURL = "tcp://192.168.1.100";
	// int port = 16104;
	// MQTTSimpleTopic topic = new MQTTSimpleTopic("mqttTest/ad", 0);
	// MQTTSimpleTopic topic2 = new MQTTSimpleTopic("mqttTest/ad2", 0);
	// MQTTSimpleTopic topic3 = new MQTTSimpleTopic("mqttTest/#", 0);
	// MQTTSimpleTopic[] topics = new MQTTSimpleTopic[]{topic2, topic3};
	//// MQTTBroker broker = new MQTTBroker.InstanceBuilder(mqttServerURL,
	// port).setTopics(topics).setKeepAlive((short)10).setLogService(new
	// SimpleLog()).buildInstance();
	//
	//// broker.start();
	//
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	//// broker.sendMessage(topic, "Hello World");
	// }

}

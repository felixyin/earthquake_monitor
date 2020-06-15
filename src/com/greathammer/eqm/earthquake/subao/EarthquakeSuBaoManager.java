package com.greathammer.eqm.earthquake.subao;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.usmj.eewp.format.EQRMsgGeneralJsonFormat;
import com.greathammer.usmj.eewp.mqtt.MQTTBroker;
import com.greathammer.usmj.eewp.mqtt.MQTTMsgListner;
import com.greathammer.usmj.eewp.mqtt.MQTTSimpleMessage;
import com.greathammer.usmj.eewp.mqtt.MQTTSimpleTopic;
import com.greathammer.usmj.eewp.time.DateHelper;

/**
 * 本程序接收地震速报信息
 * 
 * @author FelixYin
 */
public class EarthquakeSuBaoManager implements MQTTMsgListner {

	private boolean debug = false;

	private final String ClientID = "EQRR" + DateHelper.idGeneratorNowTimeFormatTypeOne();
	private final static EarthquakeSuBaoManager SELF = new EarthquakeSuBaoManager();

	private Log log = LogFactory.getLog(EarthquakeSuBaoManager.class);

	private MQTTBroker _broker;
	private String _mqttServerURL;
	private int _mqttServerPort;
	private MQTTSimpleTopic _eqrTopic;

	{
		// MQTTBroker SETTING
		if (debug) {
			_mqttServerURL = "tcp://192.168.1.102";
			_mqttServerPort = 16104;
			_eqrTopic = new MQTTSimpleTopic("fjea/eewproject/eqrmsg", 0);
		} else {
			_mqttServerURL = "tcp://218.5.2.74";
			_mqttServerPort = 1884;
			_eqrTopic = new MQTTSimpleTopic("fjea/eewproject/eqrmsg", 0);
		}
	}

	public static void main(String[] args) {
		SELF.start();
	}

	private void start() {
		_broker = new MQTTBroker.InstanceBuilder(_mqttServerURL, _mqttServerPort).setClientID(ClientID)
				.setMsgListener(SELF).setTopics(new MQTTSimpleTopic[] { _eqrTopic }).buildInstance();
		_broker.start();
	}

	@Override
	public void messageArrived(MQTTSimpleMessage mqttMsg) {
		if (mqttMsg.getTopicName().equals(_eqrTopic.getTopicName())) {
			String content = null;
			try {
				content = new String(mqttMsg.getPayload(), "UTF-8");
				log.debug(content);
				EQRMsgGeneralJsonFormat egjf = new EQRMsgGeneralJsonFormat(content);
				log.debug(egjf.toString());
			} catch (UnsupportedEncodingException | ParseException e) {
				log.error(e);
			}
		}
	}

}

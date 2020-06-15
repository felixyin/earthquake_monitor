package com.greathammer.eqm.earthquake.yujing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.util.Intensity;
import com.greathammer.usmj.eewp.mqtt.MQTTBroker;
import com.greathammer.usmj.eewp.mqtt.MQTTMsgListner;
import com.greathammer.usmj.eewp.mqtt.MQTTSimpleMessage;
import com.greathammer.usmj.eewp.mqtt.MQTTSimpleTopic;
import com.greathammer.usmj.eewp.time.DateHelper;

/**
 * 本程序接收地震预警信息
 *
 * @author FelixYin
 */
public class EarthquakeYuJingManagerForProduction implements MQTTMsgListner, IEarthquakeYuJingManager {

	private final String ClientID = "EQRR" + DateHelper.idGeneratorNowTimeFormatTypeOne();
	private final static EarthquakeYuJingManagerForProduction SELF = new EarthquakeYuJingManagerForProduction();

	private Log log = LogFactory.getLog(EarthquakeYuJingManagerForProduction.class);

	private MQTTBroker _broker;
	private String _mqttServerURL;
	private int _mqttServerPort;
	private MQTTSimpleTopic _eqrTopic;

	{
		_mqttServerURL = Constant.EQMQ_TCP_FUJIAN;
		_mqttServerPort = Constant.EQMQ_PORT_FUJIAN;
		_eqrTopic = new MQTTSimpleTopic(Constant.EQMQ_TOPIC, 0);
	}

	private static EarthquakeYuJingListener listener;

	public EarthquakeYuJingManagerForProduction() {
	}

	public EarthquakeYuJingManagerForProduction(EarthquakeYuJingListener listener) {
		EarthquakeYuJingManagerForProduction.listener = listener;
	}

	public EarthquakeYuJingManagerForProduction setListener(EarthquakeYuJingListener listener) {
		EarthquakeYuJingManagerForProduction.listener = listener;
		return this;
	}

	public static void main(String[] args) {
		SELF.start();
	}

	@Override
	public void start() {
		// Constant.setRealEarthquake(true);

		_broker = new MQTTBroker.InstanceBuilder(_mqttServerURL, _mqttServerPort).setClientID(ClientID)
				.setMsgListener(SELF).setTopics(new MQTTSimpleTopic[] { _eqrTopic }).buildInstance();
		_broker.start();
	}

	/**
	 * "{\"1\":\"20160206035727_1\",\n" + "\"2\":\"2016-11-15 11:49:30\",\n" + "
	 * \"3\":\"120.37\",\n" + " \"4\":\"22.86\",\n" + "\"5\":\"台湾高雄县\",\n" + "
	 * \"6\":\"5.8\",\n" + " \"7\":\"03:58:30\"}"
	 */
	@Override
	public void start(String eqData) {
		// Constant.setRealEarthquake(false);

		_broker = new MQTTBroker.InstanceBuilder(_mqttServerURL, _mqttServerPort).setClientID(ClientID)
				.setMsgListener(SELF).setTopics(new MQTTSimpleTopic[] { _eqrTopic }).buildInstance();

		// 解决可能存在的中文乱码问题
		byte[] bytes = eqData.getBytes(Charset.forName("UTF-8"));

		String dateStr = "2016-11-22 17:40:16";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MQTTSimpleMessage mqtt;
		try {
			mqtt = new MQTTSimpleMessage(sdf.parse(dateStr), _broker, _eqrTopic.getTopicName(), bytes, bytes.length,
					true);
			this.messageArrived(mqtt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(MQTTSimpleMessage mqttMsg) {
		if (mqttMsg.getTopicName().equals(_eqrTopic.getTopicName())) {
			log.info("-------------------------------接收到地震数据,当前时间是：" + new Date().getTime());
			String content = null;
			try {
				content = new String(mqttMsg.getPayload(), "UTF-8");
				log.debug(content);

				// 原有 json 转 java bean，已废弃
				// EQRMsgGeneralJsonFormat egjf = new
				// EQRMsgGeneralJsonFormat(content);
				// LOG.debugLog(new LogSimpleObject<>("ParseToEQRObject",
				// egjf));

				// json 转 java bean
				EarthquakeYuJing event = new ObjectMapper().readValue(content, EarthquakeYuJing.class);

				Intensity intensityUtil = new Intensity(content);

				// 计算烈度
				int intensity = intensityUtil.getIntensity();
				event.setIntensity(intensity);

				// 计算距离
				double distance = intensityUtil.getDistance();
				event.setDistance(Math.round(distance) + "");

				// 计算地震倒计时
				calculateArrivalTime(event, distance, intensityUtil.getCountDownS());

				// 计算避震建议
				calculateAdivice(event, intensity);

				// 计算报警灯
				calculateLight(event, intensity);

				EarthquakeYuJingManagerForProduction.listener.doEvent(event, content);

			} catch (JsonParseException ex) {
				log.error(ex);
			} catch (JsonMappingException ex) {
				log.error(ex);
			} catch (ParseException ex) {
				log.error(ex);
			} catch (UnsupportedEncodingException ex) {
				log.error(ex);
			} catch (IOException ex) {
				log.error(ex);
			}
		}
	}

	private void calculateArrivalTime(EarthquakeYuJing event, double distance, int coutdownS) throws ParseException {
		// String otime = event.getOtime();
		// SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		// long startTime = sdf1.parse(otime).getTime();
		// long endTime = new Date().getTime();
		// long minusTime = endTime - startTime;
		// int minusSecond = (int) (minusTime / 1000000);

		// int calTime = (int) (distance / 3.5);
		int resultTime = coutdownS;
		event.setArrivalTime(resultTime > 0 ? resultTime : 0);
	}

	/**
	 * 当地震烈度小于4时蓝色灯闪烁亮，4<=烈度<6时黄色灯闪烁亮，6<=烈度<8时橙色灯闪烁亮；大于等于8时红色灯闪烁亮
	 * 
	 * @param event
	 * @param intensity
	 */
	private void calculateLight(EarthquakeYuJing event, int intensity) {
		if (intensity < 4) {
			event.setLight("blue");
		} else if (intensity >= 4 && intensity < 6) {
			event.setLight("yellow");
		} else if (intensity >= 6 && intensity < 8) {
			event.setLight("orange");
		} else if (intensity >= 8) {
			event.setLight("red");
		}
	}

	private void calculateAdivice(EarthquakeYuJing event, int intensity) {
		if (intensity <= 1 || intensity == 2) {
			event.setEqDesc(EQDESC_1_2);
			event.setAdvice(ADVICE_1_2);
		} else if (intensity == 3 || intensity == 4) {
			event.setEqDesc(EQDESC_3_4);
			event.setAdvice(ADVICE_3_4);
		} else if (intensity == 5 || intensity == 6 || intensity == 7) {
			event.setEqDesc(EQDESC_5_6_7);
			event.setAdvice(ADVICE_5_6_7);
		} else if (intensity == 8) {
			event.setEqDesc(EQDESC_8);
			event.setAdvice(ADVICE_8);
		} else { // >=9的情况，已非常非常危险了
			event.setEqDesc(EQDESC_9_);
			event.setAdvice(ADVICE_9_);
		}
	}

	@Override
	public void start(int i) {
		// Constant.setRealEarthquake(false);

	}

}

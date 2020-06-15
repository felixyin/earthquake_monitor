package com.greathammer.usmj.intensity.earthquake.event;

import java.text.ParseException;

import com.greathammer.usmj.intensity.earthquake.SimpleEarthquakeCenter;
import com.greathammer.usmj.intensity.earthquake.intensity.InstrumentalIntensity;
import com.greathammer.usmj.intensity.earthquake.intensity.InstrumentalIntensity.InstrumentalIntensityStandard;
import com.greathammer.usmj.intensity.format.EEWMsgGeneralJsonFormat;

/**
 * 预警事件类.
 * 
 * @author Zhou Yueyong
 * @author Zheng Chao
 * @version 0.2 2016.10.25
 */
public class EEWEvent {

	final private SimpleEarthquakeCenter EARTHQUAKE_CENTER;
	final private String EVENT_ID;
	final private int SerialNO;

	final private double DUANCENG_DISTANCE;// km

	// 参数P
	// 1 - lnPGA = A + B*ln(R+R0) + C*R + D*M; (R-半径;M-震级)
	final private double P1_A = -0.044824;
	final private double P1_B = -1.6896;
	final private double P1_C = -0.00348;
	final private double P1_D = 1.7722;
	final private double P1_R0 = 10;// km
	// 2 - lnPGA = A + B*M + (C + D*M)*ln(R + R0);(R-半径;M-震级)
	final private double P2_A = 1.6683;
	final private double P2_B = 1.4315;
	final private double P2_C = -1.7457;
	final private double P2_D = 0.0289;
	final private double P2_R0 = 10;// km
	// 3 - lgPGA = A + B*lg(R + R0 + C) + D*M;(R-半径;M-震级)
	final private double P3_A = 1.71;
	final private double P3_B = -2.18;
	final private double P3_C = 15;
	final private double P3_D = 0.657;
	final private double P3_R0 = 10;// km

	// 断层
	final private double PD_A = 0.62;
	final private double PD_B = -2.57;

	final private double Ms_MIN = 4.15;
	final private double Ms_MAX = 9.5;

	public EEWEvent(EEWMsgGeneralJsonFormat eewmsg) {
		EARTHQUAKE_CENTER = new SimpleEarthquakeCenter.InstanceBuilder(eewmsg.getLongitude(), eewmsg.getLatitude())
				.setHappenDate(eewmsg.getEventDate()).setMagnitude(eewmsg.getMagnitude())
				.setPlaceName(eewmsg.getPlaceName()).buildInstance();
		EVENT_ID = eewmsg.getEventID();
		SerialNO = eewmsg.getEventSerialNO();
		double mag = eewmsg.getMagnitude();
		if (mag > Ms_MIN && mag <= Ms_MAX) {
			DUANCENG_DISTANCE = Math.pow(10, PD_A * mag + PD_B);
		} else {
			DUANCENG_DISTANCE = 1;
		}
	}

	public SimpleEarthquakeCenter getEarthquakeCenter() {
		return EARTHQUAKE_CENTER;
	}

	public double getFaultLength() {
		return DUANCENG_DISTANCE;
	}

	public String getEventID() {
		return EVENT_ID;
	}

	public int getSerialNO() {
		return SerialNO;
	}

	public double calPredictPGAWithDistance(double distanceFromCenter_km) {
		if (distanceFromCenter_km > DUANCENG_DISTANCE / 2) {
			distanceFromCenter_km = distanceFromCenter_km - DUANCENG_DISTANCE / 2;
		} else {
			distanceFromCenter_km = 0;
		}

		double pga;
		double mag = EARTHQUAKE_CENTER.getMagnitude();

		if (mag <= 4.0) {
			pga = Math.exp(
					P1_A + P1_B * Math.log(distanceFromCenter_km + P1_R0) + P1_C * distanceFromCenter_km + P1_D * mag);
		} else if (mag <= 6.5) {
			pga = Math.exp(P2_A + P2_B * mag + (P2_C + P2_D * mag) * Math.log(distanceFromCenter_km + P2_R0));
		} else if (mag <= 9) {
			pga = Math.pow(10, P3_A + P3_B * Math.log10(distanceFromCenter_km + P3_R0 + P3_C) + P3_D * mag);
		} else {
			// 原代码：pga=2000
			pga = Math.pow(10, P3_A + P3_B * Math.log10(distanceFromCenter_km + P3_R0 + P3_C) + P3_D * mag);
		}

		if (pga >= 2000)
			pga = 2000;
		return pga;
	}

	public static void main(String[] args) {
		// 台湾20160206035727
		// SimpleEarthquakeCenter ec = new
		// SimpleEarthquakeCenter.InstanceBuilder(120.37,
		// 22.86).setMagnitude(5.8).setIntensity(8).buildInstance();
		// EEWEvent event = new EEWEvent(ec, "1000" , 0);
		// System.out.println("affect radius" + event.AFFECTED_RADIUS);
		// for(double a : event.getIntensityRadius()) {
		// System.out.println(a);
		// }

		// jsonString内容已被修改..
		String jsonString = "{\"1\":\"20160706103126_3\",\"2\":\"2016-07-06 21:59:37\",\"3\":\"120.47\",\"4\":\"26.74\",\"5\":\"霞浦县附近海域\",\"6\":\"6.5\",\"7\":\"22:50:26\"}";
		try {
			EEWMsgGeneralJsonFormat test = new EEWMsgGeneralJsonFormat(jsonString);
			System.out.println(test);
			EEWEvent event = new EEWEvent(test);
			// NormalCoordinate pointOne = new NormalCoordinate(118.5758,
			// 24.8158);
			// NormalCoordinate pointTwo = new
			// NormalCoordinate(event.getEarthquakeCenter().getCenterCoordinate());
			// double distance = MapDistance.getDistance(pointOne, pointTwo);
			// System.out.println("distance is " + distance);
			double distance = 286.233;
			System.out.println("预测晋江地区烈度为:" + InstrumentalIntensity.getIntensity(InstrumentalIntensityStandard.CHNPGA,
					event.calPredictPGAWithDistance(distance)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

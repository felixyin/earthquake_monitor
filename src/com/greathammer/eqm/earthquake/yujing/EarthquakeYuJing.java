/*
 * 


 */
package com.greathammer.eqm.earthquake.yujing;
//地震速报消息MQTT接收方式与数据格式如下：

//topic: fjea/eewproject/eqrmsg

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * 预警消息（含事件ID）： mqtt topic：fjea/eewproject/eewmsg payload: 1-事件ID_第x报 2-发震时刻
 * 3-经度 4-纬度 5-地名 6-震级 7-消息发送时刻
 *
 * 例子：{"1":"20160206035727_1", "2":"2016-02-06 03:57:25", "3":"120.37",
 * "4":"22.86", "5":"台湾高雄县", "6":"5.8", "7":"03:58:30"}
 *
 * @author FelixYin
 */
public class EarthquakeYuJing {

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
	// 事件ID_第x报
	@JsonProperty("1")
	private String eventID;
	// 发震时刻
	@JsonProperty("2")
	private String otime = "0";
	// 经度
	@JsonProperty("3")
	private double longitude = 0;
	// 纬度
	@JsonProperty("4")
	private double latitude = 0;
	// 地名
	@JsonProperty("5")
	private String location = "0";
	// 震级
	@JsonProperty("6")
	private double magitude = 0;
	// 消息发送时刻
	@JsonProperty("7")
	private String eventDate = "0";

	// ------------------------------------------------
	// 建议
	private String advice = "0";
	// 预计到达时间
	private int arrivalTime = 0;
	// 震中距离
	private String distance = "0";
	// 地震描述
	private String eqDesc = "0";
	// 预估烈度
	private int intensity = 0;
	// 灯
	private String light = "";

	public EarthquakeYuJing(String eventID, String otime, double longitude, double latitude, String location,
			double magitude, String eventDate, String advice, int arrivalTime, String distance, String eqDesc,
			int intensity, String light) {
		super();
		this.eventID = eventID;
		this.otime = otime;
		this.longitude = longitude;
		this.latitude = latitude;
		this.location = location;
		this.magitude = magitude;
		this.eventDate = eventDate;
		this.advice = advice;
		this.arrivalTime = arrivalTime;
		this.distance = distance;
		this.eqDesc = eqDesc;
		this.intensity = intensity;
		this.light = light;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		String old = this.eventID;
		this.eventID = eventID;
		changeSupport.firePropertyChange("eventID", old, eventID);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		String old = this.location;
		this.location = location;
		changeSupport.firePropertyChange("location", old, location);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		double old = this.latitude;
		this.latitude = latitude;
		changeSupport.firePropertyChange("latitude", old, latitude);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		double old = this.longitude;
		this.longitude = longitude;
		changeSupport.firePropertyChange("longitude", old, longitude);
	}

	public double getMagitude() {
		return magitude;
	}

	public void setMagitude(double magitude) {
		double old = this.magitude;
		this.magitude = magitude;
		changeSupport.firePropertyChange("magitude", old, magitude);
	}

	public String getOtime() {
		return otime;
	}

	public void setOtime(String otime) {
		String old = this.otime;
		this.otime = otime;
		changeSupport.firePropertyChange("otime", old, otime);
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		String old = this.advice;
		this.advice = advice;
		changeSupport.firePropertyChange("advice", old, advice);
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(final int arrivalTimeNew) {
		int old = this.arrivalTime;
		this.arrivalTime = arrivalTimeNew;
		changeSupport.firePropertyChange("arrivalTime", old, arrivalTime);

	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		String old = this.distance;
		this.distance = distance;
		changeSupport.firePropertyChange("distance", old, distance);
	}

	public String getEqDesc() {
		return eqDesc;
	}

	public void setEqDesc(String eqDesc) {
		String old = this.eqDesc;
		this.eqDesc = eqDesc;
		changeSupport.firePropertyChange("eqDesc", old, eqDesc);
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		int old = this.intensity;
		this.intensity = intensity;
		changeSupport.firePropertyChange("intensity", old, intensity);
	}

	public String getLight() {
		return light;
	}

	public void setLight(String light) {
		String old = this.light;
		this.light = light;
		changeSupport.firePropertyChange("light", old, light);
	}

	public EarthquakeYuJing() {
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void change(EarthquakeYuJing e) {
		// 预警消息7个字段之内的
		this.setEventID(e.getEventID());
		this.setOtime(e.getOtime());
		this.setLongitude(e.getLongitude());
		this.setLatitude(e.getLatitude());
		this.setLocation(e.getLocation());
		this.setMagitude(e.getMagitude());

		// 界面上需要，7个之外的
		this.setAdvice(e.getAdvice());
		this.setArrivalTime(e.getArrivalTime());
		this.setDistance(e.getDistance());
		this.setEqDesc(e.getEqDesc());
		this.setIntensity(e.getIntensity());
		this.setLight(e.getLight());
	}

	@Override
	public String toString() {
		return "EarthquakeYuJing [changeSupport=" + changeSupport + ", eventID=" + eventID + ", otime=" + otime
				+ ", longitude=" + longitude + ", latitude=" + latitude + ", location=" + location + ", magitude="
				+ magitude + ", eventDate=" + eventDate + ", advice=" + advice + ", arrivalTime=" + arrivalTime
				+ ", distance=" + distance + ", eqDesc=" + eqDesc + ", intensity=" + intensity + ", light=" + light
				+ "]";
	}

	public void resetField() {
		this.setAdvice("0");
		this.setArrivalTime(0);
		this.setDistance("0");
		this.setEqDesc("0");
		this.setEventID("0");
		this.setIntensity(0);
		this.setLatitude(0L);
		this.setLight("");
		this.setLocation("0");
		this.setLongitude(0L);
		this.setMagitude(0L);
		this.setOtime("0");
	}

}

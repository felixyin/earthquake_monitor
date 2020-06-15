/*
 * 


 */
package com.greathammer.eqm.weather;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Michel
 */
public class WeatherMessage {

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	// 城市
	private String city;
	// 天气
	private String status;
	// 温度
	private String temperature;
	// 风向
	private String direction;
	// 风力
	private String power;
	// 空气质量
	private String pollution;
	// 当前温度
	private String currentTemp;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		String old = this.city;
		this.city = city;
		changeSupport.firePropertyChange("city", old, city);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		String old = this.status;
		this.status = status;
		changeSupport.firePropertyChange("status", old, status);
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		String old = this.temperature;
		this.temperature = temperature;
		changeSupport.firePropertyChange("temperature", old, temperature);
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		String old = this.direction;
		this.direction = direction;
		changeSupport.firePropertyChange("direction", old, direction);
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		String old = this.power;
		power = power.replace("<![CDATA[", "");
		power = power.replace("]]>", "");
		this.power = power;
		changeSupport.firePropertyChange("power", old, power);
	}

	public String getPollution() {
		return pollution;
	}

	public void setPollution(String pollution) {
		String old = this.pollution;
		this.pollution = pollution;
		changeSupport.firePropertyChange("pollution", old, pollution);
	}

	public String getCurrentTemp() {
		return currentTemp;
	}

	public void setCurrentTemp(String currentTemp) {
		String old = this.currentTemp;
		this.currentTemp = currentTemp;
		changeSupport.firePropertyChange("currentTemp", old, currentTemp);
	}

	public WeatherMessage() {
	}

	public WeatherMessage(String city, String status, String temperature, String direction, String power,
			String pollution, String currentTemp) {
		this.city = city;
		this.status = status;
		this.temperature = temperature;
		this.direction = direction;
		this.power = power;
		this.pollution = pollution;
		this.currentTemp = currentTemp;
	}

	@Override
	public String toString() {
		return "WeatherMessage{" + "changeSupport=" + changeSupport + ", city=" + city + ", status=" + status
				+ ", temperature=" + temperature + ", direction=" + direction + ", power=" + power + ", pollution="
				+ pollution + ",currentTemp =" + currentTemp + "}";
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void change(WeatherMessage w) {
		this.setCity(w.getCity());
		this.setCurrentTemp(w.getCurrentTemp());
		this.setDirection(w.getDirection());
		this.setPollution(w.getPollution());
		this.setPower(w.getPower());
		this.setStatus(w.getStatus());
		this.setTemperature(w.getTemperature());
	}
}

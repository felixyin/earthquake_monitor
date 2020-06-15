/*
 * 


 */
package com.greathammer.eqm.weather;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Michel
 */
public class WeatherMessageManager {

	private WeatherMessage message;
	private WeatherMessageListener listener;

	public WeatherMessageManager addListener(String city, WeatherMessageListener listener) {
		// CommonsWeatherUtils utils = new CommonsWeatherUtils();

		this.message = WeatherUtil.getWeather(city);
		this.listener = listener;
		return this;
	}

	public void start() {
		listener.doEvent(message);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if (hour == 6 || hour == 9 || hour == 12 || hour == 15 || hour == 18 || hour == 21) {
					listener.doEvent(message);
				}
			}

		}, 2000, 1000 * 60 * 60 * 1); // 每小时执行一次
	}
}

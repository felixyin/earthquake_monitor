/*
 * 


 */
package com.greathammer.eqm.date;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Michel
 */
public class DateMessageManager {

	private DateMessage message;

	private DateMessageListener listener;

	public DateMessageManager addListener(DateMessageListener listener) {
		this.listener = listener;
		return this;
	}

	public void start() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ChinaDate chinaDate = new ChinaDate();
				message = chinaDate.getLunar();
				listener.doEvent(message);
			}
		}, 1000, 1000);
	}
}

/*
 * 


 */
package com.greathammer.eqm.date;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Michel
 */
public class DateMessage {

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	// 时间
	private String time;
	// 年
	private String date;
	// 星期
	private String week;
	// 农历年
	private String lunarYear;
	// 生肖年
	private String animalYear;
	// 农历月
	private String lunarMonth;
	// 农历日
	private String lunarDay;
	// 农历月日
	private String nongli;
	// 上午下午
	private String upDown;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		String old = this.time;
		this.time = time;
		int substring = Integer.parseInt(time.substring(0, 2));
		changeSupport.firePropertyChange("time", old, time);
		if (substring > 11) {
			setUpDown("下午");
		} else {
			setUpDown("上午");
		}
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		String old = this.date;
		this.date = date;
		changeSupport.firePropertyChange("date", old, date);
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		String old = this.week;
		this.week = week;
		changeSupport.firePropertyChange("week", old, week);
	}

	public String getLunarYear() {
		return lunarYear;
	}

	public void setLunarYear(String lunarYear) {
		String old = this.lunarYear;
		this.lunarYear = lunarYear;
		changeSupport.firePropertyChange("lunarYear", old, lunarYear);
	}

	public String getAnimalYear() {
		return animalYear;
	}

	public void setAnimalYear(String animalYear) {
		String old = this.animalYear;
		this.animalYear = animalYear;
		changeSupport.firePropertyChange("animalYear", old, animalYear);
	}

	public String getLunarMonth() {
		return lunarMonth;
	}

	public void setLunarMonth(String lunarMonth) {
		if (null != lunarMonth && !lunarMonth.contains("月")) { // 正月等特殊月份，没有月字，所以补全
			lunarMonth += "月";
		}
		String old = this.lunarMonth;
		this.lunarMonth = lunarMonth;
		changeSupport.firePropertyChange("lunarMonth", old, lunarMonth);
	}

	public String getLunarDay() {
		return lunarDay;
	}

	public void setLunarDay(String lunarDay) {
		String old = this.lunarDay;
		this.lunarDay = lunarDay;
		changeSupport.firePropertyChange("lunarDay", old, lunarDay);
	}

	public String getNongli() {
		return nongli;
	}

	public void setNongli(String nongli) {
		String old = this.nongli;
		this.nongli = nongli;
		changeSupport.firePropertyChange("nongli", old, nongli);
	}

	public String getUpDown() {
		return upDown;
	}

	public void setUpDown(String upDown) {
		String old = this.upDown;
		this.upDown = upDown;
		changeSupport.firePropertyChange("upDown", old, upDown);
	}

	public DateMessage() {
	}

	public DateMessage(String time, String date, String week, String lunarYear, String animalYear, String lunarMonth,
			String lunarDay, String nongli, String upDown) {
		this.time = time;
		this.date = date;
		this.week = week;
		this.lunarYear = lunarYear;
		this.animalYear = animalYear;
		this.lunarMonth = lunarMonth;
		this.lunarDay = lunarDay;
		this.nongli = nongli;
		this.upDown = upDown;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	@Override
	public String toString() {
		return "DateMessage{" + "changeSupport=" + changeSupport + ", time=" + time + ", date=" + date + ", week="
				+ week + ", lunarYear=" + lunarYear + ", animalYear=" + animalYear + ", lunarMonth=" + lunarMonth
				+ ", lunarDay=" + lunarDay + ", nongli=" + nongli + ",upDown=" + upDown + "}";
	}

	public void change(DateMessage e) {
		this.setAnimalYear(e.getAnimalYear());
		this.setDate(e.getDate());
		this.setLunarDay(e.getLunarDay());
		this.setLunarMonth(e.getLunarMonth());
		this.setLunarYear(e.getLunarYear());
		this.setNongli(e.getNongli());
		this.setTime(e.getTime());
		this.setUpDown(e.getUpDown());
		this.setWeek(e.getWeek());
	}
}

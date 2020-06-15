package com.greathammer.usmj.eewp.format;

/**
 * 预警等级. <blockquote>
 * 
 * <pre>
 * <br>
 * 预测烈度[3,4) 蓝色 <br>
 * 预测烈度[4,6) 黄色 <br>
 * 预测烈度[6,8) 橙色 <br>
 * 预测烈度[8,) 红色
 * 
 * @author Zheng Chao
 * @since 2016.07.13
 */
public enum EEWAlarmLevel {

	BlueWarning(1), YellowWarning(2), OrangeWarning(3), RedWarning(4);

	private int _statusCode;

	private EEWAlarmLevel(int d) {
		setStatusCode(d);
	}

	private void setStatusCode(int statusCode) {
		_statusCode = statusCode;
	}

	public int getStatusCode() {
		return _statusCode;
	}

	public static EEWAlarmLevel getLevelWithCode(int levelCode) {
		switch (levelCode) {
		case 1:
			return EEWAlarmLevel.BlueWarning;
		case 2:
			return EEWAlarmLevel.YellowWarning;
		case 3:
			return EEWAlarmLevel.OrangeWarning;
		case 4:
			return EEWAlarmLevel.RedWarning;
		default:
			return null;
		}
	}

}

package com.greathammer.eqm.earthquake.yujing;

/**
 * 
 * @author FelixYin
 */
public interface IEarthquakeYuJingManager {

	String DATE_FORMAT_PATTERN = "yyyy-MM-dd hh:mm:ss";

	String ADVICE_9_ = "剧烈震动，飞速撤离";
	String EQDESC_9_ = "非常危险";

	String ADVICE_8 = "剧烈震动，飞速撤离";
	String EQDESC_8 = "摇晃颠簸";

	String ADVICE_5_6_7 = "强烈震动，立即撤离";
	String EQDESC_5_6_7 = "有感,请适当避震";

	String ADVICE_3_4 = "较强震动，加强防护";
	String EQDESC_3_4 = "微有感";

	String ADVICE_1_2 = "轻微震动，注意防范";
	String EQDESC_1_2 = "";

	IEarthquakeYuJingManager setListener(EarthquakeYuJingListener listener);

	void start();

	void start(String eqData);

	void start(int i);

}
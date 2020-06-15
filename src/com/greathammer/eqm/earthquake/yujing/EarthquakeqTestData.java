/*
 * 


 */
package com.greathammer.eqm.earthquake.yujing;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FelixYin
 */
public class EarthquakeqTestData {

	private List<EarthquakeYuJing> list = new ArrayList<EarthquakeYuJing>();

	public List<EarthquakeYuJing> getList() {
		return list;
	}

	public EarthquakeqTestData() {
		EarthquakeYuJing eMessage = null;
		// eMessage = new EarthquakeYuJing("20161019082906_2", "2016-10-19
		// 08:29:02", 122.08, 23.86,
		// "台湾花莲县附近海域", 8, "08:30:08", "请适当避震", 15, "1440", "微弱地震", 2, "blue");
		// this.list.add(eMessage);

		eMessage = new EarthquakeYuJing("20161019082922_3", "2016-11-07 08:29:03", 41.46, 74.89, "新疆喀什", 8, "09:25:45",
				"剧烈震动，飞速撤离", 16, "3872", "巨大地震", 8, "red");
		this.list.add(eMessage);

		eMessage = new EarthquakeYuJing("20161019082902_3", "2016-10-29 08:29:04", 79.46, 74.89, "新疆喀什", 7, "08:25:45",
				"强烈震动，立即撤离", 15, "3872", "强大地震", 6, "orange");
		this.list.add(eMessage);

		eMessage = new EarthquakeYuJing("20161019082906_3", "2016-10-29 08:29:05", 3.46, 74.89, "新疆喀什", 5, "11:25:45",
				"较强震动，加强防护", 14, "3872", "中度地震", 5, "yellow");
		this.list.add(eMessage);

		eMessage = new EarthquakeYuJing("20161019082924_3", "2016-10-29 08:29:06", 56.46, 74.89, "新疆喀什", 4, "09:25:45",
				"轻微震动，注意防范", 13, "3872", "微弱地震", 3, "blue");
		this.list.add(eMessage);
	}

}

/*
 * 


 */
package com.greathammer.eqm.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author FelixYin
 */
public class Constant {

	private static Log log = LogFactory.getLog(Constant.class);

	public static String SAVE_SETTING = 
			"http://139.224.1.36:8080/deployPoint/saveSetting";
	
	private static boolean EARTHQUAKING = false;

	public static boolean isEARTHQUAKING() {
		return EARTHQUAKING;
	}

	public static void setEARTHQUAKING(boolean eARTHQUAKING) {
		EARTHQUAKING = eARTHQUAKING;
	}

	public static String C_WINDOWS_EQM_PROPERTIES = "C:\\earthquake_monitor\\eqm.properties";

	// 演示版本与正式版本之间的切换,1表示正式版，0表示演示版
	public static int IS_PRODUCTION_VERSION = 1;

	public static boolean isTestMode() {
		return IS_PRODUCTION_VERSION == 0;
	}

	public static boolean isProductionMode() {
		return IS_PRODUCTION_VERSION == 1;
	}

	public static void setTestMode() {
		IS_PRODUCTION_VERSION = 0;
	}

	public static void setProductionMode() {
		IS_PRODUCTION_VERSION = 1;
	}

	// 系统部署所在地，城市名称
	public static String CITY_NAME = "福州";

	// 系统部署所在地的经度
	public static double LONGITUDE = 120;

	// 系统部署所在地的维度
	public static double LATITUDE = 36;

	// 烈度阈值超过多少会播放报警音频
	public static int AUDIO_WAV_INTENSITY = 1;

	// 地震报警音频播放次数
	public static int AUDIO_WAV_COUNT = 3;

	// 是否播放倒计时音效，1表示播放，0表示不播放
	public static int IS_PLAY_COUNT_DOWN = 1;

	// 是否显示报警灯光
	public static int IS_SHOW_LIGHT = 1;

	// 是否播放报警声音
	public static int IS_PLAY_AUDIO = 1;

	// 状态栏地震名称
	public static String COMPANY_NAME = "青岛地震局";

	// 模拟的地震，启动应用后第一次地震来的时间
	public static long TEST_EARTHQUAKE_FIRST_TIME = 1000 * 10;

	// 模拟的地震，每隔15分钟来一次(针对演示版本有效)
	public static long TEST_EARTHQUAKE_TIME = 1000 * 60 * 15;

	// 多少时间过后没有收到地震预警则自动切换到屏保界面
	public static long SWITCH_SCREENSAVER_TIME_AFTER_EQM = 1000 * 60 * 10;

	// 欢迎屏幕的显示时间
	public static int HELLO_SCREEN_SHOW_TIME = 1000 * 3;

	// 地震主屏幕显示完毕后，等待多少时间显示屏保
	public static int SCREENSAVER_SHOW_AFTER_EQM = 1000 * 2;

	// 福建地震局接口地址
	public static String EQMQ_TCP_FUJIAN = "tcp://218.5.2.74";

	// 福建地震局接口端口
	public static int EQMQ_PORT_FUJIAN = 1884;
	
	// 福建地震局接口订阅的主题（发布的频道名称）
	public static String EQMQ_TOPIC = "fjea/eewproject/eewmsg";

	// 福建地震局接口订阅的主题（发布的频道名称）
	public static String EQMQ_TOPIC_FUJIAN = "fjea/eewproject/eewmsg";
	// public static String EQMQ_TOPIC_FUJIAN = "eewZGQ";

	// 对于演示版本，是否需要一直循环产生地震，默认为false
	public static boolean IS_LOOP_FOR_TEST = false;

	// 串口端口
	// /dev/tty.usbserial
	public static String COM = "COM1";
	
	// 订阅的测试接口的主题
	public static String EQMQ_TOPIC_FUJIAN_TEST ="eewZGQ";
	
	// 是否是对接地震局台网的生产接口1是，0不是（0是测试）
	public static int IS_TOPIC_PRODUCTION_MODEL = 1;
	

	// 真正地震发生时，切换屏保时间设置，单位：分钟
	// public static int EQMQ_AFTER_PRODUCTION_TIME = 1000 * 60 * 60;

	/*
	 * 是否是正在配置设置的过程中
	 */
	public static boolean isSettingIng = false;

	public static boolean isCanShow() {
		return !isSettingIng;
	}

	public static void cantShow() {
		isSettingIng = true;
	}

	public static void initConstant() {
		log.info("从配置文件初始化配置数据到java对象：");

		Properties p = new Properties();
		try {
			log.info("开始加载配置文件");
			p.load(new InputStreamReader(new FileInputStream(C_WINDOWS_EQM_PROPERTIES), "UTF-8"));
		} catch (FileNotFoundException e) {
			log.error(e);
			System.exit(1);
		} catch (IOException e) {
			log.error(e);
			System.exit(1);
		}

		IS_PRODUCTION_VERSION = Integer.parseInt(p.getProperty("IS_PRODUCTION_VERSION"));
		CITY_NAME = p.getProperty("CITY_NAME");
		LONGITUDE = Double.parseDouble(p.getProperty("LONGITUDE"));
		LATITUDE = Double.parseDouble(p.getProperty("LATITUDE"));
		AUDIO_WAV_INTENSITY = Integer.parseInt(p.getProperty("AUDIO_WAV_INTENSITY"));
		AUDIO_WAV_COUNT = Integer.parseInt(p.getProperty("AUDIO_WAV_COUNT"));
		IS_PLAY_COUNT_DOWN = Integer.parseInt(p.getProperty("IS_PLAY_COUNT_DOWN"));
		IS_SHOW_LIGHT = Integer.parseInt(p.getProperty("IS_SHOW_LIGHT"));
		IS_PLAY_AUDIO = Integer.parseInt(p.getProperty("IS_PLAY_AUDIO"));
		COMPANY_NAME = p.getProperty("COMPANY_NAME");
		TEST_EARTHQUAKE_FIRST_TIME = Long.parseLong(p.getProperty("TEST_EARTHQUAKE_FIRST_TIME")) * 1000;
		TEST_EARTHQUAKE_TIME = Long.parseLong(p.getProperty("TEST_EARTHQUAKE_TIME")) * 1000;
		SWITCH_SCREENSAVER_TIME_AFTER_EQM = Long.parseLong(p.getProperty("SWITCH_SCREENSAVER_TIME_AFTER_EQM")) * 1000;
		HELLO_SCREEN_SHOW_TIME = Integer.parseInt(p.getProperty("HELLO_SCREEN_SHOW_TIME")) * 1000;
		SCREENSAVER_SHOW_AFTER_EQM = Integer.parseInt(p.getProperty("SCREENSAVER_SHOW_AFTER_EQM")) * 1000;
		EQMQ_TCP_FUJIAN = p.getProperty("EQMQ_TCP_FUJIAN");
		EQMQ_PORT_FUJIAN = Integer.parseInt(p.getProperty("EQMQ_PORT_FUJIAN"));
		EQMQ_TOPIC_FUJIAN = p.getProperty("EQMQ_TOPIC_FUJIAN");
		EQMQ_TOPIC_FUJIAN_TEST = p.getProperty("EQMQ_TOPIC_FUJIAN_TEST");
		IS_TOPIC_PRODUCTION_MODEL = Integer.parseInt(p.getProperty("IS_TOPIC_PRODUCTION_MODEL"));
		
		if(IS_TOPIC_PRODUCTION_MODEL == 1) {
			EQMQ_TOPIC = EQMQ_TOPIC_FUJIAN;
		}else {
			EQMQ_TOPIC = EQMQ_TOPIC_FUJIAN_TEST;
		}
		
		COM = p.getProperty("COM");
		// EQMQ_AFTER_PRODUCTION_TIME =
		// Integer.parseInt(p.getProperty("EQMQ_AFTER_PRODUCTION_TIME")) * 1000
		// * 60;

		// 配置文件中添加部分：：：
		// # 福建地震局接口地址
		// EQMQ_TCP_FUJIAN=tcp://218.5.2.74
		// # 福建地震局接口端口
		// EQMQ_PORT_FUJIAN=1884
		// # 福建地震局接口订阅的主题（发布的频道名称）
		// EQMQ_TOPIC_FUJIAN=fjea/eewproject/eewmsg

		// 源码中添加部分：：：
		// EQMQ_TCP_FUJIAN = p.getProperty("EQMQ_TCP_FUJIAN");
		// EQMQ_PORT_FUJIAN =
		// Integer.parseInt(p.getProperty("EQMQ_PORT_FUJIAN"));
		// EQMQ_TOPIC_FUJIAN = p.getProperty("EQMQ_TOPIC_FUJIAN");
		log.info("加载配置文件成功，新配置java对象已更新");

	}

	public static int getHELLO_SCREEN_SHOW_TIME() {
		return HELLO_SCREEN_SHOW_TIME;
	}

	public static void setHELLO_SCREEN_SHOW_TIME(int hELLO_SCREEN_SHOW_TIME) {
		HELLO_SCREEN_SHOW_TIME = hELLO_SCREEN_SHOW_TIME;
	}

	public static int getSCREENSAVER_SHOW_AFTER_EQM() {
		return SCREENSAVER_SHOW_AFTER_EQM;
	}

	public static void setSCREENSAVER_SHOW_AFTER_EQM(int sCREENSAVER_SHOW_AFTER_EQM) {
		SCREENSAVER_SHOW_AFTER_EQM = sCREENSAVER_SHOW_AFTER_EQM;
	}

	// private static boolean isRealEarthquake = false;
	//
	// public static boolean isRealEarthquake() {
	// return isRealEarthquake;
	// }
	//
	// public static void setRealEarthquake(boolean isRealEarthquake) {
	// Constant.isRealEarthquake = isRealEarthquake;
	// }

}
package com.greathammer.eqm.util;

import java.io.IOException;
import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greathammer.eqm.earthquake.yujing.EarthquakeYuJing;
import com.greathammer.usmj.intensity.earthquake.event.EEWEvent;
import com.greathammer.usmj.intensity.earthquake.intensity.InstrumentalIntensity;
import com.greathammer.usmj.intensity.earthquake.intensity.InstrumentalIntensity.InstrumentalIntensityStandard;
import com.greathammer.usmj.intensity.file.FileHelper;
import com.greathammer.usmj.intensity.format.EEWMsgGeneralJsonFormat;
import com.greathammer.usmj.intensity.map.MapDistance;
import com.greathammer.usmj.intensity.map.NormalCoordinate;

/**
 * 工具类 计算烈度和p波s波的到达时间
 * 
 * @author FelixYin
 */
public class Intensity {

	private Log log = LogFactory.getLog(Intensity.class);

	private String json;

	private double distance;

	private int intensity;

	private int countDownP;

	private int countDownS;

	private int countDownL;

	public Intensity() {
	}

	public Intensity(String json) throws ParseException {
		this.json = json;

		log.info("开始计算烈度和距离");

		// 这个方法报错
		EEWMsgGeneralJsonFormat egjf = new EEWMsgGeneralJsonFormat(json);

		EEWEvent event = new EEWEvent(egjf);
		NormalCoordinate localCity = new NormalCoordinate(Constant.LONGITUDE, Constant.LATITUDE);
		NormalCoordinate earthquakeCenter = new NormalCoordinate(event.getEarthquakeCenter().getCenterCoordinate());

		this.distance = MapDistance.getDistance(localCity, earthquakeCenter);

		this.intensity = InstrumentalIntensity.getIntensity(InstrumentalIntensityStandard.CHNPGA,
				event.calPredictPGAWithDistance(distance));

		this.countDownP = (int) (distance / 6);
		this.countDownS = (int) (distance / 3.5);

		log.info("计算的烈度是：" + this.intensity);
		log.info("计算的距离是：" + this.distance);
		log.info("计算的P波传播时间是：" + this.countDownP);
		log.info("计算的S波传播时间是：" + this.countDownS);
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public int getCountDownP() {
		return countDownP;
	}

	public void setCountDownP(int countDownP) {
		this.countDownP = countDownP;
	}

	public int getCountDownS() {
		return countDownS;
	}

	public void setCountDownS(int countDownS) {
		this.countDownS = countDownS;
	}

	public int getCountDownL() throws Exception {
		if (this.countDownL == 0)
			throw UnknownError("面波暂时无法实现");
		return this.countDownL;
	}

	public void setCountDownL(int countDownL) {
		this.countDownL = countDownL;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public void predictIntensityCalExample() {
		String confiPath = "src/com/greathammer/eqm/util/eewmsg.json";
		String eewmsg = null;
		try {
			eewmsg = FileHelper.stringFromFile(confiPath, true);
			EEWMsgGeneralJsonFormat egjf = new EEWMsgGeneralJsonFormat(eewmsg);
			EEWEvent event = new EEWEvent(egjf);
			NormalCoordinate qingDao = new NormalCoordinate(120.33, 36.07);
			NormalCoordinate earthquakeCenter = new NormalCoordinate(event.getEarthquakeCenter().getCenterCoordinate());
			double distance = MapDistance.getDistance(qingDao, earthquakeCenter);

			int intensity = InstrumentalIntensity.getIntensity(InstrumentalIntensityStandard.CHNPGA,
					event.calPredictPGAWithDistance(distance));
			System.out.print("青岛市预计烈度是:" + intensity + ", 距离地震中心：" + distance + " km, ");
			System.out.println("P波预计到达时间为：" + (1440 / 6));
		} catch (IOException | ParseException e) {
			log.error(e);
		}
	}

	static String DATE_FORMAT_PATTERN = "yyyy-MM-dd hh:mm:ss";

	static String ADVICE_9_ = "剧烈震动，飞速撤离";
	static String EQDESC_9_ = "非常危险";

	static String ADVICE_8 = "剧烈震动，飞速撤离";
	static String EQDESC_8 = "摇晃颠簸";

	static String ADVICE_5_6_7 = "强烈震动，立即撤离";
	static String EQDESC_5_6_7 = "有感,请适当避震";

	static String ADVICE_3_4 = "较强震动，加强防护";
	static String EQDESC_3_4 = "微有感";

	static String ADVICE_1_2 = "轻微震动，注意防范";
	static String EQDESC_1_2 = "";

	private static void calculateArrivalTime(EarthquakeYuJing event, double distance, int coutdownS)
			throws ParseException {
		// String otime = event.getOtime();
		// SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		// long startTime = sdf1.parse(otime).getTime();
		// long endTime = new Date().getTime();
		// long minusTime = endTime - startTime;
		//
		// int minusSecond = (int) (minusTime / 1000000);
		// int calTime = (int) (distance / 3.5);
		event.setArrivalTime(coutdownS);
	}

	/**
	 * 当地震烈度小于4时蓝色灯闪烁亮，4<=烈度<6时黄色灯闪烁亮，6<=烈度<8时橙色灯闪烁亮；大于等于8时红色灯闪烁亮
	 * 
	 * @param event
	 * @param intensity
	 */
	private static void calculateLight(EarthquakeYuJing event, int intensity) {
		if (intensity < 4) {
			event.setLight("blue");
		} else if (intensity >= 4 && intensity < 6) {
			event.setLight("yellow");
		} else if (intensity >= 6 && intensity < 8) {
			event.setLight("orange");
		} else if (intensity >= 8) {
			event.setLight("red");
		}
	}

	private static void calculateAdivice(EarthquakeYuJing event, int intensity) {
		if (intensity <= 1 || intensity == 2) {
			event.setEqDesc(EQDESC_1_2);
			event.setAdvice(ADVICE_1_2);
		} else if (intensity == 3 || intensity == 4) {
			event.setEqDesc(EQDESC_3_4);
			event.setAdvice(ADVICE_3_4);
		} else if (intensity == 5 || intensity == 6 || intensity == 7) {
			event.setEqDesc(EQDESC_5_6_7);
			event.setAdvice(ADVICE_5_6_7);
		} else if (intensity == 8) {
			event.setEqDesc(EQDESC_8);
			event.setAdvice(ADVICE_8);
		} else { // >=9的情况，已非常非常危险了
			event.setEqDesc(EQDESC_9_);
			event.setAdvice(ADVICE_9_);
		}
	}

	public static void main(String[] args)
			throws ParseException, JsonParseException, JsonMappingException, IOException {
		Constant.LONGITUDE = 119.3;
		Constant.LATITUDE = 26.1;

		String content = "{\"1\":\"20161108111859_1\",\"2\":\"2016-11-08 11:18:59\",\"3\":117.66,\"4\":24.52,\"5\":\"漳州\",\"6\":8,\"7\":\"11:19:12\"}";

		// json 转 java bean
		EarthquakeYuJing event = new ObjectMapper().readValue(content, EarthquakeYuJing.class);

		Intensity intensityUtil = new Intensity(content);

		// 计算烈度
		int intensity = intensityUtil.getIntensity();
		event.setIntensity(intensity);

		// 计算距离
		double distance = intensityUtil.getDistance();
		event.setDistance(Math.round(distance) + "");

		// 计算地震倒计时
		calculateArrivalTime(event, distance, intensityUtil.getCountDownS());

		// 计算避震建议
		calculateAdivice(event, intensity);

		// 计算报警灯
		calculateLight(event, intensity);

		System.out.println(event);
	}

	private Exception UnknownError(String string) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}

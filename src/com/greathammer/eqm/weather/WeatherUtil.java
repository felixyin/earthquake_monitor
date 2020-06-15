package com.greathammer.eqm.weather;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author FelixYin
 */
public class WeatherUtil {

	private static Log log = LogFactory.getLog(WeatherUtil.class);

	private static final String ZHONG_HUA_WAN_NIAN_LI_TIAN_QI = "http://wthrcdn.etouch.cn/weather_mini?citykey=";

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String cityName = "青岛";
		
		WeatherMessage weather = getWeather(cityName);
		System.out.println(weather);
	}

	private static String getJson(String cityName) {
		Content returnContent = null;
		while (returnContent == null) {// 一直等待数据的到来
			try {
				returnContent = Request.Get(ZHONG_HUA_WAN_NIAN_LI_TIAN_QI + cityName).execute().returnContent();
				if (null == returnContent) {
					Thread.sleep(5000);
				}
			} catch (Exception e) {
				log.debug(e);
			}
		}
		String body = returnContent.asString(Charset.forName("UTF-8"));
		return body;
	}

	private static WeatherRoot getWeatherRoot(String cityKey) {
		String json = getJson(cityKey);
		WeatherRoot weatherRoot = null;
		try {
			weatherRoot = new ObjectMapper().readValue(json, WeatherRoot.class);
		} catch (IOException e) {
			log.error(e);
		}
		return weatherRoot;
	}

	public static WeatherMessage getWeather(String city) {
		String cityKey = CityKey.getCityKey(city);
		System.out.println(cityKey);
		WeatherRoot wr = getWeatherRoot(cityKey);
		WeatherMessage wm = new WeatherMessage();
		wm.setCity(city);
		WeatherData wd = wr.getData();
		WeatherForecast wf = wd.getForecast().get(0);
		wm.setCurrentTemp(wd.getWendu() + "℃");
		wm.setDirection(wf.getFengxiang());
		wm.setPollution(wd.getPollution());
		wm.setPower(wf.getFengli());
		wm.setStatus(wf.getType());
		wm.setTemperature(wf.getTemperature());
		return wm;
	}

}

class WeatherRoot {
	@JsonProperty("desc")
	private String desc;
	@JsonProperty("status")
	private int status;
	@JsonProperty("data")
	private WeatherData data;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public WeatherData getData() {
		return data;
	}

	public void setData(WeatherData data) {
		this.data = data;
	}

}

class WeatherData {
	@JsonProperty("wendu")
	private String wendu;
	@JsonProperty("ganmao")
	private String ganmao;
	@JsonProperty("forecast")
	private List<WeatherForecast> forecast;
	@JsonProperty("yesterday")
	private WeatherYesterday yesterday;
	@JsonProperty("aqi")
	private String aqi;
	@JsonProperty("city")
	private String city;
	private String pollution;

	public String getWendu() {
		return wendu;
	}

	public void setWendu(String wendu) {
		this.wendu = wendu;
	}

	public String getGanmao() {
		return ganmao;
	}

	public void setGanmao(String ganmao) {
		this.ganmao = ganmao;
	}

	public List<WeatherForecast> getForecast() {
		return forecast;
	}

	public void setForecast(List<WeatherForecast> forecast) {
		this.forecast = forecast;
	}

	public WeatherYesterday getYesterday() {
		return yesterday;
	}

	public void setYesterday(WeatherYesterday yesterday) {
		this.yesterday = yesterday;
	}

	public String getAqi() {
		return aqi;
	}

	public void setAqi(String aqi) {
		this.aqi = aqi;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 空气质量等级计算
	 * 
	 * @return
	 */
	public String getPollution() {
		int aqi = Integer.parseInt(this.getAqi());
		if (aqi >= 0 && aqi < 50) {
			setPollution("优");
		} else if (aqi >= 51 && aqi < 100) {
			setPollution("良");
		} else if (aqi >= 101 && aqi < 150) {
			setPollution("轻度污染");
		} else if (aqi >= 151 && aqi < 200) {
			setPollution("中度污染");
		} else if (aqi >= 201 && aqi < 300) {
			setPollution("重度污染");
		} else if (aqi > 300) {
			setPollution("严重污染");
		}
		return pollution;
	}

	public void setPollution(String pollution) {
		this.pollution = pollution;
	}

}

class WeatherForecast {
	@JsonProperty("fengxiang")
	private String fengxiang;
	@JsonProperty("fengli")
	private String fengli;
	@JsonProperty("high")
	private String high;
	@JsonProperty("type")
	private String type;
	@JsonProperty("low")
	private String low;
	@JsonProperty("date")
	private String date;

	public String getFengxiang() {
		return fengxiang;
	}

	public void setFengxiang(String fengxiang) {
		this.fengxiang = fengxiang;
	}

	public String getFengli() {
		return fengli;
	}

	public void setFengli(String fengli) {
		this.fengli = fengli;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMyLow() {
		return low.replace("低温 ", "");
	}

	public String getMyHigh() {
		return high.replace("高温 ", "");
	}

	public String getTemperature() {
		return getMyLow() + "至" + getMyHigh();
	}

}

class WeatherYesterday {
	@JsonProperty("fl")
	private String fl;
	@JsonProperty("fx")
	private String fx;
	@JsonProperty("high")
	private String high;
	@JsonProperty("type")
	private String type;
	@JsonProperty("low")
	private String low;
	@JsonProperty("date")
	private String date;

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public String getFx() {
		return fx;
	}

	public void setFx(String fx) {
		this.fx = fx;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

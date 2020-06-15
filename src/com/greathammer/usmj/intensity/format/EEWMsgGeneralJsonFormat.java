package com.greathammer.usmj.intensity.format;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

import com.greathammer.usmj.intensity.time.DateHelper;

/**
 * EEWMSG JSON 数据格式. <br>
 * Immutable <br>
 * 目前预警消息发布通用格式 Since 2015 <br>
 * 数据格式为: <br>
 * 1-EEWMsgEventID ---- "20160206035727_1"(事件ID_第x报) <br>
 * 2-EventDate ---- "2016-02-06 03:57:25) <br>
 * 3-Longitude ---- "120.37" <br>
 * 4-Latitude ---- "22.86" <br>
 * 5-PlaceName ---- "台湾高雄县" <br>
 * 6-Magnitude ---- "5.8"(公里) <br>
 * 7-ServerSendDate ---- "03:58:30" <br>
 * <br>
 * 
 * @author Zheng Chao
 * @since 2016.07.11
 */
public class EEWMsgGeneralJsonFormat implements Cloneable {

	private String _1_eewmsgEventID;
	private Date _2_eventDate;
	private double _3_longitude;
	private double _4_latitude;
	private String _5_placeName;
	private double _6_magnitude;
	private Date _7_serverSendDate;
	private JSONObject _jsonMsg;

	/**
	 * 使用JsonString初始化.
	 * 
	 * @param jsonString
	 * @throws ParseException
	 */
	public EEWMsgGeneralJsonFormat(String eewmsgGeneralJsonString) throws ParseException {
		// 缺少数据有效性判定
		_jsonMsg = new JSONObject(eewmsgGeneralJsonString);
		parseJsonObject(_jsonMsg);
	}

	/**
	 * @param eventID:
	 *            事件ID_第x报 "20160206035727_1"
	 * @param eventDate:
	 *            发震时刻 "2016-02-06 03:57:25"
	 * @param longitude:
	 *            经度(正-东经, 负-西经) "120.37"
	 * @param latitude:
	 *            纬度(正-南纬, 负-北纬) "22.86"
	 * @param placeName:
	 *            中文地名 "台湾高雄县"
	 * @param magnitude:
	 *            震级 "5.8"
	 * @param serverSendDate
	 *            发布该报时间 "03:58:30"
	 * @throws ParseException
	 */
	public EEWMsgGeneralJsonFormat(String eventID, Date eventDate, double longitude, double latitude, String placeName,
			double magnitude, Date serverSendDate) throws ParseException {

		JSONObject msg = new JSONObject();
		msg.put("1", eventID);
		msg.put("2", DateHelper.dateToStringFormateNormal(eventDate));
		msg.put("3", String.format("%.2f", longitude));
		msg.put("4", String.format("%.2f", latitude));
		msg.put("5", placeName);
		msg.put("6", String.format("%.1f", magnitude));
		msg.put("7", DateHelper.dateToStringFormateForEEWMQ(serverSendDate));

		_jsonMsg = msg;
		parseJsonObject(_jsonMsg);

	}

	private void parseJsonObject(JSONObject jo) throws ParseException {

		_1_eewmsgEventID = jo.getString("1");
		_2_eventDate = DateHelper.StringFormateNormalToDate(jo.getString("2"));
		_3_longitude = jo.getDouble("3");
		_4_latitude = jo.getDouble("4");
		_5_placeName = jo.getString("5");
		_6_magnitude = jo.getDouble("6");
		_7_serverSendDate = DateHelper
				.StringFormateNormalToDate(jo.getString("2").substring(0, 11) + jo.getString("7"));
		if (DateHelper.dateInterval(_2_eventDate, _7_serverSendDate) < -60 * 60)
			_7_serverSendDate = new Date(_7_serverSendDate.getTime() + 24 * 60 * 60 * 1000);

	}

	/**
	 * 用于子类.
	 */
	public EEWMsgGeneralJsonFormat(EEWMsgGeneralJsonFormat mjf) {

		_1_eewmsgEventID = mjf._1_eewmsgEventID;
		_2_eventDate = (Date) mjf._2_eventDate.clone();
		_3_longitude = mjf._3_longitude;
		_4_latitude = mjf._4_latitude;
		_5_placeName = mjf._5_placeName;
		_6_magnitude = mjf._6_magnitude;
		_7_serverSendDate = (Date) mjf._7_serverSendDate.clone();

		_jsonMsg = new JSONObject(mjf._jsonMsg.toString());
	}

	@Override
	public String toString() {
		return "EEWMsg content is :\n\t" + "1-EventID:" + _1_eewmsgEventID + " | value is " + getEventID() + " | NO:"
				+ getEventSerialNO() + "\n\t" + "2-EventDate:" + _2_eventDate + " | value is "
				+ DateHelper.dateToStringFormateNormal(_2_eventDate) + "\n\t" + "3-Longitude:" + _3_longitude + "\n\t"
				+ "4-Latitude:" + _4_latitude + "\n\t" + "5-PlaceName:" + _5_placeName + "\n\t" + "6-Magnitude:"
				+ _6_magnitude + "\n\t" + "7-ServerSendDate:" + _7_serverSendDate + " | value is "
				+ DateHelper.dateToStringFormateNormal(_7_serverSendDate) + "\n\t" + "JsonObject String is:"
				+ _jsonMsg.toString();
	}

	/**
	 * EEWMsg Content key:1的值
	 */
	public String getEEWMsgEventID() {
		return _1_eewmsgEventID;
	}

	public String getEventID() {
		return _1_eewmsgEventID.substring(0, 14);
	}

	public int getEventSerialNO() {
		return Integer.valueOf(_1_eewmsgEventID.substring(15, 16));
	}

	/**
	 * EEWMsg Content key:2的值
	 */
	public Date getEventDate() {
		return (Date) _2_eventDate.clone();
	}

	/**
	 * EEWMsg Content key:3的值
	 */
	public Double getLongitude() {
		return _3_longitude;
	}

	/**
	 * EEWMsg Content key:4的值
	 */
	public Double getLatitude() {
		return _4_latitude;
	}

	/**
	 * EEWMsg Content key:5的值
	 */
	public String getPlaceName() {
		return _5_placeName;
	}

	/**
	 * EEWMsg Content key:6的值
	 */
	public Double getMagnitude() {
		return _6_magnitude;
	}

	/**
	 * EEWMsg Content key:7的值
	 */
	public Date getServerSendDate() {
		return (Date) _7_serverSendDate.clone();
	}

	private void setServerSendDate(Date date) {
		_7_serverSendDate = date;
		_jsonMsg.put("7", DateHelper.dateToStringFormateForEEWMQ(date));
	}

	public EEWMsgGeneralJsonFormat setNewServerSendDate(Date date) {
		EEWMsgGeneralJsonFormat newObject = (EEWMsgGeneralJsonFormat) this.clone();
		newObject.setServerSendDate(date);
		return newObject;
	}

	/**
	 * 获取JsonObject
	 */
	public JSONObject getJsonObject() {
		return new JSONObject(_jsonMsg.toString());
	}

	public static void main(String[] args) {
		// jsonString内容已被修改..
		String jsonString = "{\"1\":\"20160706103126_3\",\"2\":\"2016-07-06 21:59:37\",\"3\":\"120.60\",\"4\":\"26.73\",\"5\":\"台湾海峡北部海域\",\"6\":\"3.4\",\"7\":\"22:50:26\"}";
		try {
			EEWMsgGeneralJsonFormat test = new EEWMsgGeneralJsonFormat(jsonString);
			System.out.println(test);

			Date newDate = new Date();
			// 演示setNewServerSendDate
			System.out.println("\nsetNewServerSendDate后, 新对象:\n" + test.setNewServerSendDate(newDate));
			System.out.println("\n旧对象:\n" + test);

			EEWMsgGeneralJsonFormat newTest1 = new EEWMsgGeneralJsonFormat(test);
			System.out.println("\n new Test : \n" + newTest1);

			EEWMsgGeneralJsonFormat newTest = new EEWMsgGeneralJsonFormat("20160706103126_3",
					DateHelper.StringFormateNormalToDate("2016-07-06 21:59:37"), 120.60, 26.73, "台湾海峡北部海域", 3.4,
					DateHelper.StringFormateNormalToDate("2016-07-06 22:50:26"));
			System.out.println("\nnewTest:\n" + newTest);

			EEWMsgGeneralJsonFormat cloneTest = (EEWMsgGeneralJsonFormat) newTest.clone();
			// newTest._2_eventDate.setYear(2001);
			// newTest._7_serverSendDate.setYear(2000);
			newTest._jsonMsg.put("1", "2");
			System.out.println("\nclone..:\n" + cloneTest);
			System.out.println("\nnewTest..:\n" + newTest);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	// clone...
	@Override
	protected Object clone() {
		EEWMsgGeneralJsonFormat object = null;
		try {
			object = (EEWMsgGeneralJsonFormat) super.clone();
			object._2_eventDate = (Date) _2_eventDate.clone();
			object._7_serverSendDate = (Date) _7_serverSendDate.clone();
			object._jsonMsg = new JSONObject(_jsonMsg.toString());
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return object;
	}

	private EEWMsgGeneralJsonFormat() {
	}

	public EEWMsgGeneralJsonFormat copy() {
		EEWMsgGeneralJsonFormat newObject = new EEWMsgGeneralJsonFormat();
		newObject._1_eewmsgEventID = _1_eewmsgEventID;
		newObject._2_eventDate = (Date) _2_eventDate.clone();
		newObject._3_longitude = _3_longitude;
		newObject._4_latitude = _4_latitude;
		newObject._5_placeName = _5_placeName;
		newObject._6_magnitude = _6_magnitude;
		newObject._7_serverSendDate = (Date) _7_serverSendDate.clone();

		newObject._jsonMsg = new JSONObject(_jsonMsg.toString());

		return newObject;
	}

}

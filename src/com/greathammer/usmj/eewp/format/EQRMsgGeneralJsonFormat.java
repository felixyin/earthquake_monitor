package com.greathammer.usmj.eewp.format;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONObject;

import com.greathammer.usmj.eewp.earthquake.SimpleEarthquakeCenter;
import com.greathammer.usmj.eewp.time.DateHelper;

/**
 * EQRMSG JSON 数据格式 <br>
 * Immutable <br>
 * 目前地震速报消息发布通用格式 Since 2015 <br>
 * 数据格式为: <br>
 * 1-EQRMsgEventID ---- "CD.20160118025943.000"(自动报产出方式+发震时刻) <br>
 * 2-EQRMsgType ---- "FormalAuto"/"A"-国家台网自动报; "FormalManual"/"I"-国家台网正式报/人工
 * <br>
 * 3-Location ---- 震中地名 <br>
 * 4-Latitude ---- 震中纬度 <br>
 * 5-Longitude ---- 震中经度 <br>
 * 6-Depth ---- 震源深度, km <br>
 * 7-Magnitude ---- 震级 <br>
 * 8-StorageType ---- 地震入编标准,十进制表示(二进制判别),1(001)-省内;2(010)-国内;4(100)-全球;
 * 另,3(011)-国内+省内,但目前只有1/2/4值的历史记录 <br>
 * 9-Isnew ---- 是否为新消息, 该项目目前无特别功能, 暂保留 <br>
 * 10-EventDate ---- 发震时刻
 * 
 * @author Zheng Chao
 * @since 2016.07.22
 * @version 0.1.1
 * @version 目前版本:{@value #VERSION}}
 */
public class EQRMsgGeneralJsonFormat implements Cloneable {
	public static final String VERSION = "0.1.1.1";

	private String _1_eqrmsgEventID;
	private EQRMsgType _2_eqrMsgType;
	private String _3_location;
	private double _4_latitude;
	private double _5_longitude;
	private double _6_depth;
	private double _7_magnitude;
	private int _8_storageType;
	private boolean _9_isNew;
	private Date _10_eventDate;

	private JSONObject _jsonMsg;

	public enum EQRMsgType {
		FormalAuto("A"), FormalManual("I");
		private String _markString;

		private EQRMsgType(String marker) {
			_markString = marker;
		}

		public static EQRMsgType getTypeWithMarker(String marker) {
			switch (marker) {
			case "A":
				return FormalAuto;
			case "I":
				return EQRMsgType.FormalManual;
			default:
				return null;
			}
		}

		public String getMarker() {
			return _markString;
		}
	};

	public EQRMsgGeneralJsonFormat(String eqrmsgGeneralJsonString) throws ParseException {
		_jsonMsg = new JSONObject(eqrmsgGeneralJsonString);
		parseJsonObject(_jsonMsg);
	}

	/**
	 * 
	 * @param eqrmsgEventID
	 *            ---- "CD.20160118025943.000"(自动报产出方式+发震时刻)
	 * @param eqrMsgType
	 *            ---- "FormalAuto"/"A"-国家台网自动报; "FormalManual"/"I"-国家台网正式报/人工
	 * @param sec
	 *            ---- SimpleEarthquakeCenter.
	 * @param storageType
	 *            ---- 地震入编标准,十进制表示(二进制判别),1(001)-省内;2(010)-国内;4(100)-全球;
	 *            另,3(011)-国内+省内,但目前只有1/2/4值的历史记录
	 * @param isNew
	 *            ---- 是否为新消息, 该项目目前无特别功能, 暂保留
	 */
	public EQRMsgGeneralJsonFormat(String eqrmsgEventID, EQRMsgType eqrMsgType, SimpleEarthquakeCenter sec,
			int storageType, boolean isNew) {
		_1_eqrmsgEventID = eqrmsgEventID;
		_2_eqrMsgType = eqrMsgType;
		_3_location = sec.getPLACE_NAME();
		_4_latitude = sec.getLatitude();
		_5_longitude = sec.getLongitude();
		_6_depth = sec.getDepth();
		_7_magnitude = sec.getMagnitude();
		_8_storageType = storageType;
		_9_isNew = isNew;
		_10_eventDate = sec.getHappenDate();

		_jsonMsg = new JSONObject();
		_jsonMsg.put("1", _1_eqrmsgEventID);
		_jsonMsg.put("2", _2_eqrMsgType.getMarker());
		_jsonMsg.put("3", _3_location);
		_jsonMsg.put("4", String.format("%.2f", _4_latitude));
		_jsonMsg.put("5", String.format("%.2f", _5_longitude));
		_jsonMsg.put("6", String.format("%.0f", _6_depth));
		_jsonMsg.put("7", String.format("%.1f", _7_magnitude));
		_jsonMsg.put("8", String.format("%d", _8_storageType));
		_jsonMsg.put("9", _9_isNew ? "1" : "0");
		_jsonMsg.put("10", DateHelper.dateToStringFormateNormal(_10_eventDate));
	}

	private void parseJsonObject(JSONObject jo) throws ParseException {
		_1_eqrmsgEventID = jo.getString("1");
		_2_eqrMsgType = EQRMsgType.getTypeWithMarker(jo.getString("2"));
		_3_location = jo.getString("3");
		_4_latitude = Double.valueOf(jo.getString("4"));
		_5_longitude = Double.valueOf(jo.getString("5"));
		_6_depth = Double.valueOf(jo.getString("6"));
		_7_magnitude = Double.valueOf(jo.getString("7"));
		_8_storageType = Integer.valueOf(jo.getString("8"));
		_9_isNew = jo.getString("9").equals("1") ? true : false;
		_10_eventDate = DateHelper.StringFormateNormalToDate(jo.getString("10"));
	}

	@Override
	public String toString() {
		return getClass().getName() + ", hashcode:" + hashCode() + ": [\n\t" + "1-EQRMsgEventID: " + _1_eqrmsgEventID
				+ "\n\t" + "2-EQRMsgType: " + _2_eqrMsgType + " | value is " + _2_eqrMsgType.getMarker() + "\n\t"
				+ "3-Location: " + _3_location + "\n\t" + "4-Latitude: " + _4_latitude + "\n\t" + "5-Longitude: "
				+ _5_longitude + "\n\t" + "6-Depth: " + _6_depth + "\n\t" + "7-Magnitude: " + _7_magnitude + "\n\t"
				+ "8-StorageType: " + _8_storageType + "\n\t" + "9-Isnew: " + (_9_isNew ? "true" : "false") + "\n\t"
				+ "10-EventDate: " + _10_eventDate + " | value is "
				+ DateHelper.dateToStringFormateNormal(_10_eventDate) + "\n\t" + "JsonObject String is: " + _jsonMsg
				+ "]";
	}

	public String getEQRMsgEventID() {
		return _1_eqrmsgEventID;
	}

	public EQRMsgType getEQRMsgType() {
		return _2_eqrMsgType;
	}

	public String getLocation() {
		return _3_location;
	}

	public double getLatitude() {
		return _4_latitude;
	}

	public double getLongitude() {
		return _5_longitude;
	}

	public double getDepth() {
		return _6_depth;
	}

	public double getMagnitude() {
		return _7_magnitude;
	}

	public int getStorageType() {
		return _8_storageType;
	}

	public boolean getIsNew() {
		return _9_isNew;
	}

	public Date getEventDate() {
		return _10_eventDate;
	}

	@Override
	public Object clone() {
		EQRMsgGeneralJsonFormat object = null;
		try {
			object = (EQRMsgGeneralJsonFormat) super.clone();
			object._10_eventDate = (Date) _10_eventDate.clone();
			object._jsonMsg = new JSONObject(_jsonMsg.toString());
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return object;
	}

	public static void main(String[] args) {
		String eqrMsgGeneralJsonString = "{\"1\":\"AU.20160720231319.000\",\"2\":\"A\",\"3\":\"瓦努阿图群岛\",\"4\":\"-18.87\",\"5\":\"169.00\",\"6\":\"197\",\"7\":\"6.1\",\"8\":\"4\",\"9\":\"0\",\"10\":\"2016-07-20 23:13:19\"}";
		try {
			EQRMsgGeneralJsonFormat emgj = new EQRMsgGeneralJsonFormat(eqrMsgGeneralJsonString);
			System.out.println(emgj);

			String eqrmsgEventID = "AU.20160720231319.000";
			EQRMsgType eqrMsgType = EQRMsgType.getTypeWithMarker("A");
			SimpleEarthquakeCenter sec = new SimpleEarthquakeCenter.InstanceBuilder(169, -18.87).setDepth(197)
					.setHappenDate(DateHelper.StringFormateNormalToDate("2016-07-20 23:13:19")).setMagnitude(6.1)
					.setPlaceName("瓦努阿图群岛").buildInstance();
			int storageType = 4;
			boolean isNew = false;
			emgj = new EQRMsgGeneralJsonFormat(eqrmsgEventID, eqrMsgType, sec, storageType, isNew);
			System.out.println(emgj);

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

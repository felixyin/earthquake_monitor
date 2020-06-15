package com.greathammer.usmj.intensity.map;

//import org.geotools.geometry.jts.JTS;
//import org.geotools.referencing.crs.DefaultGeographicCRS;
//import org.opengis.referencing.crs.CoordinateReferenceSystem;
//import org.opengis.referencing.operation.TransformException;
//
//import com.vividsolutions.jts.geom.Coordinate;
//
//import usmj.eewp.time.NanoTimerHelper;

/**
 * 计算Map上两点距离 <br>
 * 方法3使用GEOTOOLS_14.2包
 * 
 * @author Zhou Yueyong
 * @author Zheng Chao
 * @version 0.2.1
 */
public class MapDistance {

	private static double EARTH_RADIUS = 6378.137;

	// private static CoordinateReferenceSystem defaultCRS =
	// DefaultGeographicCRS.WGS84;

	// private static Coordinate A;
	// private static Coordinate B;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 计算两点距离, 返回值单位km, 精度到米(保留3位小数). <br>
	 * 效率最高(0.025), 精度较高(0.17%), 方法见main.
	 * 
	 * @param pointOne
	 * @param pointTwo
	 */
	public static double getDistance(NormalCoordinate pointOne, NormalCoordinate pointTwo) {
		Double lat1 = pointOne.getLatitude();
		Double lng1 = pointOne.getLongitude();
		Double lat2 = pointTwo.getLatitude();
		Double lng2 = pointTwo.getLongitude();

		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double difference = radLat1 - radLat2;
		double mdifference = rad(lng1) - rad(lng2);
		double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(mdifference / 2), 2)));
		distance = Math.round(distance * 1000) / 1000.0; // 精度,保留3位小数.
		// String distanceStr = distance+"";
		// distanceStr = distanceStr.
		// substring(0, distanceStr.indexOf("."));

		// return distanceStr;
		return distance;
	}

	/**
	 * 计算两点距离, 监测中心方法, 返回值单位km, 精度到米(保留3位小数). <br>
	 * 效率较高(0.034), 精度较高(0.06%), 方法见main.
	 * 
	 * @param pointOne
	 * @param pointTwo
	 */
	public static double getDistance2(NormalCoordinate pointOne, NormalCoordinate pointTwo) {
		Double lat1 = rad(pointOne.getLatitude());
		Double lng1 = rad(pointOne.getLongitude());
		Double lat2 = rad(pointTwo.getLatitude());
		Double lng2 = rad(pointTwo.getLongitude());

		double x1 = EARTH_RADIUS * Math.cos(lat1) * Math.cos(lng1);
		double y1 = EARTH_RADIUS * Math.cos(lat1) * Math.sin(lng1);
		double z1 = EARTH_RADIUS * Math.sin(lat1);
		double x2 = EARTH_RADIUS * Math.cos(lat2) * Math.cos(lng2);
		double y2 = EARTH_RADIUS * Math.cos(lat2) * Math.sin(lng2);
		double z2 = EARTH_RADIUS * Math.sin(lat2);

		return Math.round(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2)) * 1000)
				/ 1000.0;
	}

}

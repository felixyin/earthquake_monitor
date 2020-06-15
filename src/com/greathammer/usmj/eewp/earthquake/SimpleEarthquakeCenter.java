package com.greathammer.usmj.eewp.earthquake;

import java.util.Date;

import com.greathammer.usmj.eewp.math.MathPair;

/**
 * 简单震中类.
 * 
 * @since Java 7.0
 * @author Zheng Chao
 */
public class SimpleEarthquakeCenter {

	private final MathPair<Double, Double> CENTER_COORDINATE;// first -
																// longitude;
																// second -
																// latitude
	private final double DEPTH;
	private final double MAGNITUDE;
	private final int INTENSITY;

	private final Date HAPPEN_DATE;
	private final String PLACE_NAME;

	public static class InstanceBuilder {
		private MathPair<Double, Double> _centerCoordinate;
		// Optional
		private double _depth;
		private double _magnitude;
		private int _intensity;
		private Date _happenDate;
		private String _placeName;

		public InstanceBuilder(double longtiude, double latitude) {
			_centerCoordinate = new MathPair<Double, Double>(longtiude, latitude);
		}

		public InstanceBuilder(SimpleEarthquakeCenter center) {
			_centerCoordinate = center.CENTER_COORDINATE;
			_depth = center.DEPTH;
			_magnitude = center.MAGNITUDE;
			_intensity = center.INTENSITY;
			_happenDate = center.HAPPEN_DATE;
			_placeName = center.PLACE_NAME;
		}

		public InstanceBuilder setCenterCoordinate(double longtiude, double latitude) {
			_centerCoordinate = new MathPair<Double, Double>(longtiude, latitude);
			return this;
		}

		public InstanceBuilder setDepth(double depth) {
			_depth = depth;
			return this;
		}

		public InstanceBuilder setMagnitude(double magnitude) {
			_magnitude = magnitude;
			return this;
		}

		public InstanceBuilder setIntensity(int intensity) {
			_intensity = intensity;
			return this;
		}

		public InstanceBuilder setHappenDate(Date happenDate) {
			_happenDate = happenDate;
			return this;
		}

		public InstanceBuilder setPlaceName(String placeName) {
			_placeName = placeName;
			return this;
		}

		public SimpleEarthquakeCenter buildInstance() {
			return new SimpleEarthquakeCenter(this);
		}
	}

	private SimpleEarthquakeCenter(InstanceBuilder builder) {
		CENTER_COORDINATE = builder._centerCoordinate;
		DEPTH = builder._depth;
		MAGNITUDE = builder._magnitude;
		INTENSITY = builder._intensity;
		HAPPEN_DATE = builder._happenDate;
		PLACE_NAME = builder._placeName;
	}

	public double getLongitude() {
		return CENTER_COORDINATE.getFirst();
	}

	public double getLatitude() {
		return CENTER_COORDINATE.getSecond();
	}

	/**
	 * PairModel - center coordinate In model, first is longitude, second is
	 * latitude;
	 * 
	 * @return
	 */
	public MathPair<Double, Double> getCenterCoordinate() {
		return CENTER_COORDINATE;
	}

	public double getDepth() {
		return DEPTH;
	}

	public double getMagnitude() {
		return MAGNITUDE;
	}

	public int getIntensity() {
		return INTENSITY;
	}

	public Date getHappenDate() {
		return HAPPEN_DATE;
	}

	public String getPLACE_NAME() {
		return PLACE_NAME;
	}

}

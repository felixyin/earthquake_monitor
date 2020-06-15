package com.greathammer.usmj.intensity.map;

import com.greathammer.usmj.intensity.math.MathPair;

/**
 * 一般经纬度, 继承了MathPair类. <br>
 * First - longitude <br>
 * Second - latitude
 * 
 * @author Zheng Chao
 */
public class NormalCoordinate extends MathPair<Double, Double> {

	// first - longitude; second - latitude
	public NormalCoordinate(Double longitude, Double latitude) {
		super(longitude, latitude);
	}

	public NormalCoordinate(MathPair<Double, Double> pair) {
		super(pair.getFirst(), pair.getSecond());
	}

	public double getLongitude() {
		return super.getFirst();
	}

	public double getLatitude() {
		return super.getSecond();
	}

	@Override
	public String toString() {
		return "NormalCoordinate [longitude=" + getLongitude() + ", latitude=" + getLatitude() + "]";
	}
}

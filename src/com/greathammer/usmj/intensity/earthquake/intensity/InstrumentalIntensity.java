package com.greathammer.usmj.intensity.earthquake.intensity;

/**
 * 仪器烈度标准.
 * 
 * @author 韦永祥
 * @author Zheng Chao
 * @version 0.2.1
 */
public class InstrumentalIntensity {

	// 单位gal
	private static final double[] CHINA_INTENSITY_PGA = new double[] { 0.8, 2.5, 8.0, 22.0, 44.0, 89.0, 117.0, 353.0,
			707.0, 1414.0, 2000.0 };
	// 单位cm/s
	private static final double[] CHINA_INTENSITY_PGV = new double[] { 0.1, 0.5, 1.0, 2.0, 4.0, 9.0, 18.0, 35.0, 71.0,
			141.0, 2000.0 };

	// 单位gal
	private static final double[] USA_INTENSITY_PGA = new double[] { 1.7, 5.0, 14.0, 39.0, 92.0, 180.0, 340.0, 650.0,
			1240.0, 2000.0 };
	// 单位cm/s
	private static final double[] USA_INTENSITY_PGV = new double[] { 0.1, 0.5, 1.1, 3.4, 8.1, 16.0, 31.0, 60.0, 116.0,
			200.0 };

	public enum InstrumentalIntensityStandard {
		CHNPGA("China Intensity PGA", CHINA_INTENSITY_PGA), CHNPGV("China Intensity PGV", CHINA_INTENSITY_PGV), USAPGA(
				"USA Intensity PGA", USA_INTENSITY_PGA), USAPGV("USA Intensity PGV", USA_INTENSITY_PGV);

		private double[] _values;
		private String _name;

		private InstrumentalIntensityStandard(String name, double[] values) {
			_values = values;
			_name = name;
		}

		private double[] getValues() {
			return _values;
		}

		@Override
		public String toString() {
			StringBuilder valueList = new StringBuilder("Instrumental Intensity--Value\n");

			for (int i = 1; i <= _values.length; i++) {
				valueList.append(i + "--" + _values[i - 1] + "\n");
			}

			return _name + ", valueList:\n" + valueList;
		}
	};

	public static double getPGAFromIntensity(InstrumentalIntensityStandard standard, int intensity) {
		return standard.getValues()[intensity - 1];
	}

	public static int getIntensity(InstrumentalIntensityStandard standard, double value) {
		double[] values = standard.getValues();

		if (value >= values[values.length - 1])
			return values.length;
		else {
			for (int i = 0; i < values.length - 1; i++) {
				if (value > values[i] && value <= values[i + 1]) {
					return (i + 2);
				}
			}
		}
		return 1;
	}

	// /**
	// * 将烈度转为PGA
	// * @param pgaValues
	// * @param intensity
	// * @return
	// */
	// @Deprecated
	// private static float getValue(float[]pgaValues,int index) {
	// return pgaValues[index];
	// }

	public static void main(String[] args) {
		System.out.println(InstrumentalIntensityStandard.USAPGA);
		System.out.println(InstrumentalIntensity.getIntensity(InstrumentalIntensityStandard.CHNPGA, 2.6));
	}

}

package com.riozenc.quicktool.common.util.math;

public class DoubleUtil {

	/**
	 * 不四舍五入
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getDouble(double a, int b) {
		int x = 0;
		int y = 1;
		for (int i = 0; i < b; i++) {
			y = y * 10;
		}
		x = (int) (a * y);
		return (double) x / y;
	}
}

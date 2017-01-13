package com.myhope.test.temp;

public class Test8 {

	public static long calDistance(double lon1, double lat1, double lon2, double lat2) {
		final double EARTH_RADIUS = 6378137;
		double radlon1 = rad(lon1);
		double radlon2 = rad(lon2);
		double a = radlon1 - radlon2;
		double b = rad(lat1) - rad(lat2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radlon1) * Math.cos(radlon2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		long res = Math.round(s * 1000) / 1000;
		return res;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static void main(String[] args) {
		System.out.println(calDistance(119.97881, 36.35206, 120.063481, 36.007091));
	}

}

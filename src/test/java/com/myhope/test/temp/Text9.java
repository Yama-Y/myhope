package com.myhope.test.temp;

import java.text.ParseException;

public class Text9 {
	public static void main(String[] args) throws ParseException {
		int[] a = {1,3,2,4,5,7,6,8};
		int temp = 0;
		for (int i = a.length - 1; i > 0; --i) {
			for (int j = 0; j < i; ++j) {
				if (a[j + 1] < a[j]) {
					temp = a[j];
					a[j] = a[j + 1];
					a[j + 1] = temp;
				}
			}
		}
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

}
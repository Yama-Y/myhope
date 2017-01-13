package com.myhope.test.algorithms;

import java.util.Random;

/**
 * 排序算法总结
 * 
 * @author Yang
 * 
 */
public class Sort {
	public static void main(String[] args) {
		Sort sort = new Sort();
		int[] n = new int[10000];
		Random r = new Random(10000);
		for (int i = 0; i < 10000; i++) {
			n[i] = r.nextInt();
		}
		Long a = System.currentTimeMillis();
		sort.sort(n);
		System.out.println(System.currentTimeMillis() - a);
		a = System.currentTimeMillis();
		sort.quickSort(n, 0, 9999);
		System.out.println(System.currentTimeMillis() - a);
	}

	/**
	 * Bubble Sort 冒泡
	 * 
	 * @param a
	 */
	public void sort(int[] a) {
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
	}

	/**
	 * Quicksort 快速排序
	 * 
	 * @param s
	 * @param l
	 * @param r
	 */
	public void quickSort(int s[], int l, int r) {
		if (l < r) {
			int i = l, j = r, x = s[l];
			while (i < j) {
				while (i < j && s[j] >= x)
					// 从右向左找第一个小于x的数
					j--;
				if (i < j)
					s[i++] = s[j];

				while (i < j && s[i] < x)
					// 从左向右找第一个大于等于x的数
					i++;
				if (i < j)
					s[j--] = s[i];
			}
			s[i] = x;
			quickSort(s, l, i - 1); // 递归调用
			quickSort(s, i + 1, r);
		}
	}
}
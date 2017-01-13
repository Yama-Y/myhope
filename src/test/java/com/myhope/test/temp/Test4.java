package com.myhope.test.temp;

import java.util.HashSet;
import java.util.Set;

public class Test4 {

	public static void main(String[] args) {
		Set<Integer> set = new HashSet<>();
		Integer a = 1;
		set.add(a);
		set.add(a);
		set.add(2);
		set.add(3);
		for (Integer integer : set) {
			System.out.println(integer);
		}
		set.remove(a);
		for (Integer integer : set) {
			System.out.println(integer);
		}
	}

}

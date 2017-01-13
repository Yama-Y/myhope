package com.myhope.util.base;

import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 * 
 * @author YangMing
 * 
 */
public class ReUtil {

	/**
	 * 正则校验
	 */
	public boolean checkRE(String RE, String value) {
		boolean isRight = true;
		Pattern pattern = Pattern.compile(RE);
		if (value != null) {
			if (!pattern.matcher(value).matches()) {
				isRight = false;
			}
		}
		return isRight;
	}
}

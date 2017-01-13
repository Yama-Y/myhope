package com.myhope.util.base;

/**
 * String工具类
 * 
 * @author YangMing
 * 
 */
public class StringUtil {

	/**
	 * 格式化字符串
	 * 
	 * 例：formateString("xxx{0}bbb",1) = xxx1bbb
	 * 
	 * @param str
	 * @param params
	 * @return
	 */
	public static String formateString(String str, String... params) {
		for (int i = 0; i < params.length; i++) {
			str = str.replace("{" + i + "}", params[i] == null ? "" : params[i]);
		}
		return str;
	}
	
	/**
	 * 去空格
	 */
	public static String trim(String string) {
		string = string.replace(" ", "");
		string = string.trim();
		return string;
	}

}

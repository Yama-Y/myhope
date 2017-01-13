package com.myhope.util.base;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * API工具类
 * 
 * @author YangMing
 * 
 */
public class ApiUtil {

	public static String request_baidu(String httpUrl, Map<String, String> httpArgMap) {
		return request(httpUrl, httpArgMap, "c8039f6a6ddccc6b3790856aab50b0b3");
	}

	public static String request(String httpUrl, Map<String, String> httpArgMap) {
		return request(httpUrl, httpArgMap, null);
	}

	public static String request(String httpUrl, Map<String, String> httpArgMap, String apikey) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		httpUrl = httpUrl + "?";
		Set<String> keySet = httpArgMap.keySet();
		for (String string : keySet) {
			httpUrl = httpUrl + string + "=" + httpArgMap.get(string) + "&";
		}
		httpUrl = httpUrl.replaceAll(" ", "");
		try {
			URL url = new URL(httpUrl.substring(0, httpUrl.length() - 1));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			if (apikey != null) {
				connection.setRequestProperty("apikey", apikey);
			}
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

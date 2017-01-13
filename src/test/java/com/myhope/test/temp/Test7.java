package com.myhope.test.temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

public class Test7 {

	public static void main(String[] args) {
		try {
			String ip1 = getMyIP();
			System.out.println("myIP:" + ip1);
			String ip2 = getMyIPLocal();
			System.out.println("myLocalIP:" + ip2);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static String getMyIP() throws IOException {
		InputStream ins = null;
		try {
			URL url = new URL("http://1212.ip138.com/ic.asp");
			URLConnection con = url.openConnection();
			ins = con.getInputStream();
			InputStreamReader isReader = new InputStreamReader(ins, "GB2312");
			BufferedReader bReader = new BufferedReader(isReader);
			StringBuffer webContent = new StringBuffer();
			String str = null;
			while ((str = bReader.readLine()) != null) {
				webContent.append(str);
			}
			int start = webContent.indexOf("[") + 1;
			int end = webContent.indexOf("]");
			return webContent.substring(start, end);
		} finally {
			if (ins != null) {
				ins.close();
			}
		}
	}

	private static String getMyIPLocal() throws IOException {
		InetAddress ia = InetAddress.getLocalHost();
		return ia.getHostAddress();
	}
}

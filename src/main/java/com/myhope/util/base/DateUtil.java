package com.myhope.util.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 * 
 * @author YangMing
 * 
 */
public class DateUtil {
	public static void main(String[] args) {
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return "";
	}

	/**
	 * 日期转字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date stringToDate(String dateString) {
		return stringToDate(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 字符串转日期
	 * 
	 * @param dateString
	 *            日期字符串
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static Date stringToDate(String dateString, String pattern) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat(pattern);
		Date date = new Date();
		try {
			date = simpledateformat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @Description: 求出两个时间段的时间差（精确到分/秒）
	 * @Title: timeDifference4mmss
	 * @param timeStart
	 * @param timeEnd
	 * @return String
	 */
	public static String timeDifference4mmss(Date timeStart, Date timeEnd) {
		if (timeStart == null || timeEnd == null) {
			return "0";
		}
		long now = timeStart.getTime();
		long limit = timeEnd.getTime();
		int minute = (int) ((Math.abs(now - limit) % (3600000 * 24)) % 3600000) / 60000;
		int second = (int) ((Math.abs(now - limit) % (3600000 * 24)) % 3600000) % 60000 / 1000;
		String timeLeft = "0";
		StringBuffer sb = new StringBuffer();
		if (now < limit) {
			if (minute > 0) {
				sb.append(minute).append("分").append(second).append("秒");
			} else {
				sb.append(second).append("秒");
			}
		}
		timeLeft = sb.toString();
		return timeLeft;
	}

	public static Date getWeekBegin(Date date) {
		return getWeekBegin(date, 0);
	}

	/**
	 * @Description: 获得周的开始日(day为向后的偏移量)
	 * @Title: getWeekBegin
	 * @param date
	 * @param day
	 * @return Date
	 */
	public static Date getWeekBegin(Date date, Integer day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		c.add(Calendar.DATE, -day_of_week + 1 + day);

		return stringToDate(dateToString(c.getTime(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
	}

	public static Integer getWeekNumber(Date date) {
		return getWeekNumber(date, 0);
	}

	/**
	 * @Description: 获得本月的第几周(day为向后的偏移量)
	 * @Title: getWeekNumber
	 * @param date
	 * @param day
	 * @return Integer
	 */
	public static Integer getWeekNumber(Date date, Integer day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_MONTH) + day;
	}

	/**
	 * @Description: 获得本月第一天
	 * @param date
	 * @param day
	 * @return Date
	 */
	public static Date getMonthBegin(Date date, Integer day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 当前月的第一天
		c.set(GregorianCalendar.DAY_OF_MONTH, 1);
		return stringToDate(dateToString(c.getTime(), "yyyy-MM-dd") + " 00:00:00");
	}

	/**
	 * @Description: 获得本月最后一天
	 * @param date
	 * @param day
	 * @return Date
	 */
	public static Date getMonthEnd(Date date, Integer day) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		// 当前月的最后一天
		c.set(Calendar.DATE, 1);
		c.roll(Calendar.DATE, -1);
		return stringToDate(dateToString(c.getTime(), "yyyy-MM-dd") + " 23:59:59");
	}

}

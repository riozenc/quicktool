/**
 *    Auth:riozenc
 *    Date:2018年9月27日 上午9:19:46
 *    Title:com.riozenc.quicktool.common.util.date.NDateUtil.java
 **/
package com.riozenc.quicktool.common.util.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 
 * @author riozenc
 *
 */
public class NDateUtil {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public enum NdateFormatter {
		DATE_TIME, DATE
	}

	/**
	 * 获取当前日期和时分秒
	 * 
	 * @return
	 */
	public static String getDateTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	/**
	 * 获取当前日期
	 * 
	 * @return
	 */
	public static String getDate() {
		return LocalDate.now().format(DateTimeFormatter.ISO_DATE);
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDate(Date date, NdateFormatter pattern) {

		switch (pattern) {
		case DATE: {
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_DATE);
		}
		case DATE_TIME: {
			return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DATE_TIME_FORMATTER);
		}
		}
		return null;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDate(String date) {
		if (date.length() > 10) {
			return Date.from(LocalDateTime.parse(date, DATE_TIME_FORMATTER).atZone(ZoneId.systemDefault()).toInstant());
		} else {
			return Date.from(LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE)
					.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}

	}

}

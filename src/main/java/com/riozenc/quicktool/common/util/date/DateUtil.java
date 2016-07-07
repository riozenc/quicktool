/**
 *
 * @author Riozen
 * @date 2015-3-18 15:32:32
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riozenc.quicktool.common.util.date;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.riozenc.quicktool.annotation.DateAnnotation;
import com.riozenc.quicktool.common.util.annotation.FieldAnnotationUtil;

/**
 *
 * @author Riozenc
 */
public class DateUtil {
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static DateFormat YYYYMMDD_FORMAT = new SimpleDateFormat("yyyyMMdd");
	// private static DateFormat dateTimeFormatr = new
	// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	public static final String DATE = "yyyy-MM-dd";
	public static final String DATE_TIME = "yyyy-MM-dd hh24:mi:ss";

	/**
	 *
	 * @param input
	 * @return
	 */
	public static Date parseDate(String input) {
		try {
			return DATE_FORMAT.parse(input);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 *
	 * @param input
	 * @return
	 */
	public static Date parseDateTime(String input) {
		try {

			return DATE_TIME_FORMAT.parse(input);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(Object date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Object date) {
		return DATE_TIME_FORMAT.format(date);
	}

	public static String getYYYYMMDD(Object date) {
		return YYYYMMDD_FORMAT.format(date);
	}

	/**
	 * 获取日期格式
	 * 
	 * @param field
	 * @return
	 */
	public static String getFormat(Field field) {

		if (DateAnnotation.DATE_TYPE.DATE == FieldAnnotationUtil.getAnnotation(
				field, DateAnnotation.class, "value")) {
			return DATE;
		} else {
			return DATE_TIME;

		}
	}
}

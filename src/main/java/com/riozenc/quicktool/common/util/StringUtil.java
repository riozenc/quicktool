/**
 *
 * @author Riozen
 * @date 2015-3-18 9:30:04
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riozenc.quicktool.common.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Riozenc
 */
public class StringUtil extends StringUtils {

	private static final char SEPARATOR = '_';
	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 格式化字符串为首字母大写的形式(例:Xxx)
	 *
	 * @param name
	 * @return
	 */
	public static String fristToUpper(String name) {
		String str = null;

		if (name.length() <= 1) {
			str = name.toUpperCase();
		} else {
			str = name.substring(0, 1).toUpperCase() + name.substring(1);
		}

		return str;
	}

	/**
	 * 全部转换大写xxXxx转化为XX_XXX
	 * 
	 * @param input
	 * @return
	 */
	public static String allToUpper(String input) {
		StringBuffer result = new StringBuffer();
		char[] temp = input.toCharArray();
		int length = temp.length;
		for (int i = 0; i < length; i++) {
			if (Character.isUpperCase(temp[i])) {
				result.append("_").append(temp[i]);
			} else {
				result.append(Character.toUpperCase(temp[i]));
			}

		}
		return result.toString();
	}

	/**
	 * 字符串转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] getBytes(String str) {
		if (str != null) {
			try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 字节数组转换为字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String toString(byte[] bytes) {
		try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
	}

}

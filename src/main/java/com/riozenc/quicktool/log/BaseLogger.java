/**
 * @Title:BaseLogger.java
 * @Author:Riozenc
 * @Datetime:2015年10月31日 上午11:53:33
 * @Project:quicktool
 */
package com.riozenc.quicktool.log;

import com.riozenc.quicktool.common.util.log.DefaultLogWriteToFileUtil;

public abstract class BaseLogger {
	public static void log(String logPath, String message) {

		DefaultLogWriteToFileUtil.log(logPath, message, 1024);
	}
}

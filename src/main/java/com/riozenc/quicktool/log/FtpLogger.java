/**
 * @Title:FtpLogger.java
 * @author:Riozenc
 * @datetime:2015年6月6日 下午4:04:34
 */
package com.riozenc.quicktool.log;

import com.riozenc.quicktool.common.util.log.DefaultLogWriteToFileUtil;

public class FtpLogger {

	public static void log(String message) {
		DefaultLogWriteToFileUtil.log("ftp", message, 1024);
	}

}

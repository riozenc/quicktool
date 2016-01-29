/**
 * @Title:WebServiceLogger.java
 * @author:Riozenc
 * @datetime:2015年5月29日 下午9:31:24
 */
package com.riozenc.quicktool.log;

import com.riozenc.quicktool.common.util.log.DefaultLogWriteToFileUtil;

public class WebServiceLogger {

	public static void log(String message) {

		DefaultLogWriteToFileUtil.log("webservice/server", message, 1024);
	}

}

/**
 * Title:LogUtil.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.common.util.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

	public static Logger getLogger(String name) {
		if (null == name) {
			return LogManager.getLogger("");
		}
		return LogManager.getLogger(name);
	}

	public static Logger getLogger(LOG_TYPE type) {

		switch (type) {
		case DB:
			return LogManager.getLogger("DB");
		case IO:
			return LogManager.getLogger("IO");
		case ERROR:
			return LogManager.getLogger("ERROR");

		default:
			return LogManager.getLogger("default");
		}
	}

	public enum LOG_TYPE {
		DB, IO, ERROR, OTHER, WEBSERVICE, CACHE
	}

}

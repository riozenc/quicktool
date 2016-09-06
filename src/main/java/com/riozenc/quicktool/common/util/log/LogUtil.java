/**
 * Title:LogUtil.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.common.util.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {

	private static LOG_OUT_TYPE OUT_TYPE = LogUtil.LOG_OUT_TYPE.SYSTEM;

	public static void setLogOutType(LOG_OUT_TYPE LOG_OUT_TYPE) {
		OUT_TYPE = LOG_OUT_TYPE;
	}

	public static Logger getLogger(String name) {
		if (null == name) {
			return LogManager.getLogger("");
		}
		return LogManager.getLogger(name);
	}

	public static Logger getLogger(LOG_TYPE type) {

		// switch (type) {
		// case DB:
		// return LogManager.getLogger("DB");
		// case IO:
		// return LogManager.getLogger("IO");
		// case ERROR:
		// return LogManager.getLogger("ERROR");
		//
		// default:
		// return LogManager.getLogger("default");
		// }
		switch (type) {
		case DB:
			return LogggerContext.getLogger("DB");
		case IO:
			return LogggerContext.getLogger("IO");
		case ERROR:
			return LogggerContext.getLogger("ERROR");

		default:
			return LogggerContext.getLogger("default");
		}
	}

	public enum LOG_TYPE {
		DB, IO, ERROR, OTHER, WEBSERVICE, CACHE
	}

	public enum LOG_OUT_TYPE {
		FILE, SYSTEM
	}

	private static class LogggerContext extends LogManager {

		public static Logger getLogger(String name) {
			switch (OUT_TYPE) {
			case FILE:

				return LogManager.getLogger(name);
			case SYSTEM:
				return LogManager.getLogger("Console");
			}
			return null;

		}

	}

}

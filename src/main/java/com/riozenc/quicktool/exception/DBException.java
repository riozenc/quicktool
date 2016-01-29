/**
 * @Title:DBException.java
 * @author:Riozenc
 * @datetime:2015年5月27日 下午2:49:36
 */
package com.riozenc.quicktool.exception;

import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;

public abstract class DBException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -259713269898880583L;
	private static final Logger LOGGER = LogUtil.getLogger(LOG_TYPE.DB);

	protected Logger getLogger() {
		return LOGGER;
	}
}

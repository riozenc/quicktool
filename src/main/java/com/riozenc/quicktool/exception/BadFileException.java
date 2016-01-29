/**
 * @Title:BadFileException.java
 * @author:Riozenc
 * @datetime:2015年6月6日 下午4:01:33
 */
package com.riozenc.quicktool.exception;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;

/**
 * 坏文件异常，包括文件未找到
 * 
 * @author Riozenc
 *
 */
public class BadFileException extends RuntimeException {
	/**	
	 * 
	 */
	private static final long serialVersionUID = -2474892332926765711L;

	public BadFileException() {
		super();
	}

	public BadFileException(String message) {
		super(message);
		// 数据初始化日志记录

		LogUtil.getLogger(LOG_TYPE.IO).error(message + ExceptionLogUtil.log(this));

	}

	public BadFileException(Throwable cause) {
		super(cause);
	}

	public BadFileException(String message, Throwable cause) {
		super(message, cause);
	}
}

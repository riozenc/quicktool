/**
 * Title:DbException.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.exception;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;

public class LoginTimeOutException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585100168362430553L;

	public LoginTimeOutException() {
		super();
	}

	public LoginTimeOutException(String message) {
		super(message);
		LogUtil.getLogger(LOG_TYPE.OTHER).error(message + ExceptionLogUtil.log(this));
	}

	public LoginTimeOutException(Throwable cause) {
		super(cause);
	}

	public LoginTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}
}

/**
 * Title:DbException.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.exception;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;

public class DbInitException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5585100168362430553L;

	public DbInitException() {
		super();
	}

	public DbInitException(String message) {
		super(message);
		// 数据初始化日志记录
		LogUtil.getLogger(LOG_TYPE.DB).error(message+ExceptionLogUtil.log(this));
	}

	public DbInitException(Throwable cause) {
		super(cause);
	}

	public DbInitException(String message, Throwable cause) {
		super(message, cause);
	}
}

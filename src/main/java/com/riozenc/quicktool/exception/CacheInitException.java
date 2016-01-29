/**
 * @Title:CacheInitException.java
 * @author:Riozenc
 * @datetime:2015年6月4日 下午5:52:25
 */
package com.riozenc.quicktool.exception;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;

public class CacheInitException extends RuntimeException {
	public CacheInitException() {
		super();
	}

	public CacheInitException(String message) {
		super(message);
		// 数据初始化日志记录
		LogUtil.getLogger(LOG_TYPE.CACHE).error(message + ExceptionLogUtil.log(this));
	}

	public CacheInitException(Throwable cause) {
		super(cause);
	}

	public CacheInitException(String message, Throwable cause) {
		super(message, cause);
	}
}

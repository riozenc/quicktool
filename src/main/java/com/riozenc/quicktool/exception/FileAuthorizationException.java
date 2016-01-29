/**
 * @Title:FileAuthorizationException.java
 * @author:Riozenc
 * @datetime:2015年5月29日 下午8:17:02
 */
package com.riozenc.quicktool.exception;

public class FileAuthorizationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileAuthorizationException() {
		super();
	}

	public FileAuthorizationException(String message) {
		super(message);
	}

	public FileAuthorizationException(Throwable cause) {
		super(cause);
	}

	public FileAuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}
}

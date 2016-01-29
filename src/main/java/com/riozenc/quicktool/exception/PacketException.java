/**
 *	
 * @Title:PacketException.java
 * @author Riozen
 *	@date 2013-11-18 上午10:09:15
 *	
 */
package com.riozenc.quicktool.exception;

public class PacketException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PacketException() {
		super();
	}

	public PacketException(String message) {
		super(message);
	}

	public PacketException(Throwable cause) {
		super(cause);
	}

	public PacketException(String message, Throwable cause) {
		super(message, cause);
	}

}

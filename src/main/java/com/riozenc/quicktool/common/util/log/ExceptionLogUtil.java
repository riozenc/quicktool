/**
 * @Title:ExceptionLogUtil.java
 * @author:Riozenc
 * @datetime:2015年6月2日 下午8:12:36
 */
package com.riozenc.quicktool.common.util.log;

/**
 * 异常日志
 * 
 * @author Riozenc
 *
 */
public class ExceptionLogUtil {

	public static String log(RuntimeException runtimeException) {

		if (null != runtimeException.getCause()) {
			return log(runtimeException.getCause());
		} else
			return log(runtimeException.getStackTrace());
	}

	public static String log(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		if (null != e.getMessage()) {
			sb.append(e.getMessage()).append("\r\n");
		}
		sb.append(e.getClass().getName());
		for (StackTraceElement stackTraceElement : stackTraceElements) {
			sb.append("\r\n");
			sb.append("\tat ");
			sb.append(stackTraceElement.toString());
		}

		if (e.getCause() != null) {
			sb.append(log(e.getCause()));
		}

		// StackTraceElement[] stackTraceElements = e.getStackTrace();
		// if (null != e.getMessage()) {
		// sb.append(e.getMessage()).append("\r\n");
		// }
		// sb.append(e.getClass().getName());
		// for (StackTraceElement stackTraceElement : stackTraceElements) {
		// sb.append("\r\n");
		// sb.append("\tat ");
		// sb.append(stackTraceElement.toString());
		// }
		return sb.toString();
	}

	public static String log(StackTraceElement[] stackTraceElements) {
		StringBuffer sb = new StringBuffer();

		for (StackTraceElement stackTraceElement : stackTraceElements) {
			sb.append("\r\n");
			sb.append("\tat ");
			sb.append(stackTraceElement.toString());
		}
		return sb.toString();
	}

	public static String log(Throwable throwable) {

		StringBuffer sb = new StringBuffer();

		if (null != throwable) {
			sb.append("Caused by: ").append(throwable.getClass().getCanonicalName())
					.append(": " + throwable.getMessage() + "\n");
			while (throwable != null) {
				for (StackTraceElement ste : throwable.getStackTrace()) {
					sb.append(ste.getClassName() + "!" + ste.getMethodName() + "!" + ste.getFileName() + "!"
							+ ste.getLineNumber()+"\r\n");
				}
				throwable = throwable.getCause();
				if (throwable != null) {
					sb.append("Caused by: " + throwable.getClass().getCanonicalName() + " : " + throwable.getMessage());
				}
			}

		}
		return sb.toString();
	}
}

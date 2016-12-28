/**
 * @Project:quicktool
 * @Title:HttpUtils.java
 * @Author:Riozenc
 * @Datetime:2016年10月24日 下午9:13:42
 * 
 */
package com.riozenc.quicktool.common.util.http;

import javax.servlet.http.HttpServletRequest;

import com.riozenc.quicktool.common.util.StringUtils;

public class HttpUtils {
	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String remoteAddr = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		}
		if (!StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		}
		if (!StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

}

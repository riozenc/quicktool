/**
 * @Project:quicktool
 * @Title:HttpUtils.java
 * @Author:Riozenc
 * @Datetime:2016年10月24日 下午9:13:42
 * 
 */
package com.riozenc.quicktool.common.util.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

	/**
	 * post请求
	 * 
	 * @param urlPath
	 * @param request
	 * @param encode
	 * @param timeOutInSeconds
	 * @return
	 * @throws Exception
	 */
	public static String post(String urlPath, String request, String encode, int timeOutInSeconds) throws Exception {
		HttpURLConnection http = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		StringBuffer result = new StringBuffer();
		try {
			URL url = new URL(urlPath);
			http = (HttpURLConnection) url.openConnection();
			http.setDoInput(true);
			http.setDoOutput(true);
			http.setUseCaches(false);
			http.setConnectTimeout(timeOutInSeconds * 1000);// 设置连接超时
			// 如果在建立连接之前超时期满，则会引发一个
			// java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
			http.setReadTimeout(timeOutInSeconds * 1000);// 设置读取超时
			// 如果在数据可读取之前超时期满，则会引发一个
			// java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + encode);
			http.connect();

			outputStream = http.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(outputStream, encode);
			osw.write(request);
			osw.flush();
			osw.close();

			if (http.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
				return result.toString();
			} else {
				throw new RuntimeException("http read [" + http.getResponseCode() + "]");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (http != null)
				http.disconnect();
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

}

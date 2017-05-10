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
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
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
	 * get请求
	 * 
	 * @param url
	 * @param encode
	 * @param timeOutInSeconds
	 * @return
	 */
	public static String get(String url, String encode, int timeOutInSeconds) {

		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			// 建立实际的连接
			connection.connect();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;

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

	/**
	 * 
	 * @param urlPath
	 * @param request
	 * @param encode
	 * @param timeOutInSeconds
	 * @return
	 * @throws Exception
	 */
	public static String postSSL(String urlPath, String request, String encode, int timeOutInSeconds) throws Exception {
		HttpsURLConnection https = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		StringBuffer result = new StringBuffer();

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		try {
			URL url = new URL(urlPath);
			https = (HttpsURLConnection) url.openConnection();
			https.setSSLSocketFactory(sc.getSocketFactory());
			https.setHostnameVerifier(new TrustAnyHostnameVerifier());
			https.setRequestProperty("Content-Type", "application/json");
			https.setDoInput(true);
			https.setDoOutput(true);
			https.setUseCaches(false);
			https.setConnectTimeout(timeOutInSeconds * 1000);// 设置连接超时
			// java.net.SocketTimeoutException。超时时间为零表示无穷大超时。
			https.setReadTimeout(timeOutInSeconds * 1000);// 设置读取超时
			// java.net.SocketTimeoutException。超时时间为零表示无穷大超时。

			https.connect();

			outputStream = https.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(outputStream, encode);
			osw.write(request);
			osw.flush();
			osw.close();

			if (https.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(https.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result.append(line);
				}
				return result.toString();
			} else {
				throw new RuntimeException("https read [" + https.getResponseCode() + "]");
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (https != null)
				https.disconnect();
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}

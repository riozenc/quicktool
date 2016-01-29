/**
 * @Title:LocalLogUtil.java
 * @author:Riozenc
 * @datetime:2015年5月29日 下午8:06:15
 */
package com.riozenc.quicktool.common.util.log;

import java.io.File;
import java.io.IOException;

import com.riozenc.quicktool.exception.FileAuthorizationException;

public class LocalLogUtil {

	private static String BASE_PATH = System.getProperty("user.dir") + "/logs" + "/webservice/server";
	private static Long FILE_SIZE_LIMIT = 1024 * 1024 * 10L;
	private static Long FLAG = 1L;

	private static String LOG_DIRECTORY = "message";

	public static void setLogDirectory(String logDirectory) {
		LOG_DIRECTORY = logDirectory;
	}

	/**
	 * 获取下一个文件
	 */
	public static File getNextFile() {
		return getNextFile(BASE_PATH, FLAG);
	}

	/**
	 * 获取下一个文件
	 * 
	 * @param _fileName
	 * @param flag
	 */
	public static File getNextFile(String _fileName, Long flag) {

		File directory = new File(_fileName);

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new FileAuthorizationException("无法创建文件夹目录...");
			}
		}

		File file = null;
		boolean f = true;
		while (f) {
			file = new File(_fileName + "/" + LOG_DIRECTORY + "_" + flag);
			if (!file.exists()) {
				try {
					if (!file.createNewFile()) {
						throw new FileAuthorizationException("无法创建文件夹目录...");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new FileAuthorizationException("无法创建文件:" + file.getName() + "...");
				}

			}
			if (file.length() > FILE_SIZE_LIMIT) {
				flag += 1;
			} else {
				f = false;
			}
		}
		return file;
	}

}

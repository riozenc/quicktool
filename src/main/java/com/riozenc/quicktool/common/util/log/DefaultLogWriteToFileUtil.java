/**
 * @Title:DefaultLogWriteToFileUtil.java
 * @author:Riozenc
 * @datetime:2015年6月6日 下午4:23:01
 */
package com.riozenc.quicktool.common.util.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.exception.FileAuthorizationException;

public class DefaultLogWriteToFileUtil {

	private static String BASE_PATH = System.getProperty("user.dir");

	private static String ENCODING = "UTF-8";

	public static void log(String docName, String message, int cacheSize) {

		Long flag = 1L;
		FileChannel fileChannel = null;

		String _fileName = BASE_PATH + docName;

		File directory = new File(_fileName);

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new FileAuthorizationException("无法创建文件夹目录...");
			}
		} else {
			flag = directory.list().length == 0 ? 1L : directory.list().length;
		}

		File file = LocalLogUtil.getNextFile(_fileName, flag);

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			fileChannel = fileOutputStream.getChannel();

			ByteBuffer byteBuffer = ByteBuffer.allocate(cacheSize);
			byteBuffer.clear();
			try {
				byteBuffer.put(message.getBytes(ENCODING));
				byteBuffer.put("\r\n".getBytes(ENCODING));
				byteBuffer.flip();
				while (byteBuffer.hasRemaining()) {
					fileChannel.write(byteBuffer);
				}
				fileChannel.force(true);
				fileOutputStream.flush();
				fileChannel.close();
				fileOutputStream.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.getLogger(LOG_TYPE.ERROR).error(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.getLogger(LOG_TYPE.ERROR).error(e.getMessage());
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getLogger(LOG_TYPE.ERROR).error(e.getMessage());
		}

	}

}

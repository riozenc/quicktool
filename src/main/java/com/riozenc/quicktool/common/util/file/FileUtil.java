/**
 * @Title:FileUtil.java
 * @author:Riozenc
 * @datetime:2015年8月18日 下午3:58:29
 */
package com.riozenc.quicktool.common.util.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.riozenc.quicktool.common.util.file.filter.ClassFileFilter;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.exception.BadFileException;

public class FileUtil {

	public static File createFile(String docPath, String fileName) {
		File doc = new File(docPath);
		File file = null;
		if (!doc.exists()) {
			doc.mkdirs();
		}
		if (doc.isDirectory()) {
			file = new File(docPath + "\\" + fileName);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.getLogger(LOG_TYPE.IO).error(e);
			}
		}
		return file;
	}

	/**
	 * 校验文件合理性
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileIsValid(File file) {
		// 判断文件是否存在
		if (file.exists()) {
			// 判断文件是否是目录
			if (!file.isDirectory()) {
				// 文件
				if (file.isFile()) {
					return true;
				}
			}

		} else {

			throw new BadFileException(file.getPath() + "文件不存在。。。");
		}
		return false;
	}

	/**
	 * 是否为目录
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isDirectory(File file) {

		// 判断文件是否存在
		if (file.exists()) {
			// 判断文件是否是目录
			if (file.isDirectory()) {
				// 文档目录
				return true;
			} else {
				return false;
			}

		} else {
			throw new BadFileException(file.getPath() + "文件不存在。。。");
		}
	}

	/**
	 * 获取指定目录下的执行文件
	 * 
	 * @param filePath
	 * @param type
	 * @return
	 */
	public static Map<String, File> getFilesByPath(String filePath, String type) {
		Map<String, File> map = new HashMap<String, File>();
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return null;
		}

		// 在给定的目录下找到所有的文件，并且进行条件过滤
		File[] dirFiles = dir.listFiles(ClassFileFilter.getFilter(false, "xml"));
		for (File file : dirFiles) {
			System.out.println(file.getName());
			map.put(file.getName(), file);
		}
		return map;
	}

	public static void main(String[] args) {
		getFilesByPath("C:\\Users\\rioze\\Desktop\\xml", "xml");

	}

}

/**
 * @Project:sh
 * @Title:XmlParseUtils.java
 * @Author:Riozenc
 * @Datetime:2016年7月6日 下午2:36:44
 */
package com.riozenc.quicktool.common.util.xml;

import java.io.File;
import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlParseUtils {

	public static Element parse(String xmlPath) throws FileNotFoundException, DocumentException {
		String projectPath = System.getProperty("user.dir");
		// File file = new File(projectPath + File.separator + "config.xml");
		File file = new File(xmlPath);
		if (!file.exists()) {
			throw new FileNotFoundException(xmlPath + " is not found...");
		} else {
			System.out.println("开始读取config.xml");
		}
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(file);

		Element element = document.getRootElement();
		return element;

	}
}

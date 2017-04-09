/**
 * @Project:sh
 * @Title:XmlParseUtils.java
 * @Author:Riozenc
 * @Datetime:2016年7月6日 下午2:36:44
 */
package com.riozenc.quicktool.common.util.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.riozenc.quicktool.common.util.ClassUtils;

public class XmlParseUtils {

	public static Element parse(String xmlPath) throws DocumentException {

		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(ClassUtils.getDefaultClassLoader().getResourceAsStream(xmlPath));

		Element element = document.getRootElement();
		return element;
	}

}

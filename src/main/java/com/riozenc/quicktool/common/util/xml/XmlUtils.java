/**
 * Title:XmlUtils.java
 * Author:riozenc
 * Datetime:2017年6月14日 下午5:39:54
**/
package com.riozenc.quicktool.common.util.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlUtils {

	public static String string2xml() {

		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement("employee");
		Element empName = rootElement.addElement("empname");
		empName.setText("@残缺的孤独");
		Element empAge = rootElement.addElement("age");
		empAge.setText("25");
		Element empTitle = rootElement.addElement("title");
		empTitle.setText("软件开发工程师");
		
		System.out.println(document.asXML());
		System.out.println(document.asXML().getBytes());
		System.out.println(new String(document.asXML().getBytes()));
		return null;
	}
	
	public static void main(String[] args) {
		string2xml();
	}
}

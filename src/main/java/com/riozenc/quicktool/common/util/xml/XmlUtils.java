/**
 * Title:XmlUtils.java
 * Author:riozenc
 * Datetime:2017年6月14日 下午5:39:54
**/
package com.riozenc.quicktool.common.util.xml;

import java.lang.reflect.Field;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.riozenc.quicktool.common.util.reflect.ReflectUtil;

public class XmlUtils {

	public static String object2xml(Object object) {
		if (object == null)
			throw new NullPointerException("参数不能为null...");
		Field[] fields = object.getClass().getDeclaredFields();
		String className = object.getClass().getSimpleName();
		if (fields == null || fields.length == 0)
			throw new RuntimeException(object.getClass() + "没有属性...");
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement(className);
		for (Field field : fields) {
			Object value = ReflectUtil.getFieldValue(object, field.getName());
			if (value != null) {
				Element element = rootElement.addElement(field.getName());
				try {
					element.setText((String) ReflectUtil.typeFormat(String.class, value));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					element.setText((String) value);
				}
			}
		}
		return document.getRootElement().asXML();
	}

}

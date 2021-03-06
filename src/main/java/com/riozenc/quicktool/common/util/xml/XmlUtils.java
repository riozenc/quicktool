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
import com.riozenc.quicktool.mybatis.MybatisEntity;

public class XmlUtils {

	public static String object2xml(Object object, boolean isSuper) {
		Field[] fields = null;
		if (isSuper) {
			fields = ReflectUtil.getFields(object.getClass());
		} else {
			fields = object.getClass().getDeclaredFields();
		}
		return field2Xml(object, fields);
	}

	public static String object2xml(Object object) {
		return object2xml(object, false);
	}

	private static String field2Xml(Object object, Field[] fields) {
		if (object == null)
			throw new NullPointerException("参数不能为null...");

		String className = object.getClass().getSimpleName().toLowerCase();
		if (fields == null || fields.length == 0)
			throw new RuntimeException(object.getClass() + "没有属性...");
		Document document = DocumentHelper.createDocument();
		Element rootElement = document.addElement(className);
		for (Field field : fields) {
			Object value = ReflectUtil.getFieldValue(object, field.getName());
			if (value != null) {
				Element element = rootElement.addElement(field.getName());
				if (value instanceof MybatisEntity) {
					object2Element(value, element);
				} else {
					try {
						element.setText((String) ReflectUtil.typeFormat(String.class, value));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						element.setText((String) value);
					}
				}
			}
		}
		return document.getRootElement().asXML();
	}

	private static void object2Element(Object object, Element rootElement) {
		Field[] fields = ReflectUtil.getFields(object.getClass());
		if (fields == null || fields.length == 0)
			throw new RuntimeException(object.getClass() + "没有属性...");
		for (Field field : fields) {
			Object value = ReflectUtil.getFieldValue(object, field.getName());
			if (value != null) {
				Element element = rootElement.addElement(field.getName());
				if (value instanceof MybatisEntity) {
					object2Element(value, element);
				} else {
					try {
						element.setText((String) ReflectUtil.typeFormat(String.class, value));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						element.setText((String) value);
					}
				}
			}
		}
	}

}

/**
 * @Project:quicktool
 * @Title:ElementCaseUtil.java
 * @Author:Riozenc
 * @Datetime:2016年7月6日 下午5:40:12
 */
package com.riozenc.quicktool.common.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.dom4j.Element;

public class ElementCaseUtil {
	public static <T> T changeByElement(Element element, Class<T> clazz) {

		T obj = null;
		try {
			obj = clazz.newInstance();

			Field field = null;
			Method method;
			Object result = null;
			String name = null;
			Element childElement = null;

			clazz.getFields();

			Iterator<Element> iterator = element.elementIterator();
			while (iterator.hasNext()) {
				childElement = iterator.next();
				name = childElement.getName();
				result = ReflectUtil.typeFormat(field.getType(), childElement.getStringValue());

				field = clazz.getField(name);
				if (null == field) {
					continue;
				}

				method = clazz.getMethod(MethodGen.generateMethodName(MethodGen.METHOD_TYPE.set, name),
						field.getType());
				method.invoke(obj, new Object[] { result });

			}

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;

	}
}

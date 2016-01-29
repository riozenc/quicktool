/**
 * Title:FieldAnnotationUtil.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.common.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FieldAnnotationUtil {

	public static Object getAnnotation(Field field,
			Class<? extends Annotation> clazz) {
		return getAnnotation(field, clazz, null);
	}

	public static Object getAnnotation(Field field,
			Class<? extends Annotation> clazz, String methodName) {
		if (null == methodName) {
			methodName = "value";
		}
		return reflectValue(field, clazz, methodName);
	}

	private static Object reflectValue(Field field,
			Class<? extends Annotation> clazz, String methodName) {
		Annotation temp = field.getAnnotation(clazz);

		Method method = null;
		if (null == temp) {
			return null;
		}

		try {
			method = clazz.getDeclaredMethod(methodName, null);
			return method.invoke(temp, new Object[] {});
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

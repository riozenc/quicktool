/**
 *
 * @author Riozen
 * @date 2015-4-3 9:25:06
 *
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riozenc.quicktool.common.util.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.riozenc.quicktool.cache.reflect.ClassCache;
import com.riozenc.quicktool.cache.reflect.entity.ClassEntity;

/**
 *
 * @author Riozenc
 */
public class ObjectToStringUtil {

	public static String execute(Object obj) {
		Class<?> clazz = obj.getClass();
		ClassEntity classEntity = ClassCache.getClassEntity(clazz);

		StringBuffer sb = new StringBuffer("对象类型:" + clazz.getSimpleName()
				+ ",值:[");
		Field[] fields = classEntity.getFields();
		String fieldName = null;
		Class<?> fieldType = null;
		Method method = null;
		Object result = null;

		AccessibleObject.setAccessible(fields, true);// 优化反射

		try {

			for (Field field : fields) {
				fieldName = field.getName();
				fieldType=field.getType();
				method = classEntity.getMethodMap().get(
						MethodGen.generateMethodName(MethodGen.METHOD_TYPE.get, fieldName));
				result = method.invoke(obj, new Object[] {});
				sb.append(fieldName);
				sb.append("=" + result + "(" + fieldType.getName() + ")" + ",");
			}

		} catch (IllegalArgumentException ex) {
			Logger.getLogger(ObjectToStringUtil.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(ObjectToStringUtil.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(ObjectToStringUtil.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(ObjectToStringUtil.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		sb.append("]");

		return sb.toString();
	}
}

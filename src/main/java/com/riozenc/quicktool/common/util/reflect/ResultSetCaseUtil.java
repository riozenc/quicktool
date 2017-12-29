/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.riozenc.quicktool.common.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.riozenc.quicktool.common.util.StringUtils;

/**
 *
 * @author Riozenc
 */
public class ResultSetCaseUtil {

	

	public static void main(String[] args) {
		System.out.println(StringUtils.h2s("qysx_id"));
	}

	/**
	 * 100w次转换需要30秒
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * @param resultSet
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> change(ResultSet resultSet, Class<T> clazz) {
		T obj = null;

		List<T> list = new ArrayList<T>();

		int i = 0;

		// ClassEntity classEntity = ClassCache.getClassEntity(clazz);

		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();

			Field field = null;
			Method method;
			Object result = null;
			String temp = null;

			while (resultSet.next()) {
				i = 0;
				obj = clazz.newInstance();
				for (; i < numberOfColumns; i++) {
					temp = StringUtils.h2s(metaData.getColumnLabel(i + 1));
					//
					// field = classEntity.getFieldMap().get(temp);

					try {
						field = clazz.getDeclaredField(temp);
					} catch (NoSuchFieldException e) {
						continue;
					}
					if (null == field) {
						continue;
					}
					result = ReflectUtil.typeFormat(field.getType(), resultSet.getObject(i + 1));

					method = clazz.getMethod(MethodGen.generateMethodName(MethodGen.METHOD_TYPE.set, temp),
							field.getType());

					method.invoke(obj, new Object[] { result });

				}
				list.add(obj);
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

		return list;
	}
}

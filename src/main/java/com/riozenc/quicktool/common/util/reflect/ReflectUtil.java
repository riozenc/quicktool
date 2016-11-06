/**
 *	
 * @Title:ReflectUtil.java
 * @author Riozen
 *	@date 2013-11-21 下午3:53:24
 *	
 */
package com.riozenc.quicktool.common.util.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.riozenc.quicktool.common.util.date.DateUtil;
import com.riozenc.quicktool.common.util.reflect.MethodGen.METHOD_TYPE;

public class ReflectUtil {

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		if (null == obj) {
			throw new RuntimeException("object can't be null");
		}

		if (null == fieldName || "".equals(fieldName.trim())) {
			throw new RuntimeException("fieldName can't be blank");
		}
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {// NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Method getAccessibleMethod(final Object obj, final String fieldName,
			final MethodGen.METHOD_TYPE methodType, final Class<?>... parameterTypes) {
		if (null == obj) {
			throw new RuntimeException("object can't be null");
		}
		if (null == fieldName || "".equals(fieldName.trim())) {
			throw new RuntimeException("fieldName can't be blank");
		}

		if (null == methodType || "".equals(methodType.name().trim())) {
			throw new RuntimeException("methodType can't be blank");
		}

		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				Method method = superClass.getDeclaredMethod(MethodGen.generateMethodName(methodType, fieldName),
						parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {// NOSONAR
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的成员变量为public,尽量不调用实际改动的语句,避免JDK的SecurityManager.
	 */
	private static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的方法为public,尽量不调用实际改动的语句,避免JDK的SecurityManager.
	 */
	private static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(method.getModifiers())) && !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 通过set方法设置对象属性值, 无视private/protected修饰符, 经过setter函数.
	 */
	public static Object setValue(final Object obj, final String fieldName, final Object... values) {
		Object result = null;
		Class<?> paramType = null;
		Method method = null;
		Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			Class<?>[] os = { null };

			method = getAccessibleMethod(obj, fieldName, METHOD_TYPE.get, os);
			if (method == null) {
				method = getAccessibleMethod(obj, fieldName, METHOD_TYPE.get, null);
			}
			paramType = method.getReturnType();

		} else {
			paramType = field.getType();
		}
		method = getAccessibleMethod(obj, fieldName, METHOD_TYPE.set, paramType);
		try {

			result = method.invoke(obj, typeFormat(paramType, values));
		} catch (IllegalAccessException e) {
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
		return result;
	}

	/**
	 * 通过方法名直接获取值
	 * 
	 * @param obj
	 * @param methodName
	 * @return
	 */
	public static Object getValue(Object obj, String methodName) {
		Object result = null;
		Method method = null;
		try {

			method = obj.getClass().getDeclaredMethod(methodName, null);
			result = method.invoke(obj, new Object[] {});

		} catch (IllegalAccessException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE, null, ex);
		}
		return result;

	}

	/**
	 * 类型转换
	 *
	 * @param clazz
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Object[] typeFormat(Class<?> clazz, Object... values) throws Exception {
		Object[] objects = new Object[values.length];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = typeFormat(clazz, values[i]);
		}
		return objects;
	}

	/**
	 * 类型转换
	 *
	 * @param clazz
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Object typeFormat(Class<?> clazz, Object value) throws Exception {

		if (String.class.equals(clazz)) {
			if (null == value || "".equals(value)) {
				return null;
			} else {
				if (Date.class.equals(value.getClass())) {
					try {
						return DateUtil.formatDateTime(value);
					} catch (Exception ex) {
						ex.printStackTrace();
						return DateUtil.formatDate(value);
					}
				} else {
					return value.toString();
				}
			}
		} else {
			if (value == null || "".equals(value.toString().trim())) {
				if (long.class.equals(clazz)) {
					return 0;
				} else if (Long.class.equals(clazz)) {
					return null;
				} else if (short.class.equals(clazz)) {
					return 0;
				} else if (Short.class.equals(clazz)) {
					return null;
				} else if (double.class.equals(clazz)) {
					return 0.0;
				} else if (Double.class.equals(clazz)) {
					return null;
				} else if (BigDecimal.class.equals(clazz)) {
					return null;
				} else if (int.class.equals(clazz)) {
					return 0;
				} else if (Integer.class.equals(clazz)) {
					return null;
				} else if (float.class.equals(clazz)) {
					return 0.0;
				} else if (Float.class.equals(clazz)) {
					return null;
				} else if (boolean.class.equals(clazz)) {
					return false;
				} else if (Boolean.class.equals(clazz)) {
					return null;
				} else if (byte.class.equals(clazz)) {
					return 0;
				} else if (Byte.class.equals(clazz)) {
					return null;
				} else if (Date.class.equals(clazz)) {
					return null;
				} else {
					throw new Exception("value为null或\"\"找不到匹配的类型:" + clazz.getName());
				}
			} else if (Long.class.equals(clazz) || long.class.equals(clazz)) {
				return new Long(value.toString());
			} else if (Short.class.equals(clazz) || short.class.equals(clazz)) {
				return new Short(value.toString());
			} else if (Double.class.equals(clazz) || double.class.equals(clazz)) {
				return new Double(value.toString());
			} else if (BigDecimal.class.equals(clazz)) {
				return new BigDecimal(value.toString());
			} else if (Integer.class.equals(clazz) || int.class.equals(clazz)) {
				return new Integer(value.toString());
			} else if (Float.class.equals(clazz) || float.class.equals(clazz)) {
				return new Float(value.toString());
			} else if (Boolean.class.equals(clazz) || boolean.class.equals(clazz)) {
				return new Boolean(value.toString());
			} else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
				return new Byte(value.toString());
			} else if (Date.class.equals(clazz)) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.toString());
				} catch (Exception ex) {
					return new SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
				}
			} else {
				throw new Exception("没有匹配的类型:" + clazz.getName());
			}
		}
	}

}

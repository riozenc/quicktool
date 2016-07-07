/**
 *	
 * @Title:ReflectUtil.java
 * @author Riozen
 *	@date 2013-11-21 下午3:53:24
 *	
 */
package com.riozenc.quicktool.common.util.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.riozenc.quicktool.common.util.date.DateUtil;

public class ReflectUtil {

	public static Object getValue(Object obj, String methodName) {
		Object result = null;
		Method method = null;
		try {

			method = obj.getClass().getDeclaredMethod(methodName, null);
			result = method.invoke(obj, new Object[] {});

		} catch (IllegalAccessException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (SecurityException ex) {
			Logger.getLogger(ReflectUtil.class.getName()).log(Level.SEVERE,
					null, ex);
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
	public static Object typeFormat(Class<?> clazz, Object value)
			throws Exception {
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
					throw new Exception("value为null或\"\"找不到匹配的类型:"
							+ clazz.getName());
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
			} else if (Boolean.class.equals(clazz)
					|| boolean.class.equals(clazz)) {
				return new Boolean(value.toString());
			} else if (Byte.class.equals(clazz) || byte.class.equals(clazz)) {
				return new Byte(value.toString());
			} else if (Date.class.equals(clazz)) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.parse(value.toString());
				} catch (Exception ex) {
					return new SimpleDateFormat("yyyy-MM-dd").parse(value
							.toString());
				}
			} else {
				throw new Exception("没有匹配的类型:" + clazz.getName());
			}
		}
	}
}

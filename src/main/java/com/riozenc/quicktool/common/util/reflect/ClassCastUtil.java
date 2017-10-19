/**
 *	
 * @Title:ClassCastUtil.java
 * @author Riozen
 *	@date 2013-11-20 下午2:12:44
 *	
 */
package com.riozenc.quicktool.common.util.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.common.util.StringUtils;

public class ClassCastUtil {

	private static final Logger LOGGER = LogManager.getLogger(ClassCastUtil.class);

	// public static Object xx(Object srcObj, Class<?> tarClazz){
	// Object obj=null;
	// if(srcObj instanceof Collection){
	// Collection<?> collection = (Collection<?>) srcObj;
	// Iterator<?> iterator = collection.iterator();
	// List<Object> result = new LinkedList<Object>();
	// while(iterator.hasNext()){
	// Object o = iterator.next();
	// result.add(cast(o,tarClazz));
	// }
	// obj = result;
	// }else{
	// obj = cast(srcObj,tarClazz);
	//
	// }
	// return obj;
	// }
	public static Object cast(Object srcObj, Class<?> tarClazz) {
		// StringBuffer sb = new StringBuffer();
		Object tarObj = null;
		if (null != srcObj) {
			try {
				Class<? extends Object> srcClazz = srcObj.getClass();
				tarObj = tarClazz.newInstance();
				Field[] srcFields = srcClazz.getDeclaredFields();
				Field[] tarFields = tarClazz.getDeclaredFields();
				AccessibleObject.setAccessible(srcFields, true);
				AccessibleObject.setAccessible(tarFields, true);

				String srcFieldName = null;
				String tarFieldName = null;
				Class<? extends Object> tarFieldType = null;
				String upperName = null;

				Method method;
				Object result = null;

				for (Field tarField : tarFields) {
					tarFieldName = tarField.getName();

					for (Field srcField : srcFields) {

						srcFieldName = srcField.getName();

						if (tarFieldName.equals(srcFieldName)) {
							upperName = StringUtils.fristToUpper(tarFieldName);

							tarFieldType = tarField.getType();
							method = srcClazz.getDeclaredMethod("get" + upperName, null);
							result = method.invoke(srcObj, new Object[] {});
							result = ReflectUtil.typeFormat(tarFieldType, result);
							method = tarClazz.getMethod("set" + upperName, new Class[] { tarFieldType });
							method.invoke(tarObj, new Object[] { result });
							// sb.append(tarFieldName)
							// .append("=")
							// .append(result == null ? "null" : result
							// .toString()).append(",");
							break;
						}

					}
				}
				// LOGGER.info(sb.toString().substring(0,
				// sb.toString().length() - 1));
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
		} else {
			LOGGER.info("未查询到数据....");
		}

		return tarObj;
	}

	public static <T> List<?> cast(List<?> list, Class<?> tarClazz) {

		List<Object> result = new LinkedList<Object>();
		if (null != list && list.size() > 0) {
			for (Object obj : list) {
				result.add(cast(obj, tarClazz));
			}
		}
		return result;
	}

}

/**
 *	
 * @Title:ClassCastUtil.java
 * @author Riozen
 *	@date 2013-11-20 下午2:12:44
 *	
 */
package com.riozenc.quicktool.common.util.reflect;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Deprecated
public class ClassCastUtil {

	private static Logger log = LoggerFactory.getLogger(ClassCastUtil.class);

	public static <T> T cast(Object srcObj, Class<T> tarClazz) {
		
		T tarObj = null;
		if (null != srcObj) {
			try {
				Class<?> srcClazz = srcObj.getClass();
				tarObj = tarClazz.newInstance();
				Field[] srcFields = ReflectUtil.getFields(srcClazz);
				Field[] tarFields = ReflectUtil.getFields(tarClazz);

				String srcFieldName = null;
				String tarFieldName = null;
				Class<?> tarFieldType = null;
				String upperName = null;

				Method method;
				Object result = null;

				for (Field tarField : tarFields) {
					tarFieldName = tarField.getName();

					for (Field srcField : srcFields) {

						srcFieldName = srcField.getName();

						if (tarFieldName.equals(srcFieldName)) {
							
							ReflectUtil.setFieldValue(tarObj, tarFieldName, ReflectUtil.getFieldValue(srcObj, srcFieldName));
							
							
//							upperName = StringUtils.fristToUpper(tarFieldName);
//							tarFieldType = tarField.getType();
//							method = srcClazz.getDeclaredMethod("get" + upperName, null);
//							result = method.invoke(srcObj, new Object[] {});
//							result = ReflectUtil.typeFormat(tarFieldType, result);
//							method = tarClazz.getMethod("set" + upperName, new Class[] { tarFieldType });
//							method.invoke(tarObj, new Object[] { result });
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
			} 
//			catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
			catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
//			catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			log.info("未查询到数据....");
		}

		return tarObj;
	}

	public static <T, F> List<T> cast(List<F> list, Class<T> tarClazz) {
		List<T> result = new LinkedList<>();
		if (null != list && list.size() > 0) {
			for (F obj : list) {
				result.add(cast(obj, tarClazz));
			}
		}
		return result;
	}

}

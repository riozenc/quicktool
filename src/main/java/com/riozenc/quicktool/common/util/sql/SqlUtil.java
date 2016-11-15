/**
 *	
 * @Title:SqlTools.java
 * @author Riozen
 *	@date 2015-3-18 上午9:25:28
 *	
 */
package com.riozenc.quicktool.common.util.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.riozenc.quicktool.annotation.DateAnnotation;
import com.riozenc.quicktool.cache.reflect.ClassCache;
import com.riozenc.quicktool.cache.reflect.entity.ClassEntity;
import com.riozenc.quicktool.common.util.StringUtils;
import com.riozenc.quicktool.common.util.annotation.AnnotationUtil;
import com.riozenc.quicktool.common.util.date.DateUtil;
import com.riozenc.quicktool.common.util.reflect.MethodGen;

public class SqlUtil {

	private static String INSERT_INTO = "insert into ";
	private static String SELECT = "select ";
	private static String FROM = " from ";
	private static String UPDATE = "update ";
	private static String VALUES = " values";
	private static String SET = " set ";
	private static String WHERE = " where ";
	private static String AND = " and ";
	private static String LEFT_BRACKET = "(";
	private static String RIGHT_BRACKET = ")";
	private static Object objs[] = {};

	/**
	 *
	 * @param tableName
	 * @param obj
	 * @return
	 */
	public static String convertInsertSQL(String tableName, Object obj) {
		StringBuffer sb = new StringBuffer();

		StringBuffer columns = new StringBuffer();
		StringBuffer values = new StringBuffer();

		sb.append(INSERT_INTO);
		sb.append(tableName);

		Field[] fields = obj.getClass().getDeclaredFields();

		ClassEntity classEntity = ClassCache.getClassEntity(obj.getClass());

		// Field[] fields = classEntity.getFields();
		fields = classEntity.getFields();

		Method method = null;
		Object value = null;
		for (Field field : fields) {

			try {
				// method =
				// obj.getClass().getDeclaredMethod(MethodGen.generateMethodName("get",
				// field.getName()), null);
				method = classEntity.getMethodMap().get(
						MethodGen.generateMethodName(MethodGen.METHOD_TYPE.get,
								field.getName()));

				value = method.invoke(obj, new Object[] {});
				if (value == null) {
					continue;
				}
				columns.append(StringUtils.allToUpper(field.getName()));

				values.append(genSqlValue(field, value));

				columns.append(",");
				values.append(",");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		columns.deleteCharAt(columns.length() - 1);
		values.deleteCharAt(values.length() - 1);
		sb.append(LEFT_BRACKET).append(columns).append(RIGHT_BRACKET);
		sb.append(VALUES).append(LEFT_BRACKET);
		sb.append(values).append(RIGHT_BRACKET);
		System.out.println("SqlTools---------------------------------");
		System.out.println(sb);
		return sb.toString();
	}

	/**
	 *
	 * @param tableName
	 * @param obj
	 * @return
	 */
	public static String convertUpdateSQL(String tableName, Object obj) {
		StringBuffer sb = new StringBuffer();

		sb.append(UPDATE);
		sb.append(tableName);
		sb.append(SET);

		ClassEntity classEntity = ClassCache.getClassEntity(obj.getClass());
		Field[] fields = classEntity.getFields();

		Method method = null;
		Object value = null;
		for (Field field : fields) {
			try {
				// method =
				// obj.getClass().getDeclaredMethod(MethodGen.generateMethodName("get",
				// field.getName()), null);
				method = classEntity.getMethodMap().get(
						MethodGen.generateMethodName(MethodGen.METHOD_TYPE.get,
								field.getName()));
				value = method.invoke(obj, new Object[] {});
				if (value == null) {
					continue;
				}
				sb.append(StringUtils.allToUpper(field.getName()));
				sb.append("=");

				sb.append(genSqlValue(field, value));

				sb.append(",");
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(WHERE);

		return sb.toString();
	}

	public static String convertFindSQL(String tableName, Object obj) {
		StringBuffer sb = new StringBuffer();
		StringBuffer values = new StringBuffer();

		sb.append(SELECT);

		Class<?> clazz = obj.getClass();

		ClassEntity classEntity = ClassCache.getClassEntity(clazz);

		Field[] fields = classEntity.getFields();
		Class<?> fieldType = null;
		String fieldName = null;
		Object result = null;
		Method method = null;
		try {
			for (Field field : fields) {
				fieldName = field.getName();
				fieldType = field.getType();

				if (Date.class == fieldType) {
					// TO_CHAR(newdate,’yyyy-mm-dd’)
					// select to_char(SAVEDATETIME,'yyyymmdd') SAVEDATETIME from
					// G_SUBS where subs_id = 159;
					sb.append("TO_CHAR(")
							.append(StringUtils.allToUpper(fieldName))
							.append(",'").append(DateUtil.getFormat(field))
							.append("') as ")
							.append(StringUtils.allToUpper(fieldName))
							.append(",");
					// sb.append(StringUtil.allToUpper(fieldName)).append(",");
				} else {
					sb.append(StringUtils.allToUpper(fieldName)).append(",");
				}

				method = classEntity.getMethodMap().get(
						MethodGen.generateMethodName(MethodGen.METHOD_TYPE.get,
								fieldName));
				// method =
				// clazz.getDeclaredMethod(MethodGen.generateMethodName("get",
				// fieldName), null);
				result = method.invoke(obj, objs);
				if (null == result) {

					continue;
				}
				values.append(StringUtils.allToUpper(fieldName)).append("=")
						.append(result);

				values.append(AND);

			}

		} catch (IllegalAccessException ex) {
			Logger.getLogger(SqlUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(SqlUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(SqlUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (SecurityException ex) {
			Logger.getLogger(SqlUtil.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(FROM).append(tableName);
		if (values.length() > 0) {
			values.setLength(values.length() - AND.length());
			sb.append(WHERE).append(values);
		}

		// sb.deleteCharAt(sb.length()-AND.length());

		return sb.toString();
	}

	/**
	 *
	 * @param tableName
	 * @param obj
	 * @return
	 */
	public static String genSQL(String tableName, Class<?> clazz) {
		Field[] fields = ClassCache.getClassEntity(clazz).getFields();
		StringBuffer result = new StringBuffer();
		result.append(INSERT_INTO);
		result.append(tableName);
		result.append(genColumnField(fields));
		result.append(VALUES).append(LEFT_BRACKET);
		int length = fields.length;
		for (int i = 0; i < length; i++) {
			result.append("?");
			if (i != length - 1) {
				result.append(",");
			}
		}
		result.append(RIGHT_BRACKET);
		return result.toString();
	}

	/**
	 *
	 * @param obj
	 * @return
	 */
	private static String genColumnField(Field... fields) {
		StringBuffer result = new StringBuffer();
		result.append("(");
		int fieldSize = fields.length;
		for (int i = 0; i < fieldSize; i++) {
			result.append(StringUtils.allToUpper((fields[i].getName())));
			if (i != fieldSize - 1) {
				result.append(",");
			}
		}
		result.append(")");
		return result.toString();
	}

	/**
	 * 生成sql语句拼装值
	 * 
	 * @param fieldType
	 * @param value
	 * @return
	 */
	public static Object genSqlValue(Field field, Object value) {
		if (String.class.equals(field.getType())) {
			return "'" + value + "'";
		} else if (Date.class.equals(field.getType())) {
			// to_date('2015-03-25 14:16:21','YYYY-MM-DD hh24:mi:ss')

			Object temp = AnnotationUtil.getAnnotationValue(field,
					DateAnnotation.class, "value");

			if (DateAnnotation.DATE_TYPE.DATE == temp) {
				return "to_date('" + DateUtil.formatDate(value) + "','"
						+ DateUtil.DATE + "')";
			}
			{
				return "to_date('" + DateUtil.formatDateTime(value)
						+ "','" + DateUtil.DATE_TIME + "')";
			}
			// DateAnnotation da = field.getAnnotation(DateAnnotation.class);
			// if (null == da) {
			// return "to_date('" + DateUtil.formatDateTime((Date) value) +
			// "','YYYY-MM-DD hh24:mi:ss')";
			// } else {
			// if (DateAnnotation.DATE_TYPE.DATE == da.value()) {
			// return "to_date('" + DateUtil.formatDateTime((Date) value) +
			// "','YYYY-MM-DD')";
			// }
			// return "to_date('" + DateUtil.formatDateTime((Date) value) +
			// "','YYYY-MM-DD hh24:mi:ss')";
			// }

		} else {
			return value;
		}
	}
}

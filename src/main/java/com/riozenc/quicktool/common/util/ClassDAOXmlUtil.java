/**
 * @Title:ClassDAOXmlUtil.java
 * @author:Riozenc
 * @datetime:2015年6月9日 上午9:15:05
 */
package com.riozenc.quicktool.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.riozenc.quicktool.annotation.TablePrimaryKey;
import com.riozenc.quicktool.common.util.file.FileIoUtil;
import com.riozenc.quicktool.common.util.file.FileUtil;

public class ClassDAOXmlUtil {

	public static String getInsert(Class<?> clazz) {
		StringBuffer sb = new StringBuffer();

		Field[] fields = clazz.getDeclaredFields();

		sb.append("(");
		for (Field field : fields) {
			sb.append("#{" + field.getName() + "},");
		}
		sb.setLength(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	public static String getUpdate(Class<?> clazz) {
		StringBuffer sb = new StringBuffer();

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			if (field.getAnnotation(TablePrimaryKey.class) != null) {
				continue;
			}
			sb.append("<if test=\"" + field.getName() + " !=null\">").append("\n");
			sb.append(StringUtils.allToUpper(field.getName()) + " = #{" + field.getName() + "},").append("\n");
			sb.append("</if>").append("\n");
		}
		return sb.toString();
	}

	public static void getFindByWhere(Class<?> clazz) {

		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {
			System.out.println("<if test=\"" + field.getName() + " !=null\">");
			System.out.println("and " + field.getName().toUpperCase() + " = #{" + field.getName() + "}");
			System.out.println("</if>");

		}
	}

	public static String getColumns(Class<?> clazz) {
		StringBuffer sb = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();

		for (Field field : fields) {

			sb.append(StringUtils.allToUpper(field.getName())).append(",");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public static List<String> getPrimaryKeys(Class<?> clazz) {
		List<String> list = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();

		Object value = null;
		for (Field field : fields) {

			value = field.getAnnotation(TablePrimaryKey.class);
			if (null != value) {
				list.add(field.getName());
			}
		}
		return list;
	}

	private static String dynamicSqlFormat(String fieldName, boolean isAnd) {
		return "<if test=\"" + fieldName + " !=null\"> \n" + (isAnd ? " and " : "") + StringUtils.allToUpper(fieldName)
				+ " = #{" + fieldName + "}" + (isAnd ? "" : ",") + "\n" + "</if>";
	}

	/**
	 * 批量生成xml domain包名
	 * 
	 * @param docPath
	 * @param packageName
	 */
	public static void buildXML(String docPath, String packageName) {
		List<Class<?>> list = FileIoUtil.getClasssFromPackage(packageName);

		for (Class<?> clazz : list) {
			try {
				buildXML(docPath, clazz);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void buildXML(String docPath, Class<?> clazz) throws IOException {
		String tableName = "I_GIS_" + clazz.getSimpleName().replace("Domain", "").toUpperCase();
		buildXML(docPath, clazz, tableName);
	}

	// 生成XML文件
	public static void buildXML(String docPath, Class<?> clazz, String tableName) throws IOException {
		BufferedWriter bufferedWriter = null;
		try {
			if (!clazz.getSimpleName().contains("Domain") && !clazz.getSimpleName().contains("DAO")) {
				return;
			}
			String namespace = clazz.getName().replace("domain", "dao").replace("Domain", "DAO");

			String fileName = clazz.getSimpleName().replace("Domain", "DAO");// xxxDAO

			// xml头部

			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			sb.append("\n");
			sb.append(
					"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
			sb.append("\n");
			sb.append("<mapper namespace=\"" + namespace + "\">");
			sb.append("\n");

			sb.append(buildFindByKey(clazz, tableName));

			sb.append(buildFindByWhere(clazz, tableName));

			sb.append(buildInsert(clazz, tableName));
			sb.append(buildUpdate(clazz, tableName));
			sb.append(buildDelete(clazz, tableName));

			sb.append("</mapper>");

			File file = FileUtil.createFile(docPath, fileName + ".xml");

			bufferedWriter = new BufferedWriter(new FileWriter(file));

			bufferedWriter.write(sb.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != bufferedWriter) {
				bufferedWriter.flush();
				bufferedWriter.close();
			}
		}
	}

	// findByKey
	private static String buildFindByKey(Class<?> clazz, String tableName) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"findByKey\" parameterType=\"").append(clazz.getSimpleName()).append("\" resultType=\"")
				.append(clazz.getSimpleName()).append("\" useCache=\"true\">");
		sb.append("\n");
		sb.append("select ").append(getColumns(clazz)).append(" from ").append(tableName);
		List<String> primaryKeys = getPrimaryKeys(clazz);
		if (0 != primaryKeys.size()) {
			sb.append("<where>");
			sb.append("\n");
			for (String fieldName : getPrimaryKeys(clazz)) {
				sb.append(dynamicSqlFormat(fieldName, true));
				sb.append("\n");
			}
			sb.append("</where>");
		}
		sb.append("\n");
		sb.append("</select>");
		sb.append("\n");
		return sb.toString();
	}

	// findByWhere
	private static String buildFindByWhere(Class<?> clazz, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"findByWhere\" parameterType=\"").append(clazz.getSimpleName())
				.append("\" resultType=\"").append(clazz.getSimpleName()).append("\" useCache=\"true\">");
		sb.append("\n");
		sb.append("select ").append(getColumns(clazz)).append(" from ").append(tableName);
		sb.append("\n");
		sb.append("<where>");
		sb.append("\n");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			sb.append(dynamicSqlFormat(field.getName(), true));
			sb.append("\n");
		}
		sb.append("</where>");
		sb.append("\n");
		sb.append("</select>");
		sb.append("\n");
		return sb.toString();
	}

	private static String buildInsert(Class<?> clazz, String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("<insert id=\"insert\" parameterType=\"" + clazz.getSimpleName() + "\" flushCache=\"true\">");
		sb.append("\n");
		sb.append("insert into ");
		sb.append(tableName);
		sb.append("(");
		sb.append(getColumns(clazz));
		sb.append(") values ");
		sb.append(getInsert(clazz));
		sb.append("\n");
		sb.append("</insert>");
		sb.append("\n");
		return sb.toString();
	}

	private static String buildUpdate(Class<?> clazz, String tableName) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<update id=\"update\" parameterType=\"" + clazz.getSimpleName() + "\" flushCache=\"true\">");
		sb.append("\n");
		sb.append("update ");
		sb.append(tableName);
		sb.append("\n");
		sb.append("<set>");
		sb.append("\n");
		sb.append(getUpdate(clazz));
		sb.append("</set>");
		sb.append("\n");
		List<String> primaryKeys = getPrimaryKeys(clazz);
		if (0 != primaryKeys.size()) {
			sb.append("<where>");
			sb.append("\n");
			for (String fieldName : getPrimaryKeys(clazz)) {
				sb.append(dynamicSqlFormat(fieldName, true));
				sb.append("\n");
			}
			sb.append("</where>");
		} else {
			throw new Exception("生成update语句无主键,存在隐患....");
		}
		sb.append("\n");
		sb.append("</update>");
		sb.append("\n");
		return sb.toString();
	}

	private static String buildDelete(Class<?> clazz, String tableName) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<delete id=\"delete\" parameterType=\"" + clazz.getSimpleName() + "\" flushCache=\"true\">");
		sb.append("\n");
		sb.append("delete from ");
		sb.append(tableName);
		sb.append("\n");

		List<String> primaryKeys = getPrimaryKeys(clazz);
		if (0 != primaryKeys.size()) {
			sb.append("<where>");
			sb.append("\n");
			for (String fieldName : getPrimaryKeys(clazz)) {
				sb.append(dynamicSqlFormat(fieldName, true));
				sb.append("\n");
			}
			sb.append("</where>");
		} else {
			throw new Exception("生成update语句无主键,存在隐患....");
		}
		sb.append("\n");
		sb.append("</delete>");
		sb.append("\n");
		return sb.toString();
	}

}

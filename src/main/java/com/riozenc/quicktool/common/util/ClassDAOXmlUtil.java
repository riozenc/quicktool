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

	public static void main(String[] args) {
		buildXML("D:\\EclipseWorkspaces\\interactionGis\\src\\main\\java\\com\\wisdom\\gy\\interactionGis\\app\\dao\\gis\\log", 
				"com.wisdom.gy.interactionGis.app.domain.gis.log");
		buildXML("D:\\EclipseWorkspaces\\interactionGis\\src\\main\\java\\com\\wisdom\\gy\\interactionGis\\app\\dao\\gis\\data", 
				"com.wisdom.gy.interactionGis.app.domain.gis.data");
	}
	
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
			sb.append("<if test=\"" + field.getName() + " !=null\">").append("\n");
			sb.append(StringUtil.allToUpper(field.getName()) + " = #{" + field.getName() + "},").append("\n");
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

			sb.append(StringUtil.allToUpper(field.getName())).append(",");
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

	private static String dynamicSqlFormat(String fieldName) {
		return "<if test=\"" + fieldName + " !=null\"> \n" + StringUtil.allToUpper(fieldName) + " = #{" + fieldName
				+ "},\n" + "</if>";
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

	// 生成XML文件
	public static void buildXML(String docPath, Class<?> clazz) throws IOException {
		
		if(!clazz.getSimpleName().contains("Domain")&&!clazz.getSimpleName().contains("DAO")){
			return;
		}
		
		String fileName = clazz.getSimpleName().replace("Domain", "DAO");// xxxDAO
		String tableName = "I_GIS_" + clazz.getSimpleName().replace("Domain", "").toUpperCase();
		File file = FileUtil.createFile(docPath, fileName + ".xml");

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
		// xml头部

		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		sb.append("\n");
		sb.append(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		sb.append("\n");
		sb.append("<mapper namespace=\"" + clazz.getName() + "\">");
		sb.append("\n");

		bufferedWriter.write(sb.toString());

		bufferedWriter.write(buildFindByKey(clazz, tableName));
		bufferedWriter.write(buildFindByWhere(clazz, tableName));
		bufferedWriter.write(buildInsert(clazz, tableName));
		try {
			bufferedWriter.write(buildUpdate(clazz, tableName));
			bufferedWriter.write(buildDelete(clazz, tableName));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bufferedWriter.write("</mapper>");

		bufferedWriter.flush();
		bufferedWriter.close();
	}

	// findByKey
	private static String buildFindByKey(Class<?> clazz, String tableName) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append("<select id=\"findByWhere\" parameterType=\"").append(clazz.getSimpleName())
				.append("\" resultType=\"").append(clazz.getSimpleName()).append("\" useCache=\"true\">");
		sb.append("\n");
		sb.append("select ").append(getColumns(clazz)).append(" from ").append(tableName);
		List<String> primaryKeys = getPrimaryKeys(clazz);
		if (0 != primaryKeys.size()) {
			sb.append("<where>");
			sb.append("\n");
			for (String fieldName : getPrimaryKeys(clazz)) {
				sb.append(dynamicSqlFormat(fieldName));
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
			sb.append(dynamicSqlFormat(field.getName()));
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
				sb.append(dynamicSqlFormat(fieldName));
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
				sb.append(dynamicSqlFormat(fieldName));
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

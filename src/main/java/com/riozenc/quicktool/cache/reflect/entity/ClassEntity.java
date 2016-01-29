/**
 * Title:ClassEntity.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.cache.reflect.entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ClassEntity {
	private Class<?> clazz;
	private Map<String, Field> fieldMap;
	private Field[] fields;
	private Map<String, Method> methodMap;
	private List<Integer> charcterLengthList;

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Map<String, Field> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, Field> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Field[] getFields() {
		return fields;
	}

	public void setFields(Field[] fields) {
		this.fields = fields;
	}

	public Map<String, Method> getMethodMap() {
		return methodMap;
	}

	public void setMethodMap(Map<String, Method> methodMap) {
		this.methodMap = methodMap;
	}

	public List<Integer> getCharcterLengthList() {
		return charcterLengthList;
	}

	public void setCharcterLengthList(List<Integer> charcterLengthList) {
		this.charcterLengthList = charcterLengthList;
	}

}

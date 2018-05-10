/**
 * Title:ClassCache.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.cache.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.annotation.CharacterLength;
import com.riozenc.quicktool.cache.reflect.entity.ClassEntity;
import com.riozenc.quicktool.common.util.annotation.AnnotationUtil;
import com.riozenc.quicktool.common.util.file.FileIoUtil;
import com.riozenc.quicktool.exception.CacheInitException;

public class ClassCache {
	private static Logger LOGGER = LogManager.getLogger(ClassCache.class);

	private static Map<Class<?>, ClassEntity> map = new ConcurrentHashMap<Class<?>, ClassEntity>();
	private static final String PACKAGE_NAME = "com.wisdom.gy.interactionMarketing.app.domain";
	private static boolean isInit = false;

	private ClassCache() {
	}

	public static ClassEntity getClassEntity(Class<?> clazz) {
		if (!isInit) {
			throw new CacheInitException("缓存机制未启动...");
		}
		return map.get(clazz);
	}

	public static void init() {
		if (isInit) {
			// OOO
		} else {
			List<Class<?>> list = FileIoUtil.getClasssFromPackage(PACKAGE_NAME);
			Map<String, Method> methodMap = null;
			Map<String, Field> fieldMap = null;
			ClassEntity entity = null;
			Method[] methods = null;
			Field[] fields = null;
			List<Integer> charcterLengthList = null;
			for (Class<?> clazz : list) {
				entity = new ClassEntity();
				methodMap = new HashMap<String, Method>();
				fieldMap = new HashMap<String, Field>();
				entity.setClazz(clazz);

				fields = clazz.getDeclaredFields();
				charcterLengthList = new ArrayList<Integer>();

				entity.setFields(fields);

				for (Field field : fields) {
					fieldMap.put(field.getName(), field);
					charcterLengthList.add((Integer) AnnotationUtil.getAnnotationValue(field, CharacterLength.class));
				}

				entity.setFieldMap(fieldMap);
				entity.setCharcterLengthList(charcterLengthList);

				methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					methodMap.put(method.getName(), method);
				}
				entity.setMethodMap(methodMap);

				map.put(clazz, entity);
				LOGGER.info("ClassCache加载" + clazz.getName() + ".....");
			}
			isInit = true;
		}
	}
}

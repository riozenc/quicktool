package com.riozenc.quicktool.common.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

public class FileContextUtil {
	private static final Map<String, Map<Object, Object>> MAP = new HashMap<String, Map<Object, Object>>();

	public static Map<Object, Object> getMap(Class<?> clazz) {
		String className = clazz.getSimpleName();
		Map<Object, Object> map = MAP.get(clazz);
		if (null == map || map.size() == 0) {
			if (null == map) {
				map = new HashMap<Object, Object>();
			}
			InputStream inputStream = Class.class.getResourceAsStream("/" + className + ".properties");
			Properties PROPERTIES = new Properties();
			try {
				PROPERTIES.load(inputStream);
				Set<Entry<Object, Object>> set = PROPERTIES.entrySet();

				for (Entry<Object, Object> entry : set) {
					map.put(entry.getKey(), entry.getValue());
				}
				MAP.put(className, map);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	public static Map<Object, Object> getMap(String fileName) {
		Map<Object, Object> map = MAP.get(fileName);

		if (null == map || map.size() == 0) {
			if (null == map) {
				map = new HashMap<Object, Object>();
			}
			try {
				InputStream inputStream = Class.class.getResourceAsStream("/" + fileName + ".properties");
				Properties PROPERTIES = new Properties();
				PROPERTIES.load(inputStream);
				Set<Entry<Object, Object>> set = PROPERTIES.entrySet();

				for (Entry<Object, Object> entry : set) {
					map.put(entry.getKey(), entry.getValue());
				}
				MAP.put(fileName, map);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}
}

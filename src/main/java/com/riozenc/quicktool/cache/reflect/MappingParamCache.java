/**
 * @Title:ContrastParamCache.java
 * @author:Riozenc
 * @datetime:2015年8月27日 下午7:05:16
 */
package com.riozenc.quicktool.cache.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.exception.CacheInitException;

/**
 * 营销参数 计量参数映射缓存
 * 
 * @author Riozenc
 *
 */
public class MappingParamCache {

	private static Logger LOGGER = LogManager.getLogger(MappingParamCache.class);

	private static Map<String, Map<String, String>> map = new ConcurrentHashMap<String, Map<String, String>>();
	private static boolean isInit = false;

	private MappingParamCache() {
	}

	public static Map<String, String> getCommonParam(String type) {
		if (!isInit) {
			throw new CacheInitException("缓存机制未启动...");
		}
		return map.get(type);
	}

}

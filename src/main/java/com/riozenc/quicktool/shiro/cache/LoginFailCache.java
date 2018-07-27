/**
 *    Auth:riozenc
 *    Date:2018年7月26日 上午10:31:12
 *    Title:com.riozenc.quicktool.shiro.cache.LoginFailCache.java
 **/
package com.riozenc.quicktool.shiro.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录失败记录
 * 
 * @author riozenc
 *
 */
public class LoginFailCache {
	private final static Map<String, Integer> loginFailMap = new ConcurrentHashMap<>();

	public static Integer put(String key, Integer value) {
		return loginFailMap.put(key, value);
	}

	public static Integer get(String key) {
		return loginFailMap.get(key);
	}

	public static void remove(String key) {
		loginFailMap.remove(key);
	}
}

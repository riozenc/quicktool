/**
 *    Auth:riozenc
 *    Date:2018年5月11日 上午10:51:47
 *    Title:com.riozenc.quicktool.shiro.cache.LoginSessionCache.java
 **/
package com.riozenc.quicktool.shiro.cache;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoginSessionCache {
	private static final Map<String, Serializable> map = new ConcurrentHashMap<>();
	private static final Lock lock = new ReentrantLock();

	public static Serializable put(String key, Serializable value) {
		return map.put(key, value);
	}

	public static Serializable get(String key) {
		return map.get(key);
	}
}

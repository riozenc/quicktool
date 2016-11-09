/**
 * Title:BaseService.java
 * Author:czy
 * Datetime:2016年10月28日 下午3:02:19
 */
package com.riozenc.quicktool.web.service;

import java.util.List;

public interface BaseService<T> {
	public int insert(T t);

	public int delete(T t);

	public int update(T t);

	public T findByKey(T t);

	public List<T> findByWhere(T t);
}

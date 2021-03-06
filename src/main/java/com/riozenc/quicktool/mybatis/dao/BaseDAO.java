/**
 * Title:BaseDAO.java
 * Author:czy
 * Datetime:2016年10月31日 下午5:35:45
 */
package com.riozenc.quicktool.mybatis.dao;

import java.util.List;

public interface BaseDAO<T> {
	public int insert(T t);

	public int delete(T t);

	public int update(T t);

	public T findByKey(T t);

	public List<T> findByWhere(T t);
}

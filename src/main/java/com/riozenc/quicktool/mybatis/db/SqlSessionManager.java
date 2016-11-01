/**
 * Title:SqlSessionManager.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.mybatis.db;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.riozenc.quicktool.mybatis.db.DbFactory;

public class SqlSessionManager {
	private static SqlSessionFactory sqlSessionFactory = null;

	private static byte[] b = new byte[0];

	private SqlSessionManager() {
	}

	public static SqlSession getSession() {

		return getSession(ExecutorType.SIMPLE);
	}

	public static SqlSession getSession(ExecutorType executorType) {
		if (null == sqlSessionFactory) {
			synchronized (b) {
				sqlSessionFactory = DbFactory.getSqlSessionFactory();
			}
		}
		// 不自动提交
		return sqlSessionFactory.openSession(executorType, false);
	}
}

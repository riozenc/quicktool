/**
 * Title:TransactionProxyHandler.java
 * Author:czy
 * Datetime:2016年11月15日 下午2:38:03
 */
package com.riozenc.quicktool.springmvc.transaction.proxy;

import org.apache.ibatis.session.SqlSession;

public interface TransactionProxyHandler {
	public void process(SqlSession sqlSession);
}

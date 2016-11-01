/**
 * Title:DbFactory.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.mybatis.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.exception.DbInitException;

public class DbFactory {

	private static final String resource = "mybatis-config.xml";
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	private static boolean FLAG = false;

	protected static SqlSessionFactory getSqlSessionFactory() {
		if (FLAG) {
			return sqlSessionFactory;
		} else {
			throw new DbInitException("数据库未完成初始化...");
		}
	}

	public static void init() {
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getLogger(LOG_TYPE.DB).error("读取配置文件失败...");
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "gy_jl");

		try {
			if (sqlSessionFactory.openSession().getConnection().isValid(2000)) {
				FLAG = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.getLogger(LOG_TYPE.ERROR).error(ExceptionLogUtil.log(e));
			LogUtil.getLogger(LOG_TYPE.DB).info("数据库初始化失败...");
		}

		if (FLAG) {
			LogUtil.getLogger(LOG_TYPE.DB).info("数据库完成初始化...");
		}
	}
}

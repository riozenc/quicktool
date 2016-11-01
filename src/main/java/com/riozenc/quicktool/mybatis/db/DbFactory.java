/**
 * Title:DbFactory.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.mybatis.db;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.config.Global;
import com.riozenc.quicktool.exception.DbInitException;
import com.riozenc.quicktool.springmvc.context.SpringContextHolder;

public class DbFactory {

	private static final String resource = "mybatis-config.xml";
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	private static boolean FLAG = false;

	private static Map<String, SqlSessionFactoryBean> DBS = new HashMap<String, SqlSessionFactoryBean>();

	protected static SqlSessionFactory getSqlSessionFactory() {
		if (FLAG) {
			return sqlSessionFactory;
		} else {
			throw new DbInitException("数据库未完成初始化...");
		}
	}

	protected static SqlSessionFactory getSqlSessionFactory(String name) throws Exception {
		if (FLAG) {
			return DBS.get(name).getObject();
		} else {
			throw new DbInitException("数据库未完成初始化...");
		}
	}

	/**
	 * 默认db
	 * 
	 * @throws Exception
	 */
	public static void initDBs() throws Exception {
		initDBs("db");
	}

	public static void initDBs(String name) throws Exception {
		String db = Global.getConfig(name);
		String[] dbs = db.split(",");
		for (String temp : dbs) {
			DBS.put(temp, SpringContextHolder.getBean(temp));
			FLAG = checkSqlSession(DBS.get(temp).getObject().openSession());
		}
	}

	public static void initDB() {
		try {
			reader = Resources.getResourceAsReader(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.getLogger(LOG_TYPE.DB).error("读取配置文件失败...");
		}
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "gy_jl");

		FLAG = checkSqlSession(sqlSessionFactory.openSession());
		if (!FLAG) {
			LogUtil.getLogger(LOG_TYPE.DB).info("数据库完成初始化...");
		}
	}

	private static boolean checkSqlSession(SqlSession sqlSession) {

		try {
			if (sqlSession.getConnection().isValid(2000)) {
				return true;
			} else {
				LogUtil.getLogger(LOG_TYPE.DB).info("数据库连接校验等待超时...请检查...");
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.getLogger(LOG_TYPE.ERROR).error(ExceptionLogUtil.log(e));
			LogUtil.getLogger(LOG_TYPE.DB).info("数据库初始化失败...");
			return false;
		} finally {
			sqlSession.close();
		}
	}
}

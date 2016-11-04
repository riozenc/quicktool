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
import java.util.Map.Entry;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;
import com.riozenc.quicktool.config.Global;
import com.riozenc.quicktool.exception.DbInitException;
import com.riozenc.quicktool.mybatis.MybatisEntity;
import com.riozenc.quicktool.springmvc.context.SpringContextHolder;

public class DbFactory {
	private static final String DB = "db";

	private static final String resource = "mybatis-config.xml";
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	private static boolean FLAG = false;

	private static Map<String, SqlSessionFactory> DBS = new HashMap<String, SqlSessionFactory>();

	protected static SqlSessionFactory getSqlSessionFactory() {
		if (FLAG) {
			return sqlSessionFactory;
		} else {
			throw new DbInitException("数据库未完成初始化...");
		}
	}

	protected static SqlSessionFactory getSqlSessionFactory(String name) throws Exception {
		if (FLAG) {
			return DBS.get(name);
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
			FLAG = checkSqlSession(DBS.get(temp).openSession());
		}
	}

	public static void init() {
		String db = Global.getConfig(DB);
		String[] dbs = db.split(",");
		for (String temp : dbs) {
			SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
			PooledDataSource pooledDataSource = new PooledDataSource();

			for (Entry<String, String> entry : Global.getConfigs(temp).entrySet()) {
				try {
					ReflectUtil.setFieldValue(pooledDataSource, getParam(entry.getKey()), entry.getValue());
				} catch (Exception e) {
					e.initCause(new ReflectionException(getParam(entry.getKey()) + "不是正确的属性..."));
					System.out.println(getParam(entry.getKey()) + "不是正确的属性...");
				}
			}

			factoryBean.setDataSource(pooledDataSource);
			factoryBean.setTypeAliasesPackage("crm");
			factoryBean.setTypeAliasesSuperType(MybatisEntity.class);

			try {
				factoryBean.setMapperLocations(
						new PathMatchingResourcePatternResolver().getResources("classpath:/crm/webapp/**/*.xml"));
				factoryBean.setConfigLocation(
						new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

	/**
	 * eg: xxx.aaa ——> aaa eg: xxx.aaa.bbb.ccc ——> ccc
	 * 
	 * @param param
	 * @return
	 */
	private static String getParam(String param) {
		return param.substring(param.lastIndexOf(".") + 1);
	}

}

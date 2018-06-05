/**
 *    Auth:riozenc
 *    Date:2018年6月5日 下午5:41:04
 *    Title:com.riozenc.quicktool.db.DruidDataSourcePoolFactory.java
 **/
package com.riozenc.quicktool.db;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.riozenc.quicktool.common.util.log.ExceptionLogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.common.util.reflect.ReflectUtil;
import com.riozenc.quicktool.config.Global;
import com.riozenc.quicktool.mybatis.MybatisEntity;
import com.riozenc.quicktool.mybatis.db.DbFactory;
import com.riozenc.quicktool.mybatis.db.pool.druid.DruidDataSource;

public class DataSourcePoolFactory {
	private static final Logger LOGGER = LogManager.getLogger(DbFactory.class);
	private static final String DB = "db";
	private static boolean FLAG = false;
	private static String defaultDB = null;
	private static SqlSessionFactory sqlSessionFactory;

	private static Map<String, SqlSessionFactory> DBS = new LinkedHashMap<String, SqlSessionFactory>();

	public void init() throws Exception {

		String db = Global.getConfig(DB);
		String[] dbs = db.split(",");
		for (String temp : dbs) {
			SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
			DruidDataSource dataSource = new DruidDataSource();

			if (Global.getConfig("db.autoCommit").equals("false")) {

				dataSource.setDefaultAutoCommit(false);// 设置连接默认不自动提交
			}

			for (Entry<String, String> entry : Global.getConfigs(temp).entrySet()) {
				try {
					ReflectUtil.setValue(dataSource, getParam(entry.getKey()), entry.getValue());
				} catch (Exception e) {
					LOGGER.info(getParam(entry.getKey()) + "属性存在问题,无该属性或对应set/get方法存在异常...尝试强制赋值.");
					e.initCause(new ReflectionException(
							getParam(entry.getKey()) + "属性存在问题,无该属性或对应set/get方法存在异常...尝试强制赋值."));
					// 强制赋值
					try {
						ReflectUtil.setFieldValue(dataSource, getParam(entry.getKey()), entry.getValue());
					} catch (Exception e1) {
						LOGGER.info(getParam(entry.getKey()) + "不是正确的属性...强制赋值失败");
						e1.initCause(new ReflectionException(getParam(entry.getKey()) + "不是正确的属性...强制赋值失败"));
					}
				}
			}

			factoryBean.setDataSource(dataSource);
			factoryBean.setTypeAliasesPackage(Global.getConfig("namespace"));
			factoryBean.setTypeAliasesSuperType(MybatisEntity.class);

			factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
					.getResources("classpath:/" + Global.getConfig("namespace") + "/webapp/**/*.xml"));

			factoryBean.setConfigLocation(
					new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));

			putDB(temp, factoryBean.getObject());

		}

	}

	private static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (DataSourcePoolFactory.sqlSessionFactory == null) {
			DataSourcePoolFactory.sqlSessionFactory = sqlSessionFactory;
		}
	}

	private static void putDB(String key, SqlSessionFactory sessionFactory) {
		if (defaultDB == null) {
			defaultDB = key;
		}
		DBS.put(key, sessionFactory);
		FLAG = checkSqlSessionFactory(DBS.get(key));
	}

	/**
	 * 校验数据库是否可用
	 * 
	 * @param sqlSession
	 * @return
	 */
	private static boolean checkSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			if (sqlSession.getConnection().isValid(2000)) {
				setSqlSessionFactory(sqlSessionFactory);
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

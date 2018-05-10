/**
 * Title:DbFactory.java
 * author:Riozen
 * datetime:2015年3月17日 下午8:20:21
 */

package com.riozenc.quicktool.mybatis.db;

import java.io.IOException;
import java.io.Reader;
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
import com.riozenc.quicktool.exception.DbInitException;
import com.riozenc.quicktool.mybatis.MybatisEntity;
import com.riozenc.quicktool.mybatis.db.pool.druid.DruidDataSource;
import com.riozenc.quicktool.springmvc.context.SpringContextHolder;

public class DbFactory {

	private static final Logger LOGGER = LogManager.getLogger(DbFactory.class);
	private static final String DB = "db";

	private static final String resource = "mybatis-config.xml";
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;

	private static boolean FLAG = false;
	private static String defaultDB = null;

	private static Map<String, SqlSessionFactory> DBS = new LinkedHashMap<String, SqlSessionFactory>();

	private static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (DbFactory.sqlSessionFactory == null) {
			DbFactory.sqlSessionFactory = sqlSessionFactory;
		}
	}

	/**
	 * 获取默认数据库连接工厂,多数据源情况下取第一个
	 * 
	 * @return
	 */
	protected static SqlSessionFactory getSqlSessionFactory() {
		if (FLAG) {
			return sqlSessionFactory;
		} else {
			throw new DbInitException("数据库未完成初始化...");
		}
	}

	/**
	 * 获取默认数据库连接工厂,根据name
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	protected static SqlSessionFactory getSqlSessionFactory(String name) throws Exception {
		if (FLAG) {
			if (null == name || "".equals(name)) {
				name = defaultDB;
			}
			return DBS.get(name);
		} else {
			throw new DbInitException("数据库未完成初始化...");
		}
	}

	/**
	 * 通过Spring的配置文件初始化
	 * 
	 * @throws Exception
	 */
	public static void initBySpring() throws Exception {
		initBySpring("db");
	}

	/**
	 * 通过Spring的配置文件初始化,name为config中的数据库名称
	 * 
	 * @param name
	 * @throws Exception
	 */
	public static void initBySpring(String name) throws Exception {

		String db = Global.getConfig(name);
		String[] dbs = db.split(",");
		for (String temp : dbs) {
			putDB(temp, SpringContextHolder.getBean(temp));
		}
	}

	/**
	 * 通过java代码实现初始化,name为config中的数据库名称
	 */
	public static void initByFactory() {
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

			try {
				factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
						.getResources("classpath:/" + Global.getConfig("namespace") + "/webapp/**/*.xml"));

				factoryBean.setConfigLocation(
						new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml"));

				// Configuration configuration = new Configuration();
				// configuration.setLazyLoadingEnabled(true);
				// configuration.setMapUnderscoreToCamelCase(true);
				// configuration.setCacheEnabled(true);
				// configuration.setLogImpl(Log4j2Impl.class);
				// configuration.setJdbcTypeForNull(JdbcType.NULL);
				// factoryBean.setConfiguration(configuration);

				putDB(temp, factoryBean.getObject());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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

/**
 * @Title:BoneCpConnectionPool.java
 * @author:Riozenc
 * @datetime:2015年6月2日 下午11:00:59
 */
package com.riozenc.quicktool.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.Statistics;
import com.riozenc.quicktool.common.util.log.LogUtil;
import com.riozenc.quicktool.common.util.log.LogUtil.LOG_TYPE;
import com.riozenc.quicktool.exception.DbInitException;

public class BoneCpConnectionPool {
	private static boolean isInit = false;

	private static BoneCPConfig CONFIG = null;
	private static BoneCP BONE_CP = null;
	private static Statistics STATISTICS = null;

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // load the DB driver
	}

	public boolean isInit() {
		return isInit;
	}

	public Statistics STATISTICS() {
		return STATISTICS;
	}

	private static void initConfig() {
		if (!isInit) {
			CONFIG = new BoneCPConfig();

			CONFIG.setJdbcUrl("jdbc:oracle:thin:@10.164.10.146:1521:energydb"); // set
																				// the
																				// JDBC
																				// url
			CONFIG.setUsername("df6100e"); // set the username
			CONFIG.setPassword("wisdom.df6100"); // set the password

			CONFIG.setIdleMaxAge(60 * 5, TimeUnit.SECONDS);// 连接池中未使用的链接最大存活时间，单位是分，默认值：60，如果要永远存活设置为0
			CONFIG.setMaxConnectionsPerPartition(5);// 每个分区最大的连接数
			CONFIG.setMinConnectionsPerPartition(1);// 每个分区最小的连接数
			CONFIG.setPartitionCount(2);// 分区数 ，默认值2，最小1，推荐3-4，视应用而定
			CONFIG.setIdleConnectionTestPeriod(60, TimeUnit.MINUTES);// 检查数据库连接池中空闲连接的间隔时间，单位是分，默认值：240，如果要取消则设置为0
			CONFIG.setAcquireIncrement(2);// 每次去拿数据库连接的时候一次性要拿几个,默认值：2

			CONFIG.setConnectionTestStatement("select 1 from dual");// 在做keep-alive的时候的SQL语句。
			CONFIG.setLogStatementsEnabled(false);// 如果设置为true，就会打印执行的SQL语句，如果你用了其他能打印SQL语句的框架，那就不必了。

			CONFIG.setAcquireRetryDelay(7, TimeUnit.SECONDS); // acquireRetryDelay：在获取连接失败后，第二次参试前的延迟时间，默认为7000毫秒。
			CONFIG.setAcquireRetryAttempts(5);// acquireRetryAttempts：在获取连接失败后的重试次数，默认为5次。

		}

	}

	public static void initPool() {
		if (!isInit) {
			try {
				initConfig();
				BONE_CP = new BoneCP(CONFIG);

				STATISTICS = BONE_CP.getStatistics();
				isInit = true;
				LogUtil.getLogger(LOG_TYPE.DB).info("数据完成初始化...");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static Connection getConnection() {
		if (isInit) {
			Connection connection = null;
			try {
				connection = BONE_CP.getConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogUtil.getLogger(LOG_TYPE.DB).error(e.getMessage());
			}
			return connection;
		} else {
			throw new DbInitException("数据库未初始化....");
		}
	}

}

/**
 * @Title:CommonParamCache.java
 * @author:Riozenc
 * @datetime:2015年8月27日 下午3:58:18
 */
package com.riozenc.quicktool.cache.reflect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.riozenc.quicktool.exception.CacheInitException;

/**
 * 公共参数缓存
 * 
 * @author Riozenc
 *
 */
public class CommonParamCache {
//	private static Logger LOGGER = LogManager.getLogger(CommonParamCache.class);
//	private static String[] PARAM_COLUMNS = { "LINE_TYPE", "LINE_CLASS", "VOLT_CODE", "XL_RUN_STATUS_CODE",
//			"DATA_SRC" };
//	private static String PARAM_TABLE = "COMMON_PARAM";
//	private static Map<String, Map<String, String>> map = new ConcurrentHashMap<String, Map<String, String>>();
//	private static boolean isInit = false;
//
//	private CommonParamCache() {
//	}
//
//	public static Map<String, String> getCommonParam(String type) {
//		if (!isInit) {
//			throw new CacheInitException("缓存机制未启动...");
//		}
//		return map.get(type);
//	}
//
//	public static void init() {
//		if (isInit) {
//			// OOO
//		} else {
//			try {
//
//				Statement statement = SqlSessionManager.getSession().getConnection().createStatement();
//
//				ResultSet resultSet = null;
//				Map<String, String> temp = null;
//				for (String colum : PARAM_COLUMNS) {
//					temp = new HashMap<String, String>();
//
//					resultSet = statement
//							.executeQuery("select * from " + PARAM_TABLE + " where param_type='" + colum + "'");
//					while (resultSet.next()) {
//
//						temp.put(resultSet.getString(2), resultSet.getString(3));
//
//					}
//					map.put(colum, temp);
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			isInit = true;
//		}
//	}

}

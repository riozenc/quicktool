/**
 *    Auth:riozenc
 *    Date:2018年6月5日 下午5:37:33
 *    Title:com.riozenc.quicktool.db.DruidDataSourcePool.java
 **/
package com.riozenc.quicktool.db;

import com.riozenc.quicktool.config.Global;

public class DruidDataSourcePool {
	
	private static final String DB = "db";

	public DruidDataSourcePool() {
		String db = Global.getConfig(DB);
		String[] dbs = db.split(",");
	}
}

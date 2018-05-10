/**
 *    Auth:riozenc
 *    Date:2018年5月10日 下午6:42:01
 *    Title:com.riozenc.quicktool.mybatis.db.pool.druid.DruidDataSource.java
 **/
package com.riozenc.quicktool.mybatis.db.pool.druid;

import java.sql.SQLException;

public class DruidDataSource extends com.alibaba.druid.pool.DruidDataSource {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5097948238078035579L;

	private String filters;

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) throws SQLException{
		super.setFilters(filters);
	}

}

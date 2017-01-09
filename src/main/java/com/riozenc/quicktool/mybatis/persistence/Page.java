package com.riozenc.quicktool.mybatis.persistence;

import com.riozenc.quicktool.config.Global;

public class Page<T> {
	private int totalRow;// 总条数
	private int pageCurrent = 1; // 当前页
	private int pageSize= Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public int getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult() {
		int firstResult = (getPageCurrent() - 1) * getPageSize();
		if (firstResult >= getTotalRow()) {
			firstResult = 0;
		}
		return firstResult;
	}

	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults() {
		return getPageSize();
	}

}

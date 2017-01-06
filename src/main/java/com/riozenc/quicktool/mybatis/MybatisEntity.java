/**
 * Title:MybatisEntity.java
 * Author:czy
 * Datetime:2016年11月4日 下午4:48:38
 */
package com.riozenc.quicktool.mybatis;

public abstract class MybatisEntity {

	private int totalRow;// 总条数
	private int pageCurrent;// 当前页
	private int psize;// 显示条数

	protected int getTotalRow() {
		return totalRow;
	}

	protected void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	protected int getPageCurrent() {
		return pageCurrent;
	}

	protected void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	protected int getPsize() {
		return psize;
	}

	protected void setPsize(int psize) {
		this.psize = psize;
	}

}

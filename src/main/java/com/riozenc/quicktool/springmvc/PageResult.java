/**
 *    Auth:riozenc
 *    Date:2018年9月7日 上午11:49:33
 *    Title:com.riozenc.quicktool.springmvc.PageResult.java
 **/
package com.riozenc.quicktool.springmvc;

import com.riozenc.quicktool.mybatis.persistence.Page;

public class PageResult {
	private Integer totalRow;
	private Integer pageCurrent;
	private Object list;

	/**
	 * 
	 * @param page
	 * @param list
	 */
	public PageResult(Page page, Object list) {
		this.totalRow = page.getTotalRow();
		this.pageCurrent = page.getPageCurrent();
		this.list = list;
	}

	public PageResult(int totalRow, int pageCurrent, Object list) {
		this.totalRow = totalRow;
		this.pageCurrent = pageCurrent;
		this.list = list;
	}

	public Integer getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(Integer totalRow) {
		this.totalRow = totalRow;
	}

	public Integer getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(Integer pageCurrent) {
		this.pageCurrent = pageCurrent;
	}

	public Object getList() {
		return list;
	}

	public void setList(Object list) {
		this.list = list;
	}
}

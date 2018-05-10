/**
 *	
 * @Title:JSONGrid.java
 * @author Riozen
 *	@date 2013-12-25 下午2:07:43
 *	
 */
package com.riozenc.quicktool.common.util.json;

import com.riozenc.quicktool.mybatis.persistence.Page;

public class JSONGrid {
	private Integer totalRow;
	private Integer pageCurrent;
	private Object list;

	public JSONGrid(Object list) {
		this.list = list;
	}

	public JSONGrid(Page page, Object list) {
		this.totalRow = page.getTotalRow();
		this.pageCurrent = page.getPageCurrent();
		this.list = list;
	}

	public JSONGrid(int totalRow, int pageCurrent, Object list) {
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

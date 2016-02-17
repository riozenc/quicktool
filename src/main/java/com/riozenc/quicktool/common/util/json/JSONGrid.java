/**
 *	
 * @Title:JSONGrid.java
 * @author Riozen
 *	@date 2013-12-25 下午2:07:43
 *	
 */
package com.riozenc.quicktool.common.util.json;

public class JSONGrid {
	private int total = 0;
	private Object rows;

	public JSONGrid(int total, Object rows) {
		this.total = total;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}
}

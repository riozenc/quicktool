/**
 * Title:JSONResult.java
 * Author:czy
 * Datetime:2016年11月27日 下午3:57:47
 */
package com.riozenc.quicktool.common.util.json;

public class JSONResult {
	private int status = 0;// 状态 0成功 1失败
	private int total = 0;// 数量
	private Object object;// 对象

	public JSONResult(int status, int total, Object object) {
		this.total = total;
		this.status = status;
		this.object = object;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}

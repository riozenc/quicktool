/**
 * Title:JSONResult.java
 * Author:czy
 * Datetime:2016年11月27日 下午3:57:47
 */
package com.riozenc.quicktool.common.util.json;

public class JSONResult {
	private int status = 0;// 状态 0成功 1失败
	private int total = 0;// 数量
	private Object result;// 对象

	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}

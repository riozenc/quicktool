/**
 * @Project:quicktool
 * @Title:IBaseProcessor.java
 * @Author:Riozenc
 * @Datetime:2017年3月13日 下午8:48:12
 * 
 */
package com.riozenc.quicktool.queue;

public interface IBaseProcessor<T> {

	public void excute(T t);
}

/**
 * @Project:quicktool
 * @Title:ProcessorFacotry.java
 * @Author:Riozenc
 * @Datetime:2017年3月13日 下午8:53:28
 * 
 */
package com.riozenc.quicktool.queue.processor;

import com.riozenc.quicktool.queue.IBaseProcessor;

public class ProcessorFacotry {

	private static ProcessorFacotry processorFacotry = null;

	private ProcessorFacotry() {
	}

	public static ProcessorFacotry getInstance() {
		if (processorFacotry == null) {
			synchronized (ProcessorFacotry.class) {
				if (processorFacotry == null)
					processorFacotry = new ProcessorFacotry();
			}
		}
		return processorFacotry;
	}

	/**
	 * 获取默认的处理器
	 * 
	 * @return
	 */
	public IBaseProcessor<?> get() {
		return null;
	}
}

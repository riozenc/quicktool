/**
 * @Project:quicktool
 * @Title:DefaultProcessor.java
 * @Author:Riozenc
 * @Datetime:2017年3月13日 下午9:04:41
 * 
 */
package com.riozenc.quicktool.queue.processor;

public class DefaultProcessor extends AbstractProcessor {

	private static DefaultProcessor defaultProcessor = new DefaultProcessor();

	private DefaultProcessor() {
	}

	public static DefaultProcessor getInstance() {
		return defaultProcessor;
	}

	@Override
	public void excute(Object t) {
		// TODO Auto-generated method stub

	}

}

/**
 * Title:BaseProcessor.java
 * Author:riozenc
 * Datetime:2017年3月3日 下午2:12:02
**/
package com.riozenc.quicktool.queue.processor;

public interface BaseProcessor<T> {

	public void excute(T t);

}

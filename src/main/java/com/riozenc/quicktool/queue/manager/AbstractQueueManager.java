/**
 * Title:AbstractQueueManager.java
 * Author:riozenc
 * Datetime:2017年3月8日 上午10:07:03
**/
package com.riozenc.quicktool.queue.manager;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.riozenc.quicktool.queue.BaseQueue;
import com.riozenc.quicktool.queue.BaseQueueElement;
import com.riozenc.quicktool.queue.IQueueManager;

public abstract class AbstractQueueManager implements IQueueManager {
	private int queueNumber = Runtime.getRuntime().availableProcessors();// 队列个数(cpu个数)
	private int queueLength = 1000;// 队列长度
	private LinkedList<BaseQueue<BaseQueueElement>> queueList;
	private final ExecutorService executor = Executors
			.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());// 获取cpu个数

	public LinkedList<BaseQueue<BaseQueueElement>> getQueueList() {
		return queueList;
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public int getQueueNumber() {
		return queueNumber;
	}

	public void setQueueNumber(int queueNumber) {
		this.queueNumber = queueNumber;
	}

	public int getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	protected abstract BaseQueue<BaseQueueElement> offerTask();//放置任务

	protected abstract BaseQueue<BaseQueueElement> getQueue();//获取队列

	protected abstract void switchQueue();//切换队列
}

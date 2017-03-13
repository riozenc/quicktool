/**
 * @Title:BaseQueue.java
 * @Author:Riozenc
 * @Datetime:2015年10月31日 下午2:33:31
 * @Project:interactionMarketing
 */
package com.riozenc.quicktool.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.riozenc.quicktool.queue.manager.QueueManager;

public abstract class BaseQueue<E> implements Runnable {

	// （阻塞）数据队列
	protected BlockingQueue<E> blockingQueue;
	// 处理器
	protected IBaseProcessor<E> baseProcessor;
	// 有效标志
	private boolean isValid;
	// 创建时间
	private long createTimestamp;
	// 最后使用时间
	private long lastUsedTimestamp;
	// 队列长度
	private int limit = 1000;

	private QueueManager queueManager;// 管理

	private int i = 0;

	public BaseQueue(QueueManager queueManager) {
		System.out.println(Thread.currentThread().getName() + "创建DbQueue");
		this.queueManager = queueManager;
		blockingQueue = new ArrayBlockingQueue<E>(limit);
		baseProcessor = null;
	}

	public BaseQueue(QueueManager queueManager, int limit) {
		System.out.println(Thread.currentThread().getName() + "创建DbQueue" + "[" + limit + "]");
		this.limit = limit;
		this.queueManager = queueManager;
		blockingQueue = new ArrayBlockingQueue<E>(limit);
		baseProcessor = null;
	}

	public boolean isValid() {
		return isValid;
	}

	public int getLimit() {
		return limit;
	}

	public long getLastUsedTimestamp() {
		return lastUsedTimestamp;
	}

	public void setLastUsedTimestamp(long lastUsedTimestamp) {
		this.lastUsedTimestamp = lastUsedTimestamp;
	}

	public int getSize() {
		return blockingQueue.size();
	}

	/**
	 * 添加 阻塞模式
	 * 
	 * @param obj
	 */
	public void putVO(E t) {
		try {
			blockingQueue.put(t);
			System.out.println("queue...put成功...");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 添加 非阻塞模式
	 * 
	 * @param obj
	 */
	public boolean offerVO(E t) {
		i++;
		return blockingQueue.offer(t);
	}

	public Object getVO() {
		return blockingQueue.poll();
	}

	public Object takeVO() throws InterruptedException {
		return blockingQueue.take();
	}

	/**
	 * 处理方法
	 * 
	 * @param t
	 */

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			baseProcessor.excute(blockingQueue.remove());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString() + "[" + i + "]";
	}
}

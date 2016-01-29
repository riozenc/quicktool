/**
 * @Title:BaseQueue.java
 * @Author:Riozenc
 * @Datetime:2015年10月31日 下午2:33:31
 * @Project:interactionMarketing
 */
package com.riozenc.quicktool.queue;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.riozenc.quicktool.queue.manager.QueueManager;

public abstract class BaseQueue<T> {

	private Long time = System.currentTimeMillis();
	private int limit = 1000;
	private ArrayBlockingQueue<T> queue;
	private QueueManager queueManager;// 管理

	public Queue<T> geQueue() {
		return queue;
	}

	public BaseQueue(QueueManager queueManager) {
		System.out.println(Thread.currentThread().getName() + "创建DbQueue");
		this.queueManager = queueManager;
		queue = new ArrayBlockingQueue<T>(limit);

	}

	public BaseQueue(QueueManager queueManager, int limit) {
		this.limit = limit;
		this.queueManager = queueManager;
		queue = new ArrayBlockingQueue<T>(limit);
	}

	public int getLimit() {
		return limit;
	}

	/**
	 * 设置queue长度
	 * 
	 * @param limit
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public int getSize() {
		return queue.size();
	}

	/**
	 * 添加 阻塞模式
	 * 
	 * @param obj
	 */
	public void putVO(T t) {
		try {
			queue.put(t);
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
	public boolean offerVO(T t) {
		return queue.offer(t);
	}

	public Object getVO() {
		return queue.poll();
	}

	public Object takeVO() throws InterruptedException {
		return queue.take();
	}

	/**
	 * 处理方法
	 * 
	 * @param t
	 */
	public abstract void excute(T t);

	public abstract void excuteAll(Queue queue);

}

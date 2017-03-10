/**
 * @Title:QueueManager.java
 * @Author:Riozenc
 * @Datetime:2015年10月31日 下午2:45:02
 * @Project:interactionMarketing
 */
package com.riozenc.quicktool.queue.manager;

import com.riozenc.quicktool.queue.BaseQueue;
import com.riozenc.quicktool.queue.BaseQueueElement;

public class QueueManager extends AbstractQueueManager {

	private volatile static QueueManager instance = null;

	private QueueManager() {
		System.out.println("初始化");
		for (int i = 0; i < getQueueNumber(); i++) {
			getQueueList().add(null);
		}
	}

	public static QueueManager getInstance() {
		// 先检查实例是否存在，如果不存在才进入下面的同步块
		if (instance == null) {
			// 同步块，线程安全地创建实例
			synchronized (QueueManager.class) {
				// 再次检查实例是否存在，如果不存在才真正地创建实例
				instance = new QueueManager();
			}
		}
		return instance;
	}

	@Override
	public void pushTask() {
		// TODO Auto-generated method stub

	}

	@Override
	protected BaseQueue<BaseQueueElement> offerTask() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseQueue<BaseQueueElement> getQueue() {
		// TODO Auto-generated method stub
		BaseQueue<BaseQueueElement> queue = getQueueList().peekFirst();
		queue.setLastUsedTimestamp(System.currentTimeMillis());
		return queue;
	}

	@Override
	protected void switchQueue() {
		// TODO Auto-generated method stub
		getQueueList().add(getQueueList().remove());
	}

}

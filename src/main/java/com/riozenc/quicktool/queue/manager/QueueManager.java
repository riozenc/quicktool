/**
 * @Title:QueueManager.java
 * @Author:Riozenc
 * @Datetime:2015年10月31日 下午2:45:02
 * @Project:interactionMarketing
 */
package com.riozenc.quicktool.queue.manager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.riozenc.quicktool.queue.BaseQueue;

public abstract class QueueManager {

	protected Long LIMIT_TIME = 1 * 60 * 1000L;// 1分钟
	protected List<BaseQueue> list = new CopyOnWriteArrayList<BaseQueue>();

	public void setQueue(BaseQueue baseQueue) {
		list.add(baseQueue);
	}

	public void run() {

	}

	public abstract void timingRun() throws InterruptedException;

	public abstract void handler(BaseQueue baseQueue);
}

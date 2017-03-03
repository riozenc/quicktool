/**
 * @Title:QueueManager.java
 * @Author:Riozenc
 * @Datetime:2015年10月31日 下午2:45:02
 * @Project:interactionMarketing
 */
package com.riozenc.quicktool.queue.manager;

import java.util.LinkedList;

import com.riozenc.quicktool.queue.BaseQueue;
import com.riozenc.quicktool.queue.BaseQueueElement;
import com.riozenc.quicktool.queue.processor.BaseProcessor;

public abstract class QueueManager {

	protected Long LIMIT_TIME = 1 * 60 * 1000L;// 1分钟
	protected LinkedList<BaseQueue> list = new LinkedList<BaseQueue>();

	// 推任务
	public final void pushTask(Object object, BaseProcessor baseProcessor) {

	}

	public <E> BaseQueue<E> getNewQueue(E t) {
		list.getLast().offerVO(t);
		

		return null;
	}

}

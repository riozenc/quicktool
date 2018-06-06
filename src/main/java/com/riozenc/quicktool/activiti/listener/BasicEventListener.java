/**
 *    Auth:riozenc
 *    Date:2018年6月6日 上午11:26:18
 *    Title:com.riozenc.quicktool.activiti.listener.BasicEventListener.java
 **/
package com.riozenc.quicktool.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicEventListener implements ActivitiEventListener {
	private static Logger log = LoggerFactory.getLogger(BasicEventListener.class);
	
	@Override
	public void onEvent(ActivitiEvent event) {
		// TODO Auto-generated method stub
		switch (event.getType()) {

		case JOB_EXECUTION_SUCCESS:
			log.info("A job well done!");
			break;

		case JOB_EXECUTION_FAILURE:
			log.info("A job has failed...");
			break;

		default:
			log.info("Event received: " + event.getType());
		}
	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub

		log.info(this.getClass() + "--isFailOnException");

		return false;
	}

}

/*
 * Created on 2005-3-29
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;


/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TaskFactory {

	/**
	 *  
	 */
	public TaskFactory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MonitorTask getInstance(String tasktype) {
		//SysLogger.info(tasktype+"================");
//		if (tasktype.equals("pingtask"))
//			return new PingTask();
		if (tasktype.equals("netcollecttask"))
			return new InterfaceTaskHC();
		
		return null;
	}

}
package com.afunms.service;

import com.gathertask.TaskManager;

class IPSServer implements Runnable{
	public IPSServer(){}
	public void run(){
		TaskManager tm = new TaskManager();
		tm.createAllTask();
		tm.CreateGahterSQLTask();
	}
	
	public static void main(String args[]) {  
        (new Thread(new IPSServer())).start();  
    }  
}

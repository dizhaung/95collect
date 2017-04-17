package com.gathertask;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.afunms.common.util.ShareData;
import com.afunms.polling.task.InterfaceTask;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.gatherdb.GathersqlRun;
import com.gatherdb.nmsmemorydate;

import java.util.Hashtable;

public class TaskManager {
	
	private Logger logger = Logger.getLogger(TaskManager.class);
	/**
	 * 建立一个维护进程
	 * 5分钟定时检查一次timer是否需要运行，或是定时时间已经改变
	 */
	public void CreateGahterSQLTask()
	{
		if (!nmsmemorydate.GathersqlTaskStatus) {
			Timer timer = null;
			GathersqlRun btask = null;
			timer = new Timer();
			btask = new GathersqlRun();
			timer.schedule(btask, 0, 20 * 1000);// 5秒钟入库一次
			nmsmemorydate.GathersqlTaskStatus = true;// 设置标记为启动
			nmsmemorydate.GathersqlTasktimer = timer;
		}
   }
	
	/**
	 * 根据数据库表的记录建立采集任务
	 * 
	 */
    public void createAllTask()
    {
		Timer timer = null;
		// 先加载资源列表

		HostNodeDao nodeDao = new HostNodeDao();
		// 得到被监视的设备
		List nodeList = new ArrayList();
	
		nodeList = nodeDao.loadIsMonitoredNode();
	
		Map<Integer,HostNode> runtask = new Hashtable();
		for (int i = 0; i < nodeList.size(); i++) {
			HostNode node = (HostNode) nodeList.get(i);
			runtask.put(node.getId(), node);
		}
		
		Iterator<HostNode> nodeIterator = runtask.values().iterator();
		while (nodeIterator.hasNext()) {
			HostNode hostnode =  nodeIterator.next();
			// 根据Hashtable 中的参数来判断来判断启动采集的任务
			if (null != nmsmemorydate.TaskList
					&& nmsmemorydate.TaskList.size() > 0
					&& nmsmemorydate.TaskList.containsKey(hostnode.getId())) {
				// 停止原来的timer，列表并且从内存中删除对应的对象
				timer = (Timer) nmsmemorydate.TaskList
						.get(hostnode.getId());
				timer.cancel();
				nmsmemorydate.TaskList.remove(hostnode.getId());
			} else if (!(ShareData.getResourceConfHashtable()).containsKey(hostnode.getId() + "")) {
				// 建立定时采集任务
				timer = new Timer();
				
				// interface任务
				InterfaceTask interfaceTask = new InterfaceTask();
				interfaceTask.setNode(hostnode);
				
				long in = 0;
				if (nmsmemorydate.TaskList.size() > 300) {
					in = (nmsmemorydate.TaskList.size() / 5) * 200;
				} else {
					in = nmsmemorydate.TaskList.size() * 200;
				}
				timer.schedule(interfaceTask, 10000L, 5 * 60 * 1000);// ping
				
				// 执行任务按分钟执行定时任务
				nmsmemorydate.TaskList.put(hostnode.getId() + "", timer);// 把TIMER对象到任务队里
			}
		}
	
    }
    
    /**
	 * 
	 * 根据id把采集任务停止
	 * 
	 * @param id
	 */
    public synchronized void cancelTask(String id)
    {
    	System.out.println("====停止任务=="+id);
    	if(null!=nmsmemorydate.TaskList.get(id) ){
    		((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//注销该任务
    		nmsmemorydate.TaskList.remove(id+"");
    	}
    }
    
    
    public void checkInterfaceTask(){
    	Timer timer = null;
		// 先加载资源列表

		HostInterfaceDao nodeDao = new HostInterfaceDao();
		// 得到被监视的设备
		List nodeList = new ArrayList();
		try {
			nodeList = nodeDao.loadAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			nodeDao.close();
		}
		Hashtable runtask = new Hashtable();
		for (int i = 0; i < nodeList.size(); i++) {
			HostNode node = (HostNode) nodeList.get(i);
			runtask.put(node.getId(), node);
		}
		logger.info("=采集任务个数==" + runtask.size());
		if (null != runtask) {
			// 如果不为空则循环
			Enumeration allvalue = runtask.elements();
			while (allvalue.hasMoreElements()) {
				HostNode hostnode = (HostNode) allvalue.nextElement();
				// 根据Hashtable 中的参数来判断来判断启动采集的任务
				if (null != nmsmemorydate.TaskList
						&& nmsmemorydate.TaskList.size() > 0
						&& nmsmemorydate.TaskList.containsKey(hostnode.getId())) {
					// 停止原来的timer，列表并且从内存中删除对应的对象
					timer = (Timer) nmsmemorydate.TaskList
							.get(hostnode.getId());
					timer.cancel();
					nmsmemorydate.TaskList.remove(hostnode.getId());
				} else if (!(ShareData.getResourceConfHashtable()).containsKey(hostnode.getId() + "")) {
					// 建立定时采集任务
					timer = new Timer();
	
					//portinfoTask.
					long in = 0;
					if (nmsmemorydate.TaskList.size() > 300) {
						in = (nmsmemorydate.TaskList.size() / 5) * 200;
					} else {
						in = nmsmemorydate.TaskList.size() * 200;
					}
					nmsmemorydate.TaskList.put(hostnode.getId() + "", timer);// 把TIMER对象到任务队里
				}
			}
		}
    }
}

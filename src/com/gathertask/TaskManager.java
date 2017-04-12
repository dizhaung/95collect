/**
 * 
 * ����XML��TASK��Ϣ�������������
 * ����������󱣴����б�TaskList��
 * 
 * һ��������ʱ�������
 * һ���ǿ��Դ������б��а��������ע��
 */
package com.gathertask;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.afunms.common.util.ShareData;
import com.afunms.polling.task.InterfaceTask;
import com.afunms.polling.task.PingTask;
import com.afunms.polling.task.PortInfoTask;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.gatherdb.GathersqlRun;
import com.gatherdb.nmsmemorydate;

import java.util.Hashtable;

public class TaskManager {
	
	private Logger logger = Logger.getLogger(TaskManager.class);
	/**
	 * ����һ��ά������
	 * 5���Ӷ�ʱ���һ��timer�Ƿ���Ҫ���У����Ƕ�ʱʱ���Ѿ��ı�
	 */
	public void CreateGahterSQLTask()
	{
		if (!nmsmemorydate.GathersqlTaskStatus) {
			Timer timer = null;
			GathersqlRun btask = null;
			timer = new Timer();
			btask = new GathersqlRun();
			timer.schedule(btask, 0, 20 * 1000);// 5�������һ��
			nmsmemorydate.GathersqlTaskStatus = true;// ���ñ��Ϊ����
			nmsmemorydate.GathersqlTasktimer = timer;
		}
   }
	
	/**
	 * �������ݿ��ļ�¼�����ɼ�����
	 * 
	 */
    public void createAllTask()
    {
		Timer timer = null;
		// �ȼ�����Դ�б�

		HostNodeDao nodeDao = new HostNodeDao();
		// �õ������ӵ��豸
		List nodeList = new ArrayList();
		try {
			nodeList = nodeDao.loadIsMonitoredNode();
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
		logger.info("=�ɼ��������==" + runtask.size());
		if (null != runtask) {
			// �����Ϊ����ѭ��
			Enumeration allvalue = runtask.elements();
			while (allvalue.hasMoreElements()) {
				HostNode hostnode = (HostNode) allvalue.nextElement();
				// ����Hashtable �еĲ������ж����ж������ɼ�������
				if (null != nmsmemorydate.TaskList
						&& nmsmemorydate.TaskList.size() > 0
						&& nmsmemorydate.TaskList.containsKey(hostnode.getId())) {
					// ֹͣԭ����timer���б��Ҵ��ڴ���ɾ����Ӧ�Ķ���
					timer = (Timer) nmsmemorydate.TaskList
							.get(hostnode.getId());
					timer.cancel();
					nmsmemorydate.TaskList.remove(hostnode.getId());
				} else if (!(ShareData.getResourceConfHashtable()).containsKey(hostnode.getId() + "")) {
					// ������ʱ�ɼ�����
					timer = new Timer();
					// ping����
					PingTask pingtask = new PingTask();
					pingtask.setNode(hostnode);
					// interface����
					InterfaceTask interfaceTask = new InterfaceTask();
					interfaceTask.setNode(hostnode);
					
					long in = 0;
					if (nmsmemorydate.TaskList.size() > 300) {
						in = (nmsmemorydate.TaskList.size() / 5) * 200;
					} else {
						in = nmsmemorydate.TaskList.size() * 200;
					}
					timer.schedule(interfaceTask, 10000L, 5 * 60 * 1000);// ping
					//timer.schedule(pingtask, 10000L, 5 * 60 * 1000);// ping
					// ִ�����񰴷���ִ�ж�ʱ����
					nmsmemorydate.TaskList.put(hostnode.getId() + "", timer);// ��TIMER�����������
				}
			}
		}
	}
    
    /**
	 * 
	 * ����id�Ѳɼ�����ֹͣ
	 * 
	 * @param id
	 */
    public synchronized void cancelTask(String id)
    {
    	System.out.println("====ֹͣ����=="+id);
    	if(null!=nmsmemorydate.TaskList.get(id) ){
    		((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//ע��������
    		nmsmemorydate.TaskList.remove(id+"");
    	}
    }
    
    
    public void checkInterfaceTask(){
    	Timer timer = null;
		// �ȼ�����Դ�б�

		HostInterfaceDao nodeDao = new HostInterfaceDao();
		// �õ������ӵ��豸
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
		logger.info("=�ɼ��������==" + runtask.size());
		if (null != runtask) {
			// �����Ϊ����ѭ��
			Enumeration allvalue = runtask.elements();
			while (allvalue.hasMoreElements()) {
				HostNode hostnode = (HostNode) allvalue.nextElement();
				// ����Hashtable �еĲ������ж����ж������ɼ�������
				if (null != nmsmemorydate.TaskList
						&& nmsmemorydate.TaskList.size() > 0
						&& nmsmemorydate.TaskList.containsKey(hostnode.getId())) {
					// ֹͣԭ����timer���б��Ҵ��ڴ���ɾ����Ӧ�Ķ���
					timer = (Timer) nmsmemorydate.TaskList
							.get(hostnode.getId());
					timer.cancel();
					nmsmemorydate.TaskList.remove(hostnode.getId());
				} else if (!(ShareData.getResourceConfHashtable()).containsKey(hostnode.getId() + "")) {
					// ������ʱ�ɼ�����
					timer = new Timer();
					// ping����
//					PingTask pingtask = new PingTask();
//					pingtask.setNode(hostnode);
//					// interface����
//					InterfaceTask interfaceTask = new InterfaceTask();
//					interfaceTask.setNode(hostnode);
					
					PortInfoTask portinfoTask = new PortInfoTask();
					//portinfoTask.
					long in = 0;
					if (nmsmemorydate.TaskList.size() > 300) {
						in = (nmsmemorydate.TaskList.size() / 5) * 200;
					} else {
						in = nmsmemorydate.TaskList.size() * 200;
					}
					//timer.schedule(interfaceTask, 10000L, 5 * 60 * 1000);// interface
					//timer.schedule(pingtask, 10000L, 5 * 60 * 1000);// ping
					//timer.schedule(portinfoTask, 10000L,24*60*60*1000);//portinfo
																			// ִ�����񰴷���ִ�ж�ʱ����
					nmsmemorydate.TaskList.put(hostnode.getId() + "", timer);// ��TIMER�����������
				}
			}
		}
    }
}

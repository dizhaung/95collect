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
import com.afunms.polling.task.InterfaceTaskHC;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.gatherdb.GathersqlRun;
import com.gatherdb.nmsmemorydate;

import java.util.Hashtable;

public class TaskManager {
	
	private Logger logger = Logger.getLogger(TaskManager.class);
	/**
	 * 寤虹珛涓�釜缁存姢杩涚▼
	 * 5鍒嗛挓瀹氭椂妫�煡涓�timer鏄惁闇�杩愯锛屾垨鏄畾鏃舵椂闂村凡缁忔敼鍙�
	 */
	public void CreateGahterSQLTask()
	{
		if (!nmsmemorydate.GathersqlTaskStatus) {
			Timer timer = null;
			GathersqlRun btask = null;
			timer = new Timer();
			btask = new GathersqlRun();
			timer.schedule(btask, 0, 20 * 1000);// 5绉掗挓鍏ュ簱涓�
			nmsmemorydate.GathersqlTaskStatus = true;// 璁剧疆鏍囪涓哄惎鍔�
			nmsmemorydate.GathersqlTasktimer = timer;
		}
   }
	
	/**
	 * 鏍规嵁鏁版嵁搴撹〃鐨勮褰曞缓绔嬮噰闆嗕换鍔�
	 * 
	 */
    public void createAllTask()
    {
		Timer timer = null;
		// 鍏堝姞杞借祫婧愬垪琛�

		HostNodeDao nodeDao = new HostNodeDao();
		// 寰楀埌琚洃瑙嗙殑璁惧
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
			// 鏍规嵁Hashtable 涓殑鍙傛暟鏉ュ垽鏂潵鍒ゆ柇鍚姩閲囬泦鐨勪换鍔�
			if (null != nmsmemorydate.TaskList
					&& nmsmemorydate.TaskList.size() > 0
					&& nmsmemorydate.TaskList.containsKey(hostnode.getId())) {
				// 鍋滄鍘熸潵鐨則imer锛屽垪琛ㄥ苟涓斾粠鍐呭瓨涓垹闄ゅ搴旂殑瀵硅薄
				timer = (Timer) nmsmemorydate.TaskList
						.get(hostnode.getId());
				timer.cancel();
				nmsmemorydate.TaskList.remove(hostnode.getId());
			} else if (!(ShareData.getResourceConfHashtable()).containsKey(hostnode.getId() + "")) {
				// 寤虹珛瀹氭椂閲囬泦浠诲姟
				timer = new Timer();
				
				// interface浠诲姟
				InterfaceTaskHC interfaceTask = new InterfaceTaskHC();
				interfaceTask.setNode(hostnode);
				long in = 0;
				if (nmsmemorydate.TaskList.size() > 300) {
					in = (nmsmemorydate.TaskList.size() / 5) * 200;
				} else {
					in = nmsmemorydate.TaskList.size() * 200;
				}
				timer.schedule(interfaceTask, 10000L, 1 * 60 * 1000);
				
				// 鎵ц浠诲姟鎸夊垎閽熸墽琛屽畾鏃朵换鍔�
				nmsmemorydate.TaskList.put(hostnode.getId() + "", timer);// 鎶奣IMER瀵硅薄鍒颁换鍔￠槦閲�
			}
		}
	
    }
    
    /**
	 * 
	 * 鏍规嵁id鎶婇噰闆嗕换鍔″仠姝�
	 * 
	 * @param id
	 */
    public synchronized void cancelTask(String id)
    {
    	System.out.println("====鍋滄浠诲姟=="+id);
    	if(null!=nmsmemorydate.TaskList.get(id) ){
    		((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//娉ㄩ攢璇ヤ换鍔�
    		nmsmemorydate.TaskList.remove(id+"");
    	}
    }
    
    
    public void checkInterfaceTask(){
    	Timer timer = null;
		// 鍏堝姞杞借祫婧愬垪琛�

		HostInterfaceDao nodeDao = new HostInterfaceDao();
		// 寰楀埌琚洃瑙嗙殑璁惧
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
		logger.info("=閲囬泦浠诲姟涓暟==" + runtask.size());
		if (null != runtask) {
			// 濡傛灉涓嶄负绌哄垯寰幆
			Enumeration allvalue = runtask.elements();
			while (allvalue.hasMoreElements()) {
				HostNode hostnode = (HostNode) allvalue.nextElement();
				// 鏍规嵁Hashtable 涓殑鍙傛暟鏉ュ垽鏂潵鍒ゆ柇鍚姩閲囬泦鐨勪换鍔�
				if (null != nmsmemorydate.TaskList
						&& nmsmemorydate.TaskList.size() > 0
						&& nmsmemorydate.TaskList.containsKey(hostnode.getId())) {
					// 鍋滄鍘熸潵鐨則imer锛屽垪琛ㄥ苟涓斾粠鍐呭瓨涓垹闄ゅ搴旂殑瀵硅薄
					timer = (Timer) nmsmemorydate.TaskList
							.get(hostnode.getId());
					timer.cancel();
					nmsmemorydate.TaskList.remove(hostnode.getId());
				} else if (!(ShareData.getResourceConfHashtable()).containsKey(hostnode.getId() + "")) {
					// 寤虹珛瀹氭椂閲囬泦浠诲姟
					timer = new Timer();
	
					//portinfoTask.
					long in = 0;
					if (nmsmemorydate.TaskList.size() > 300) {
						in = (nmsmemorydate.TaskList.size() / 5) * 200;
					} else {
						in = nmsmemorydate.TaskList.size() * 200;
					}
					nmsmemorydate.TaskList.put(hostnode.getId() + "", timer);// 鎶奣IMER瀵硅薄鍒颁换鍔￠槦閲�
				}
			}
		}
    }
}

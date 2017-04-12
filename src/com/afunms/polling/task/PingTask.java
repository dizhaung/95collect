package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.PingInfoParser;
import com.afunms.common.util.PingUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostnetPingResultTosql;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PingTask extends MonitorTask {
	private HostNode node=new HostNode();
	private static Hashtable connectConfigHashtable = new Hashtable();
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	
	public void run() {
		try{
    		int numThreads = 200;
    		try {
    			List numList = new ArrayList();
    			TaskXml taskxml = new TaskXml();
    			numList = taskxml.ListXml();
    			for (int i = 0; i < numList.size(); i++) {
    				Task task = new Task();
    				BeanUtils.copyProperties(task, numList.get(i));
    				if (task.getTaskname().equals("pingtask")){
    					numThreads = task.getPolltime().intValue();
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}					  
		  
  		// 生成线程池
  		ThreadPool threadPool = new ThreadPool(numThreads);														
  		// 运行任务
  		/*for (int i=0; i<nodeList.size(); i++) {
  			HostNode node = (HostNode)nodeList.get(i);
  			threadPool.runTask(createTask(node));
  		}*/
  		threadPool.runTask(createTask(node));
  		// 关闭线程池并等待所有任务完成
  		threadPool.join();
		}
		catch(Exception e){
			//SysLogger.info("error in ExecutePing!"+e.getMessage());
	  	}
		finally{
			//SysLogger.info("********Ping Thread Count : "+Thread.activeCount());
		}		
	}

public HostNode getNode() {
		return node;
	}

	public void setNode(HostNode node) {
		this.node = node;
	}

/**
创建任务
*/	
private static Runnable createTask(final HostNode hostnode) {
return new Runnable() {
    public void run() {
    	Vector vector=null;
    	Hashtable returnhash = new Hashtable();
        try {    
        	
        	PingUtil pingU=new PingUtil(hostnode.getIpAddress());
			Integer[] packet=pingU.ping();
			vector=pingU.addhis(packet); 
			if(vector!=null){				
				returnhash.put("ping", vector);
			}
			 //把数据转换成sql
	        HostnetPingResultTosql tosql=new HostnetPingResultTosql();
	        tosql.CreateResultTosql(returnhash, hostnode.getIpAddress());
	        
        }catch(Exception exc){
        	
        }
    }
};
}

}

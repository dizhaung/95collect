package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.PortInfo;
import com.afunms.polling.om.Portinfocollectdata;
import com.afunms.polling.om.Task;
import com.afunms.topology.dao.DiscoverCompleteDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetPortInfoResulttosql;
import com.gatherResulttosql.NetinterfaceResultTosql;

public class PortInfoTask extends MonitorTask {
	private HostNode node=new HostNode();
	public HostNode getNode() {
		return node;
	}
	public void setNode(HostNode node) {
		this.node = node;
	}
	public void collectData(HostNode node) {

	}
	public Hashtable collect_Data(HostNode host,Calendar date){
		host.setSnmpversion(1);
		SysLogger.info("----------流量信息采集interface start -----"
				+ host.getIpAddress());
		Hashtable returnHash = new Hashtable();
		Vector portInfoVector = new Vector();
		try{
			Portinfocollectdata portinfodata = null;
			PortInfo portInfo = new PortInfo();
			try{
				Hashtable hash = ShareData.getPortinfodata(host.getIpAddress());
				TaskXml taskxml = new TaskXml();
				Task task = taskxml.GetXml("portinfotask");
				int interval = getInterval(task.getPolltime().floatValue(),task.getPolltimeunit());
				if (hash == null)
					hash = new Hashtable();
				String[] oids = new String[] { 
						"1.3.6.1.2.1.4.20.1.1",// ipAdEntAddr
						"1.3.6.1.2.1.4.20.1.2"};// ipAdEntIfIndex
				String[][] valueArray = null;
				try {
					valueArray = SnmpUtils.getTableData(host.getIpAddress(),
							host.getCommunity(), oids, host.getSnmpversion(),
							3, 1000 * 30);
				} catch (Exception e) {
				}
				Vector tempV = new Vector();
				Hashtable tempHash = new Hashtable();
				if (valueArray != null) {
					
					for (int i = 0; i < valueArray.length; i++) {
						if (valueArray[i][0] == null)
							continue;
						portinfodata = new Portinfocollectdata();
						for (int j = 0; j < 2; j++) {
							String sValue = valueArray[i][j];
							
							// 把预期状态和ifLastChange过滤掉
							if (sValue != null){
								portinfodata.setNodeId(host.getIpAddress());
								portinfodata.setCollecttime(sdf.format(date.getTime()));
								if(j==0){
									portinfodata.setIpAddress(sValue);
								}
								if(j==1){
									portinfodata.setPort(sValue);
								}
								
							}
							
						}
						portInfoVector.addElement(portinfodata);
					}
				
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		returnHash.put("portinfo", portInfoVector); // 端口信息
		System.out.println("returnHash大小====="+returnHash.size());
		portInfoVector = null;
		NetPortInfoResulttosql tosql = new NetPortInfoResulttosql();
		tosql.CreateResultTosql(returnHash, host.getIpAddress());
		
        return returnHash;
	}
	
	public int getInterval(float d, String t) {
		int interval = 0;
		if (t.equals("d"))
			interval = (int) d * 24 * 60 * 60; // 天数
		else if (t.equals("h"))
			interval = (int) d * 60 * 60; // 小时
		else if (t.equals("m"))
			interval = (int) d * 60; // 分钟
		else if (t.equals("s"))
			interval = (int) d; // 秒
		return interval;
	}
	@Override
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
    				if (task.getTaskname().equals("portinfotask")){
    					numThreads = task.getPolltime().intValue();
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}					  
		  
    		// 生成线程池
    		ThreadPool threadPool = null;
    		Calendar date=Calendar.getInstance();
    		// 运行任务
    		threadPool = new ThreadPool(82);
    		System.out.println("=============1=============");
    		threadPool.runTask(createTask((HostNode)getNode(),date));
    		System.out.println("=============2=============");
    		threadPool.join();
    		threadPool.close();
    		threadPool = null;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{}	
	}
	private static Runnable createTask(final HostNode host,final Calendar date) {
		return new Runnable() {
			public void run() {
				new PortInfoTask().collect_Data(host,date);
		    
			}
		};
	}
	
	/**
	    * 把'换成_,保证sql不出错
	    */
	private String replace(String oldStr){
		if(oldStr==null) return "";
		
		if(oldStr.length()>45) 
			oldStr = oldStr.substring(0,45);	   
		if(oldStr.indexOf("'")>=0)
			return oldStr.replace('\'','_');
		else
			return oldStr; 
	}
	
	public static void main(String[] args){
		PortInfoTask ptask = new PortInfoTask();
		ptask.run();
	}
}

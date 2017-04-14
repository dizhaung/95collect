/**
 * <p>Description:operate table NMS_INTERFACE</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-09-16
 */

package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.afunms.common.base.*;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.om.IfEntity;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.InterfaceNode;



public class HostInterfaceDao extends BaseDao 
{
   public HostInterfaceDao()
   {
	   super("topo_interface");	   	  
   }
   public List loadall()
   {
	   return findByCriteria("select * from topo_interface"); 
   }
   public boolean deleteByHostId(String nodeid)
	  {	
		   String sql = "delete from topo_interface where node_id='"+nodeid+"'";
		   return saveOrUpdate(sql);			
	  }
   
   public InterfaceNode loadInterface(String nodeId)
   {
	   List retList = new ArrayList();
	   List nodeList = findByCriteria("select * from topo_interface where node_id='"+nodeId+"'"); 
	   if(nodeList != null && nodeList.size()>0){
		   InterfaceNode node = (InterfaceNode)nodeList.get(0);
			   return node;
	   }
	   return null;
   }
   public List loadInterfaces(String nodeId)
   {
	   List hash = new ArrayList();
	   try
	   {
		   rs = conn.executeQuery("select distinct node_id,if_index from topo_interface where node_id='" + nodeId + "' order by if_index");
		   while(rs.next())
		   {
			   InterfaceNode ifnode = new InterfaceNode();
			   ifnode.setIfIndex(rs.getString("if_index"));
			   ifnode.setNodeId(rs.getString("node_id"));
			   hash.add(ifnode);
		   }	   
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("HostInterfaceDao.loadInterfaces()",e);
	   }
	   return hash;
   }
   
   public List loadNode()
   {
	   List hash = new ArrayList();
	   try
	   {
		   rs = conn.executeQuery("select distinct node_id from topo_interface ");
		   while(rs.next())
		   {
			   InterfaceNode ifnode = new InterfaceNode();
			   ifnode.setNodeId(rs.getString("node_id"));
			   hash.add(ifnode);
		   }	   
	   }
	   catch(Exception e)
	   {
		   SysLogger.error("HostInterfaceDao.loadInterfaces()",e);
	   }
	   return hash;
   }
   
   public BaseVo loadFromRS(ResultSet rs)
   {
	   InterfaceNode node = new InterfaceNode();
	   try{
		   node.setId(rs.getInt("id"));
		   node.setNodeId(rs.getString("node_id"));
		   node.setIfIndex(rs.getString("if_index"));
		   node.setIfDesc(rs.getString("if_desc"));
		   node.setCustomerId(rs.getString("customer_id"));
	   }catch(Exception e)
       {
 	       SysLogger.error("HostInterfaceDao.loadFromRS()",e); 
       }
      return node;
   }	   
   
   public synchronized int getNextID(){
		return super.getNextID("topo_interface");
	}
	
}
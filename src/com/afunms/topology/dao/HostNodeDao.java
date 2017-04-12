/**
 * <p>Description:与nodedao都是操作表nms_topo_node,但nodedao主要用于发现</p>
 * <p>Description:而toponodedao主要用于页面操作</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-09-20
 */

package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.base.DaoInterface;
import com.afunms.common.util.SysLogger;
import com.afunms.topology.model.HostNode;
import com.database.DBManager;

public class HostNodeDao extends BaseDao implements DaoInterface
{
   public HostNodeDao()
   {
	   super("topo_host_node");
   }
   

   /**
    * 查找相同ip的网元
    * @param key
    * @param value
    * @return
    *  konglq
    */
   public List findBynode(String key,String value)
   {	  
	   return findByCriteria("select * from topo_host_node where " + key + " = '" + value + "'");   
   }
   
   public List findByCondition(String key,String value)
   {	  
	   return findByCriteria("select * from topo_host_node where " + key + " like '%" + value + "%'");   
   }
   public List findByCondition1(String key,String value)
   {	  
	   return findByCriteria("select * from topo_host_node where " + key + " = '" + value + "'");   
   }
     
   public List loadNetwork(int nodetypeflag)
   {
	   if(nodetypeflag == 1){
		   ////网络设备
		   return findByCriteria("select * from topo_host_node where managed = 1 and (category<4 or category=7 or category=8) order by ip_long"); 
   		}else 
		   return findByCriteria("select * from topo_host_node where category<4 or category=7 order by ip_long");
   }
   

   public List findByIDs(String nodeIDs)
   {
	   List list = new ArrayList();
		  try{
			  rs = conn.executeQuery("select * from topo_host_node where id in(" + nodeIDs +")");
			  while(rs.next())
			  {
				  BaseVo vo = loadFromRS(rs);
				  list.add(vo);
			  }
		  }catch(Exception e){e.printStackTrace();}
		  return list;
   }
   public List loadIsMonitored(int monitorflag)
   {
	   if(monitorflag == 1)
		   return findByCriteria("select * from topo_host_node where managed = 1 order  by category,ip_long"); 
	   else
		   return findByCriteria("select * from topo_host_node where managed = 0 order by category,ip_long");
   }
   
   public List loadIsMonitoredNode()
   {
	   return findByCriteria("select distinct thn.id,thn.ip_address,thn.community from TOPO_HOST_NODE thn,topo_interface ti where thn.ip_address=ti.node_id"); 
   } 
   public List loadNodeInfo(String ip)
   {
	   return findByCriteria("select distinct t.id,t.ip_address,t.community,t.snmpversion from TOPO_HOST_NODE t where t.ip_address='"+ip+"'"); 
   } 
   public HostNode loadHost(int id)
   {
	   
	   List retList = new ArrayList();
	   List nodeList = findByCriteria("select * from topo_host_node where id="+id); 
	   if(nodeList != null && nodeList.size()>0){
			   HostNode node = (HostNode)nodeList.get(0);
			   return node;
	   }
	   return null;
   }

   @Override
   public BaseVo loadFromRS(ResultSet rs)
   {
	   HostNode vo = new HostNode();
       try
       {
		   vo.setId(rs.getInt("id"));
//		   vo.setAssetid(rs.getString("asset_id"));
//		   vo.setLocation(rs.getString("location"));
		   vo.setIpAddress(rs.getString("ip_address"));
//		   vo.setIpLong(rs.getLong("ip_long"));
//		   vo.setSysName(rs.getString("sys_name"));
//		   vo.setAlias(rs.getString("alias"));
//		   vo.setNetMask(rs.getString("net_mask"));
//		   vo.setSysDescr(rs.getString("sys_descr"));
//		   vo.setSysLocation(rs.getString("sys_location"));
//		   vo.setSysContact(rs.getString("sys_contact"));
//		   vo.setSysOid(rs.getString("sys_oid"));
		   vo.setCommunity(rs.getString("community"));
//		   vo.setWriteCommunity(rs.getString("write_community"));
//		   vo.setSnmpversion(rs.getInt("snmpversion"));
//		   vo.setTransfer(rs.getInt("transfer"));
//		   vo.setCategory(rs.getInt("category"));		   
//		   vo.setManaged(rs.getInt("managed")==1?true:false);
//		   vo.setType(rs.getString("type"));
//		   vo.setSuperNode(rs.getInt("super_node"));
//		   vo.setLocalNet(rs.getInt("local_net"));
//		   vo.setLayer(rs.getInt("layer"));
//		   vo.setBridgeAddress(rs.getString("bridge_address"));
//		   vo.setStatus(rs.getInt("status"));
//		   vo.setDiscovertatus(rs.getInt("discoverstatus"));
//		   vo.setOstype(rs.getInt("ostype"));
//		   vo.setCollecttype(rs.getInt("collecttype"));
//		   vo.setSendemail(rs.getString("sendemail"));
//		   vo.setSendmobiles(rs.getString("sendmobiles"));
//		   vo.setSendphone(rs.getString("sendphone"));
//		   vo.setBid(rs.getString("bid"));
//		   vo.setEndpoint(rs.getInt("endpoint"));
//		   vo.setSupperid(rs.getInt("supperid"));//snow add at 2010-05-18
		   //SysLogger.info(vo.getSendemail()+"==="+vo.getBid());
       }
       catch(Exception e)
       {
 	       SysLogger.error("HostNodeDao.loadFromRS()",e); 
       }	   
       return vo;
   }	
   
   public boolean save(BaseVo vo)
   {
	   return false;
   }
   
   public List<String> getIfIps()
   {
	   List<String> allIps = new ArrayList<String>();
	   try
	   {
		   rs = conn.executeQuery("select a.ip_address from topo_interface a,topo_host_node b where a.node_id=b.id and b.category<4 and a.ip_address<>'' order by a.id");
		   while(rs.next())
		   {
		       String ips = rs.getString("ip_address");
		       allIps.add(ips);
		   }
	   }
	   catch(Exception e)
	   {		   
	   }
	   return allIps;
   }
   public String loadOneColFromRS(ResultSet rs){
	   return "";
   }
   
   public int getNodeID(String ip){
	   int nodeID = 0;
	   HostNode vo = (HostNode)findByIpaddress(ip);
       if (null != vo && vo.getIpAddress() != null) {
       		nodeID = vo.getId();
       		if(nodeID == 0)nodeID = getNextID("topo_host_node");
       	}else
       		nodeID = getNextID("topo_host_node");
	   return nodeID;
   }
   
   public int getCountByIpaddress(String ipaddress){
   	   int count = 0;
   	   String sql = "select count(1) from topo_host_node where ip_address = '"+ ipaddress +"'";
   	   try {
   			rs = conn.executeQuery(sql);
   			if(rs.next()){
   				count = rs.getInt(1);
   			}
   	   } catch (SQLException e) {
   			// TODO Auto-generated catch block
   			e.printStackTrace();
   	   }
   	   return count;
      }

      public BaseVo findByIpaddress(String ipaddress){
	   BaseVo hostNode = new BaseVo();
	   String sql = "select * from topo_host_node where ip_address = '"+ ipaddress +"'";
	   try {
			rs = conn.executeQuery(sql);
			if(rs.next()){
				hostNode = loadFromRS(rs);
			}
	   } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	   }
	   return hostNode;
   }
      
    //zhushouzhi----------------------------------
      public List loadall()
      {
   	   return findByCriteria("select * from topo_host_node"); 
      }
      public List all(){
  		return findByCriteria("select * from topo_host_node order by ip_long");
  	   }
      public String loadipaddressbyid(String id)
      { 
    	String ipaddess = "";     
    	String sql = "select ip_address from topo_host_node where id ="+id;
    	 try {
 			rs = conn.executeQuery(sql);
 			if(rs.next()){
 				ipaddess = rs.getString("ip_address");
 			}
 	   } catch (SQLException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 	   }finally{
			//conn.close();
	   }
 	   return ipaddess;
      }


	public boolean update(BaseVo vo) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	public static void main(String[] args){
//		DBManager db = new DBManager();
//		try {
//			
//			for(int i=11;i<114;i++)
//			{
//				String community="sxyd-idc";
//				String sql="insert into topo_host_node (id,ip_address,sys_name,community,managed)"+
//				"values("+i+",'183.203.0."+i+
//			"','183.203.0."+i+
//			"','"+community+
//			"','0')";
//				db.executeUpdate(sql);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		HostNodeDao dao = new HostNodeDao();
		int count=dao.getTableCount("PORTSTATUS183_203_0_113");
		System.out.println(count);
	}
	
	public int getTableCount(String str){
		int count=0;
		count=getNextID(str);
		return count;
	}
	
	
  
}

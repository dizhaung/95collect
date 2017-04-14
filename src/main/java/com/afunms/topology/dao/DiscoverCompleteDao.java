/**
 * <p>Description:operate table NMS_DISCOVER_CONDITION</p>
 * ��Ҫ���ڷ�����֮��������� 
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.dao;

import java.sql.ResultSet;
import java.util.List;

import com.afunms.common.base.BaseDao;
import com.afunms.common.base.BaseVo;
import com.afunms.common.util.CommonUtil;
import com.afunms.topology.model.HostNode;

public class DiscoverCompleteDao extends BaseDao
{
   public DiscoverCompleteDao()
   {
	   super("topo_interface");
   }
   
   /**
    * ���������ӿ�����
    */   
   public void addInterfaceData(HostNode host)
   {
	   int id = getNextID("topo_interface");      

	   List ifList = host.getIfEntityList();
	   if(ifList!=null){
		   for(int j=0;j<ifList.size();j++)
		   {
	 		 com.afunms.util.IfEntity ifEntity = (com.afunms.util.IfEntity)ifList.get(j);
	 		 String physAddress = ifEntity.getPhysAddress();
	 		 physAddress = CommonUtil.removeIllegalStr(physAddress);
	 		 StringBuffer sql = new StringBuffer(300);    		 
	 		 sql.append("insert into topo_interface(id,node_id,entity,descr,port,speed,phys_address,ip_address,oper_status,type,chassis,slot,uport)values(");
	 		 sql.append(id++);
	 		 sql.append(","); 
	 		 sql.append(host.getIpAddress());
	 		 sql.append(",\"");
	 		 sql.append(ifEntity.getIndex());
	 		 sql.append("\",\"");
	 		 sql.append(replace(ifEntity.getDescr())); 
	 		 sql.append("\",\"");
	 		 sql.append(ifEntity.getPort()==null?"":ifEntity.getPort());
	 		 sql.append("\",\"");
	 		 sql.append(ifEntity.getSpeed());
	 		 sql.append("\",\"");
	 		 sql.append(physAddress);
	 		 sql.append("\",\"");
	 		 sql.append(ifEntity.getIpList()); //����IP��ַ
	 		 sql.append("\",");
	 		 sql.append(ifEntity.getOperStatus());
	 		 sql.append(",");
	 		 sql.append(ifEntity.getType()); //�˿�����
	 		 sql.append(",");
	 		 sql.append(ifEntity.getChassis()); //���
	 		 sql.append(",");
	 		 sql.append(ifEntity.getSlot()); //��
	 		 sql.append(",");
	 		 sql.append(ifEntity.getUport()); //��
	 		 sql.append(")");    		 
	 		 conn.executeUpdate(sql.toString(),false);    
	 	 }//end_for_j
	 	 conn.commit();
	 	 conn.executeUpdate("update topo_interface set alias=descr");
	   }
   }

   /**
    * ��'����_,��֤sql������
    */
   private String replace(String oldStr)
   {
	   if(oldStr==null) return "";
	   
	   if(oldStr.length()>45) 
		  oldStr = oldStr.substring(0,45);	   
	   if(oldStr.indexOf("'")>=0)
		  return oldStr.replace('\'','_');
	   else
		  return oldStr; 
   }
     
   public BaseVo loadFromRS(ResultSet rs)
   {
      return null;
   }
   

}

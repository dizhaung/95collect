/**
 * <p>Description:node of topology,all devices are hosts</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-10
 */

package com.afunms.util;

import java.util.*;

import com.afunms.common.util.*;
import com.afunms.polling.task.ThreadPool;

public class Host extends Node
{    
	private int hasDetected;
	private boolean isDiscovered;   
	private boolean linkAnalysed;	
	private int ipTotal;  
	private String alias;
	private String switchIds;
	private int completSwitchs;
	private boolean isRouter;
	private List<String> vlanCommunities;
	private String bridgeAddress;
	
    public Host()
    {   	  	
    	setDiscovered(false);
        setNetMask("255.255.255.0");
        hasDetected = 0;
        ipTotal = -1;
        alias = null;
    }
    
    public void completeOneSwitch()
    {
    	completSwitchs ++;
    }
    
    public boolean allSwitchesFound()
    {
    	if(switchIds==null) return true;
    	String[] ids = switchIds.split(",");
    	if(completSwitchs==ids.length)
    	   return true;
    	else
    	   return false;	
    }
    
    public void setRouter(boolean is)
    {
        isRouter = is;	
    }
    
    public boolean isRouter()
    {
        return isRouter;	
    }
    
    public void addSwitchId(int id)
    {
        if(switchIds==null)
           switchIds = "" + id;
        else
           switchIds += "," + id;
        SysLogger.info(ipAddress + "����һ����,id=" + id);
    }
    
    public String getSwitchIds()
    {
        return switchIds;	
    }
    
	public boolean isDiscovered() {
		return isDiscovered;
	}

	public void setDiscovered(boolean isDiscovered) {
		this.isDiscovered = isDiscovered;
	}
    
    /**
     * ���ӿ������ҵ��ӿ�
     */
    public IfEntity getIfEntityByIndex(String ifIndex)
    {    
    	if(ifEntityList==null||ifEntityList.size()==0)
    		return null;

    	IfEntity ifEntity = null;
    	for(int i=0;i<ifEntityList.size();i++)    	
    	{
    		IfEntity obj = (IfEntity)ifEntityList.get(i);
    		if(obj.getIndex().equals(ifIndex))
    		{
    			ifEntity = obj;
    			break;
    		}	
    	}	
        if(ifEntity == null)    	    	
		   SysLogger.info(ipAddress + "��û������Ϊ" + ifIndex + "�Ľӿ�");
        
		return ifEntity;
    }
    
    /**
     * ���˿��ҵ�һ���ӿ�
     */
    public IfEntity getIfEntityByPort(String port)
    {
    	if(port==null) return null;    	
    	if(ifEntityList==null||ifEntityList.size()==0)
    		return null;	
    	
    	IfEntity ifEntity = null;
    	for(int i=0;i<ifEntityList.size();i++)    	
    	{
    		IfEntity obj = (IfEntity)ifEntityList.get(i);
    		if(obj.getPort().equals(port))
    		{
    			ifEntity = obj;
    			break;
    		}	
    	}
        if(ifEntity == null)    	    	
 		   SysLogger.info(ipAddress + "��û�ж˿�Ϊ" + port + "�Ľӿ�");    	
    	return ifEntity;
    } 
    
    /**
     * �������ҵ�һ���ӿ�
     */
    public IfEntity getIfEntityByDesc(String desc)
    {
    	if(desc==null) return null;    	
    	if(ifEntityList==null||ifEntityList.size()==0)
    		return null;	
    	
    	IfEntity ifEntity = null;
    	for(int i=0;i<ifEntityList.size();i++)    	
    	{
    		IfEntity obj = (IfEntity)ifEntityList.get(i);
    		if(obj.getDescr().equals(desc))
    		{
    			ifEntity = obj;
    			break;
    		}	
    	}
        if(ifEntity == null)    	    	
 		   SysLogger.info(ipAddress + "��û�ж˿�����Ϊ" + desc + "�Ľӿ�");    	
    	return ifEntity;
    }
    /**
     * ��IP�ҵ��ӿ�
     */
    public IfEntity getIfEntityByIP(String ip)
    {    
    	if(ifEntityList==null||ifEntityList.size()==0)
    		return null;	
    	
    	IfEntity ifEntity = null;
    	for(int i=0;i<ifEntityList.size();i++)    	
    	{
    		IfEntity obj = (IfEntity)ifEntityList.get(i);		
    		if(obj.getIpList()!=null)
    		{
    			if(obj.getIpList().split(",").length>0)
    			{
    				int flag = 0;
    				String IPS[] = obj.getIpList().split(",");
    				for(int k=0;k<IPS.length;k++)
    				{
    					//SysLogger.info(this.getIpAddress()+"���нӿڵ�ַ"+IPS[k]+"===="+ip);
    					if(IPS[k].equalsIgnoreCase(ip))
    					{
    						ifEntity = obj;
    						flag = 1;
    		    			break;
    					}
    				}
    				if(flag == 1)break;
    			}
    			else
    			{
    				//SysLogger.info(this.getIpAddress()+"���нӿڵ�ַ"+obj.getIpList()+"====="+ip);
    				if(obj.getIpList().equalsIgnoreCase(ip))
    				{
    					ifEntity = obj;
		    			break;
    				}
    			}
    		}
    	}
		return ifEntity;
    }
    
    /**
     * �ҵ����豸�Ĺ����ַ
     */
    /*
    public String getAdminIp()
    {    
    	if(ifEntityList==null||ifEntityList.size()==0)
    		return null;

    	IfEntity ifEntity = null;
    	for(int i=0;i<ifEntityList.size();i++)    	
    	{
    		IfEntity obj = (IfEntity)ifEntityList.get(i);
    		if(obj.getType() == 24){
    			//ΪLOOPBACK��ַ
    			if(obj.getIpAddress().indexOf("127.0")<0){
    				ifEntity = obj;
    				return obj.getIpAddress();
    			}
    		}
    	}	
        if(ifEntity == null)    	    	
		   SysLogger.info(ipAddress + "��Ӧ���豸��û�й����ַ");        
		return null;
    }
*/
	public String getAlias() 
	{
		return alias;
	}

	public void setAlias(String alias) 
	{
		this.alias = alias;
	}

	public boolean isLinkAnalysed() {
		return linkAnalysed;
	}

	public void setLinkAnalysed(boolean linkAnalysed) {
		this.linkAnalysed = linkAnalysed;
	}

	public void setVlanCommunities(List<String> vlanCommunities) {
		this.vlanCommunities = vlanCommunities;
	}

	public String getBridgeAddress() {
		return bridgeAddress;
	}

	public void setBridgeAddress(String bridgeAddress) {
		this.bridgeAddress = bridgeAddress;
	} 

}

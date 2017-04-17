/**
 * <p>Description:mapping table NMS_TOPO_NODE</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-12
 */

package com.afunms.topology.model;

import java.util.ArrayList;
import java.util.List;

import com.afunms.common.base.BaseVo;
import com.afunms.common.util.SysLogger;
import com.afunms.util.IfEntity;

public class HostNode extends BaseVo
{
	private int id; 
	private String assetid;
	private String location;
	private String ipAddress;
	private long ipLong;	
	private String sysName;
	private String alias;
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostNode other = (HostNode) obj;
		if (id != other.id)
			return false;
		return true;
	}

	private String netMask;  
	private String sysDescr;
	protected String sysLocation;  //ϵͳλ��
    protected String sysContact;  //ϵͳ��ϵ��
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HostNode [id=" + id + ", ipAddress=" + ipAddress
				+ ", community=" + community + ", managed=" + managed + "]";
	}

	private String sysOid;  
	private String community;
	private String writeCommunity; 
	private int snmpversion;
	private int transfer;
	private String type;     //����
	private int category;  
	private int localNet;	
	private boolean managed;
	private String bridgeAddress;
	private int status;//״̬
	private int superNode;    //��һ���ڵ��id
	private int layer;        //��
	private int discoverstatus;//�洢����ظ����ֵ�״̬
	private int collecttype;//���ݲɼ���ʽ  1:snmp 2:shell
	private int ostype;//����ϵͳ����  1:snmp 2:shell
	private String sendmobiles;
	private String sendemail;
	private String sendphone;
	private String bid;
	private int endpoint;//ĩ���豸
	private int supperid;//��Ӧ��id snow add at 2010-5-18
	private int hasDetected;
	private boolean isDiscovered;   
	private boolean linkAnalysed;	
	private int ipTotal;
	private String switchIds;
	private int completSwitchs;
	private boolean isRouter;
	private List<String> vlanCommunities;
	private List ifEntityList=null; //���ж˿�
	private List aliasIPs;//IP����
	private List aliasIfEntitys;//IP����
	protected String mac;       //MAC��ַ
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	public List getAliasIfEntitys() {
		return aliasIfEntitys;
	}

	public void setAliasIfEntitys(List aliasIfEntitys) {
		this.aliasIfEntitys = aliasIfEntitys;
	}
	public List getAliasIPs() {
		return aliasIPs;
	}

	public void setAliasIPs(List aliasIPs) {
		this.aliasIPs = aliasIPs;
	}
	public List getIfEntityList() {
		return ifEntityList;
	}
	//�����豸�Ľӿڱ�,�������豸��IP�����б�,�����ù���IP
		public void setIfEntityList(List ifEntityList) {
			this.ifEntityList = ifEntityList;
			if(ifEntityList != null && ifEntityList.size()>0){
				aliasIPs = new ArrayList();
				aliasIfEntitys = new ArrayList();
				for(int i=0;i<ifEntityList.size();i++){
					IfEntity ifEntity = (IfEntity)ifEntityList.get(i);
					if(ifEntity == null)continue;
					if(mac == null ){
						if(!ifEntity.getPhysAddress().equals("00:00:00:00:00:00") && ifEntity.getType()==117 )mac = ifEntity.getPhysAddress();
					}
					if(ifEntity.getIpAddress() != null && ifEntity.getIpAddress().trim().length()>0){
						aliasIPs.add(ifEntity.getIpAddress());
						//SysLogger.info(ifEntity.getIpAddress()+"===========");
						aliasIfEntitys.add(ifEntity);
						SysLogger.info(ipAddress+"��IP����Ϊ:"+ifEntity.getIpAddress());
					}
				}
			}
		}
	public int getSupperid() {
		return supperid;
	}

	public void setSupperid(int supperid) {
		this.supperid = supperid;
	}
	
	public String getAssetid() {
		return assetid;
	}

	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getCollecttype() {
		return this.collecttype;
	}
	public void setCollecttype(int collecttype) {
		this.collecttype = collecttype;
	}
	
	public int getOstype() {
		return this.ostype;
	}
	public void setOstype(int ostype) {
		this.ostype = ostype;
	}
	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public int getSuperNode() {
		return superNode;
	}

	public void setSuperNode(int superNode) {
		this.superNode = superNode;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public boolean isManaged() {
		return managed;
	}
	public void setManaged(boolean managed) {
		this.managed = managed;
	}
	public String getNetMask() {
		return netMask;
	}
	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}
	public String getSysDescr() {
		return sysDescr;
	}
	public void setSysDescr(String sysDescr) {
		this.sysDescr = sysDescr;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getSysContact() 
	{
		return sysContact;
	}

	public void setSysContact(String sysContact) 
	{
		this.sysContact = sysContact;
	}
	
	public String getSysLocation() 
	{
		return sysLocation;
	}

	public void setSysLocation(String sysLocation) 
	{
		this.sysLocation = sysLocation;
	}
	public String getSysOid() {
		return sysOid;
	}
	public void setSysOid(String sysOid) {
		this.sysOid = sysOid;
	}
	public String getWriteCommunity() {
		return writeCommunity;
	}
	public void setWriteCommunity(String writeCommunity) {
		this.writeCommunity = writeCommunity;
	}
	public int getSnmpversion() {
		return snmpversion;
	}
	public void setSnmpversion(int snmpversion) {
		this.snmpversion = snmpversion;
	}
	
	public int getTransfer() {
		return transfer;
	}

	public void setTransfer(int transfer) {
		this.transfer = transfer;
	}

	public int getLocalNet() {
		return localNet;
	}
	public void setLocalNet(int localNet) {
		this.localNet = localNet;
	} 
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public long getIpLong() {
		return ipLong;
	}
	public void setIpLong(long ipLong) {
		this.ipLong = ipLong;
	}	
	public String getBridgeAddress() {
		return bridgeAddress;
	}

	public void setBridgeAddress(String bridgeAddress) {
		this.bridgeAddress = bridgeAddress;
	}
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getDiscovertatus() {
		return this.discoverstatus;
	}
	public void setDiscovertatus(int discoverstatus) {
		this.discoverstatus = discoverstatus;
	}
	public String getSendmobiles() {
		return sendmobiles;
	}	
	public void setSendmobiles(String sendmobiles) {
		this.sendmobiles = sendmobiles;
	}
	public String getSendemail() {
		return sendemail;
	}	
	public void setSendemail(String sendemail) {
		this.sendemail = sendemail;
	}
	public String getSendphone() {
		return sendphone;
	}	
	public void setSendphone(String sendphone) {
		this.sendphone = sendphone;
	}
	public String getBid() {
		return bid;
	}	
	public void setBid(String bid) {
		this.bid = bid;
	}
	public int getEndpoint() {
		return this.endpoint;
	}
	public void setEndpoint(int endpoint) {
		this.endpoint = endpoint;
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
    

	public boolean isLinkAnalysed() {
		return linkAnalysed;
	}

	public void setLinkAnalysed(boolean linkAnalysed) {
		this.linkAnalysed = linkAnalysed;
	}

	public void setVlanCommunities(List<String> vlanCommunities) {
		this.vlanCommunities = vlanCommunities;
	}
}
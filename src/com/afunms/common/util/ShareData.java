package com.afunms.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import com.afunms.polling.task.MonitorTimer;


public class ShareData {

  public static int count = 0;              // separate for each servlet
  public static int firstipmac =0;
  public static Hashtable sharedata = new Hashtable();  // also shared
  public static Hashtable pingdata = new Hashtable();  // also shared
  public static Hashtable portdata = new Hashtable();  // all port data
  public static Hashtable hostdata = new Hashtable();  // all host and network
  //��������Ϣ
  public static Hashtable sendeddata = new Hashtable();  // all host and network
  public static Hashtable createeventdata = new Hashtable();  // all host and network
  public static Hashtable portsendeddata = new Hashtable();  // all host and network
  public static Hashtable allpingdata = new Hashtable();  // all host and network
  public static Hashtable allportdata = new Hashtable();  // all host and network
  public static Hashtable packsdata = new Hashtable();  // all host and network
  public static Hashtable discardsdata = new Hashtable();  // all host and network
  public static Hashtable errorsdata = new Hashtable();  // all host and network
  public static Hashtable octetsdata = new Hashtable();  // �����ֽ�
  public static Hashtable realoctetsdata = new Hashtable();  // �����ֽ�
  public static Hashtable pksdata = new Hashtable();  // all host and network
  public static Hashtable eventdata = new Hashtable();  // all host and network
  public static Hashtable sameequipsmsdata = new Hashtable();  // all host and network
  //�ڵ��ַPINGֵ
  public static Hashtable relateippingdata = new Hashtable();  // all host and network
  //ipmac
  public static Hashtable relateipmacdata = new Hashtable(); 
  public static Hashtable relatefdbipmacdata = new Hashtable(); 
  public static Hashtable ipmacbanddata = new Hashtable();  
  public static Hashtable fdbipmacbanddata = new Hashtable();
  public static Hashtable iprouterdata = new Hashtable();  // all host and network
  public static Hashtable policydata = new Hashtable();  // all host and network
  public static Hashtable tosroutedata = new Hashtable();  // all host and network
  
  private static Hashtable allLinkData = new Hashtable();//��·��Ϣ  HONGLI 2011-04-13
  private static Hashtable allLinknodeInterfaceData = new Hashtable();//��·�ڵ�ӿ����ݼ��� HONGLI 2011-04-13

  private static Hashtable alarmHashtable = new Hashtable();//�澯��ֵ 2012-08-05
  
  private static Hashtable portinfodata = new Hashtable();

  private static Hashtable utilhdxdata = new Hashtable();
  	
  public static void setPortinfodata(String ip,Hashtable hash){  	
	  portinfodata.put(ip,hash);  	
	  }
	  public static Hashtable getPortinfodata(String ip){
	  	return (Hashtable)portinfodata.get(ip);
	  }
	public static void setUtilhdxdata(String ip,double value){  	
	  utilhdxdata.put(ip,value);  	
	  }
	  public static Double getUtilhdxdata(String ip){
	  	return (Double)utilhdxdata.get(ip);
	  }

public static Hashtable getAlarmHashtable() {
	return alarmHashtable;
}

public static void setAlarmHashtable(Hashtable alarmHashtable) {
	ShareData.alarmHashtable = alarmHashtable;
}

/**
   * �洢���д����ݿ���ȡ�ģ������豸���߷��������Ĳ�������  
   * <p>�ɲμ�NetworkDao.java collectAllNetworkData���� ���ڸ�������ͼUpdateXmlTaskʱ �洢���ݿ���ʵʱ����</p>
   * @author HONGLI 2011-04-19
   */
  private static Hashtable allNetworkData = new Hashtable();  
  
  /**
   * �洢���д����ݿ���ȡ�ģ������豸���߷�����������ͨ������  
   */
  private static Hashtable allNetworkPingData = new Hashtable();
  
  public static Hashtable connectConfigHashtable = new Hashtable(); //pingsnmp  
  
  public static Hashtable portConfigHash = new Hashtable(); //��Ŷ˿�����
  
  public static Hashtable gatherHash = new Hashtable(); //��Ŷ˿�����
  
  public static Hashtable checkEventHash = new Hashtable(); //��Ŷ˿�����
  
  public static Hashtable paramsHash = new Hashtable(); //TELNET����
  
  public static Hashtable aclHash = new Hashtable(); //���ACLƥ��ֵ
  public static Hashtable slaHash = new Hashtable(); //���SLAƥ��ֵ
  
  public static Hashtable businessHash = new Hashtable();//���ҵ�����
  
  /**
   * ��������Ϣ��Ӧ��nms_alarm_correlations
   */
  private static Hashtable Alarmcorrelations = new Hashtable();
  
  public static Hashtable getAlarmcorrelations() {
	return Alarmcorrelations;
}

public static void setAlarmcorrelations(Hashtable alarmcorrelations) {
	Alarmcorrelations = alarmcorrelations;
}

/**
   * ���ÿ5���Ӳɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m5TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ5���Ӳɼ��������ݼ���
   */
  private static Hashtable m5Alldata = new Hashtable();
  
  /**
   * ���ÿ5���Ӳɼ��������У��Ѿ��ɼ�������
   */
  private static int m5CollectedSize;
  
  
  /**
   * ���ÿ��ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> d1TimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ��ɼ��������ݼ���
   */
  private static Hashtable d1Alldata = new Hashtable();
  
  /**
   * ���ÿ��ɼ��������У��Ѿ��ɼ�������
   */
  private static int d1CollectedSize;
  
  
  
  /**
   * ���ÿ5������SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m5HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ5���ӷ������ɼ��������ݼ���
   */
  private static Hashtable m5HostAlldata = new Hashtable();
  
  /**
   * ���ÿ5���ӷ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int m5HostCollectedSize; 
  
  /**
   * ���ÿ1����SNMP�ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> d1HostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ1��������ɼ��������ݼ���
   */
  private static Hashtable d1HostAlldata = new Hashtable();
  
  /**
   * ���ÿ��������ɼ��������У��Ѿ��ɼ�������
   */
  private static int d1HostCollectedSize; 
  
  /**
   * ��ż����·�ɼ���Timer����
   * String:linkid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> linkTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ���ÿ��·�ɼ��������ݼ���
   */
  private static Hashtable linkAlldata = new Hashtable();
  
  /**
   * �����·�ɼ��������У��Ѿ��ɼ�������
   */
  private static int linkCollectedSize; 
  
  /**
   * ������ɼ���Timer����
   * String:linkid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> panelTimerMap = new HashMap<String, MonitorTimer>();
  /**
   * ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int panelCollectedSize;
  
  /**
   * ����豸���µ�Timer����
   * Host 
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> pollTimerMap = new HashMap<String, MonitorTimer>();
  /**
   * ����豸���µ������У��Ѿ��ɼ�������
   */
  private static int pollCollectedSize; 
  
  /**
   * ���XML���µ�Timer����
   * Host 
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> xmlTimerMap = new HashMap<String, MonitorTimer>();
  /**
   * ���XML���µ������У��Ѿ��ɼ�������
   */
  private static int xmlCollectedSize; 
  
  /**
   * ���ÿ5�����ô���ɼ���ʽ�������ɼ���Timer����
   * String:nodeid
   * MonitorTimer:��node��Ӧ��Timer
   */
  private static HashMap<String,MonitorTimer> m5AgentHostTimerMap = new HashMap<String, MonitorTimer>();
  
  /**
   * ��ŷ�����ÿ5����AGENT�������ɼ��������ݼ���
   */
  private static Hashtable m5AgentHostAlldata = new Hashtable();
  
  /**
   * ���ÿ5���ӷ������ɼ��������У��Ѿ��ɼ�������
   */
  private static int m5AgentHostCollectedSize; 
  
  private static Hashtable allportconfigs;//������ж˿�������Ϣ
  public static Hashtable getAllportconfigs() { 
		return allportconfigs;
	}
  
  
  


private static Hashtable Agentalarmlevellist = new Hashtable();//agent�ڵ�澯���ͼ����б�-konglq
private static Hashtable Agentalarmcontrollist = new Hashtable();//agent�ڵ�澯���б�-konglq

  

public static Hashtable getAgentalarmcontrollist() {
	return Agentalarmcontrollist;
}

public static void setAgentalarmcontrollist(Hashtable agentalarmcontrollist) {
	Agentalarmcontrollist = agentalarmcontrollist;
}

public static Hashtable getAgentalarmlevellist() {
	return Agentalarmlevellist;
}

public static void setAgentalarmlevellist(Hashtable agentalarmlevellist) {
	Agentalarmlevellist = agentalarmlevellist;
}

	public static void setAllportconfigs(Hashtable allportconfigs) {
		allportconfigs = allportconfigs;
	}
  
  private static Hashtable allipalias; //���IP-����IP���ձ�
  
  public static Hashtable getAllipalias() { 
		return allipalias;
	}

	public static void setAllipalias(Hashtable allipalias) {
		allipalias = allipalias;
	}
  
  /**
   * ������
   */
  private static Object obj = null;
  
  public synchronized static Object getInstanceOfObject(){
	  if(obj == null){
		  obj = new Object();
	  }
	  return obj;
  }
  
  public static int getM5AgentHostCollectedSize() {
		return m5AgentHostCollectedSize;
	}

	public static void setM5AgentHostCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			m5AgentHostCollectedSize = collectedSize;
		}
	}

	/**
	 * m5CollectedSize����1
	 */
	public static int addM5AgentHostCollectedSize(){
		synchronized(getInstanceOfObject()){
			m5AgentHostCollectedSize = m5AgentHostCollectedSize + 1;
		}
		return m5AgentHostCollectedSize;
	}

	public static Hashtable getM5AgentHostAlldata() { 
		return m5AgentHostAlldata;
	}

	public static void setM5AgentHostAlldata(Hashtable alldata) {
		m5AgentHostAlldata = alldata;
	}

	public static HashMap<String, MonitorTimer> getM5AgentHostTimerMap() {
			return m5AgentHostTimerMap;
	  }

		public static void setM5AgentHostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			m5AgentHostTimerMap = timerMap;
		}
  
  public static int getXmlCollectedSize() {
		return xmlCollectedSize;
	}

	public static void setXmlCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			xmlCollectedSize = collectedSize;
		}
	}

	/**
	 * xmlCollectedSize����1
	 */
	public static int addXmlCollectedSize(){
		synchronized(getInstanceOfObject()){
			xmlCollectedSize = xmlCollectedSize + 1;
		}
		return xmlCollectedSize;
	}
	
	public static HashMap<String, MonitorTimer> getXmlTimerMap() {
		return xmlTimerMap;
}

	public static void setXmlTimerMap(HashMap<String, MonitorTimer> timerMap) {
		xmlTimerMap = timerMap;
	}
	
  
  public static int getPollCollectedSize() {
		return pollCollectedSize;
	}

	public static void setPollCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			pollCollectedSize = collectedSize;
		}
	}

	/**
	 * PollCollectedSize����1
	 */
	public static int addPollCollectedSize(){
		synchronized(getInstanceOfObject()){
			pollCollectedSize = pollCollectedSize + 1;
		}
		return pollCollectedSize;
	}
	
	public static HashMap<String, MonitorTimer> getPollTimerMap() {
		return pollTimerMap;
}

	public static void setPollTimerMap(HashMap<String, MonitorTimer> timerMap) {
		pollTimerMap = timerMap;
	}
  
  public static int getPanelCollectedSize() {
		return panelCollectedSize;
	}

	public static void setPanelCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			panelCollectedSize = collectedSize;
		}
	}

	/**
	 * panelCollectedSize����1
	 */
	public static int addPanelCollectedSize(){
		synchronized(getInstanceOfObject()){
			panelCollectedSize = panelCollectedSize + 1;
		}
		return panelCollectedSize;
	}
	
	public static HashMap<String, MonitorTimer> getPanelTimerMap() {
		return panelTimerMap;
  }

	public static void setPanelTimerMap(HashMap<String, MonitorTimer> timerMap) {
		panelTimerMap = timerMap;
	}
	
  
  public static int getLinkCollectedSize() {
		return linkCollectedSize;
	}

	public static void setLinkCollectedSize(Integer collectedSize) {
		synchronized(getInstanceOfObject()){
			linkCollectedSize = collectedSize;
		}
	}

	/**
	 * linkCollectedSize����1
	 */
	public static int addLinkCollectedSize(){
		synchronized(getInstanceOfObject()){
			linkCollectedSize = linkCollectedSize + 1;
		}
		return linkCollectedSize;
	}

	public static Hashtable getLinkAlldata() { 
		return linkAlldata;
	}

	public static void setLinkAlldata(Hashtable alldata) {
		linkAlldata = alldata;
	}

	public static HashMap<String, MonitorTimer> getLinkTimerMap() {
			return linkTimerMap;
	  }

		public static void setLinkTimerMap(HashMap<String, MonitorTimer> timerMap) {
			linkTimerMap = timerMap;
		}  
  
public static int getM5CollectedSize() {
	return m5CollectedSize;
}

public static void setM5CollectedSize(Integer collectedSize) {
	synchronized(getInstanceOfObject()){
		m5CollectedSize = collectedSize;
	}
}

/**
 * m5CollectedSize����1
 */
public static int addM5CollectedSize(){
	synchronized(getInstanceOfObject()){
		m5CollectedSize = m5CollectedSize + 1;
	}
	return m5CollectedSize;
}

public static Hashtable getM5Alldata() { 
	return m5Alldata;
}

public static void setM5Alldata(Hashtable alldata) {
	m5Alldata = alldata;
}

public static HashMap<String, MonitorTimer> getM5TimerMap() {
		return m5TimerMap;
  }

	public static void setM5TimerMap(HashMap<String, MonitorTimer> timerMap) {
		m5TimerMap = timerMap;
	}
	/**
		* 1��ɼ�
		* @return
		*/
		public static int getD1CollectedSize() {
			return d1CollectedSize;
		}

		public static void setD1CollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				d1CollectedSize = collectedSize;
			}
		}

		/**
		* d1CollectedSize����1
		*/
		public static int addD1CollectedSize(){
			synchronized(getInstanceOfObject()){
				d1CollectedSize = d1CollectedSize + 1;
			}
			return d1CollectedSize;
		}

		public static Hashtable getD1Alldata() { 
			return d1Alldata;
		}

		public static void setD1Alldata(Hashtable alldata) {
			d1Alldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getD1TimerMap() {
			return d1TimerMap;
		}

		public static void setD1TimerMap(HashMap<String, MonitorTimer> timerMap) {
			d1TimerMap = timerMap;
		}
		
		/**
		* ������5���Ӳɼ�
		* @return
		*/
		public static int getM5HostCollectedSize() {
			return m5HostCollectedSize;
		}

		public static void setM5HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				m5HostCollectedSize = collectedSize;
			}
		}

		/**
		* m5HostCollectedSize����1
		*/
		public static int addM5HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				m5HostCollectedSize = m5HostCollectedSize + 1;
			}
			return m5HostCollectedSize;
		}

		public static Hashtable getM5HostAlldata() { 
			return m5HostAlldata;
		}

		public static void setM5HostAlldata(Hashtable alldata) {
			m5HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getM5HostTimerMap() {
			return m5HostTimerMap;
		}

		public static void setM5HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			m5HostTimerMap = timerMap;
		}		
		
		/**
		* ������1��ɼ�
		* @return
		*/
		public static int getD1HostCollectedSize() {
			return d1HostCollectedSize;
		}

		public static void setD1HostCollectedSize(Integer collectedSize) {
			synchronized(getInstanceOfObject()){
				d1HostCollectedSize = collectedSize;
			}
		}

		/**
		* d1HostCollectedSize����1
		*/
		public static int addD1HostCollectedSize(){
			synchronized(getInstanceOfObject()){
				d1HostCollectedSize = d1HostCollectedSize + 1;
			}
			return d1HostCollectedSize;
		}

		public static Hashtable getD1HostAlldata() { 
			return d1HostAlldata;
		}

		public static void setD1HostAlldata(Hashtable alldata) {
			d1HostAlldata = alldata;
		}

		public static HashMap<String, MonitorTimer> getD1HostTimerMap() {
			return d1HostTimerMap;
		}

		public static void setD1HostTimerMap(HashMap<String, MonitorTimer> timerMap) {
			d1HostTimerMap = timerMap;
		}
		

		
  
  public static Hashtable getAllNetworkData() {
	return allNetworkData;
}


public static Hashtable getAllNetworkPingData() {
	return allNetworkPingData;
}


public static void setAllNetworkPingData(Hashtable allNetworkPingData) {
	ShareData.allNetworkPingData = allNetworkPingData;
}


public static void setAllNetworkData(Hashtable allNetworkData) {
	ShareData.allNetworkData = allNetworkData;
}





	public static Hashtable getAllLinknodeInterfaceData() {
	return allLinknodeInterfaceData;
}


public static void setAllLinknodeInterfaceData(
		Hashtable allLinknodeInterfaceData) {
	ShareData.allLinknodeInterfaceData = allLinknodeInterfaceData;
}


  
  public static Hashtable getAllLinkData() {
		return allLinkData;
	}


	public static void setAllLinkData(Hashtable allLinkData) {
		ShareData.allLinkData = allLinkData;
	}


  public static int runflag =0;
  public static int runserviceflag =0;

  
  public static int getRunflag(){
	  return runflag;
  }
  public static void setRunflag(int _runflag){
	  runflag = _runflag;
  }
  
  public static int getRunserviceflag(){
	  return runserviceflag;
  }
  public static void setRunserviceflag(int _runserviceflag){
	  runserviceflag = _runserviceflag;
  }

  public static Date formerUpdateTime = new Date();

  public static void setFormerUpdateTime(Date date){
	  formerUpdateTime = date;
  }
  public static Date getFormerUpdateTime(){
	  if (formerUpdateTime == null) formerUpdateTime = new Date();
	  return formerUpdateTime;
  }
  
  public static void setPolicydata(String ip,Hashtable hash){
	  policydata.put(ip,hash);
	  }
  public static Hashtable getPolicydata(){
	  	return policydata;
  }
  

  public static void setTosroutedata(String ip,List hash){
	  tosroutedata.put(ip,hash);
	  }
  public static Hashtable getTosroutedata(){
	  	return tosroutedata;
	  }
  
   public static void setIprouterdata(String ip,Vector v){
  	iprouterdata.put(ip,v);
  }
  public static Hashtable getIprouterdata(){
  	return iprouterdata;
  }
  
  public static Hashtable getIpmacbanddata(){
  	return ipmacbanddata;
  }
  public static Hashtable getFdbipmacbanddata(){
	  	return fdbipmacbanddata;
	  }

  public static void setRelateipmacdata(String ip,Vector v){
  	relateipmacdata.put(ip,v);
  }
  public static Hashtable getRelateipmacdata(){
  	return relateipmacdata;
  }
  
  public static void setRelatefdbipmacdata(String ip,Vector v){
	  	relatefdbipmacdata.put(ip,v);
	  }
	  public static Hashtable getRelatefdbipmacdata(){
	  	return relatefdbipmacdata;
	  }

  public static void setRelateippingdata(String ip,Vector v){
  	relateippingdata.put(ip,v);
  }
  public static Hashtable getRelateippingdata(){
  	return relateippingdata;
  }

  public static void setSameequipsmsdata(String ip,Vector v){  	
  	sameequipsmsdata.put(ip,v);  	
  }
  public static void refreshSameequipsmsdata(){  	
  	sameequipsmsdata = new Hashtable();  	
  }
  public static Hashtable getSameequipsms(){
  	return sameequipsmsdata;
  }
  public static Vector getSameequipsmsdata(String ip){
  	return (Vector)sameequipsmsdata.get(ip);
  }

  
  
  
  public static void setEventdata(String ip,Vector v){  	
  	eventdata.put(ip,v);  	
  }
  public static Vector getEventdata(String ip){
  	return (Vector)eventdata.get(ip);
  }
  
  public static void setOctetsdata(String ip,Hashtable hash){  	
  	octetsdata.put(ip,hash);  	
  }
  public static Hashtable getOctetsdata(String ip){
  	return (Hashtable)octetsdata.get(ip);
  }
  
  public static void setRealOctetsdata(String ip,Hashtable hash){  	
	  	realoctetsdata.put(ip,hash);  	
	  }
	  public static Hashtable getRealOctetsdata(String ip){
	  	return (Hashtable)realoctetsdata.get(ip);
	  }
  
  public static void setPksdata(String ip,Hashtable hash){  	
	  	pksdata.put(ip,hash);  	
	  }
	  public static Hashtable getPksdata(String ip){
	  	return (Hashtable)pksdata.get(ip);
	  }
  
  
  public static void setPacksdata(String ip,Hashtable hash){  	
  	packsdata.put(ip,hash);  	
  }
  public static Hashtable getPacksdata(String ip){
  	return (Hashtable)packsdata.get(ip);
  }
  
  public static void setDiscardsdata(String ip,Hashtable hash){
  	discardsdata.put(ip,hash);
  }
  public static Hashtable getDiscardsdata(String ip){
  	return (Hashtable)discardsdata.get(ip);
  }
  public static void setErrorsdata(String ip,Hashtable hash){
  	errorsdata.put(ip,hash);
  }
  public static Hashtable getErrorsdata(String ip){
  	return (Hashtable)errorsdata.get(ip);
  }

  
  public static void setCount(int acount){
  	count = acount;
  }
  public static int getCount(){
  	return count;
  }

  public static void setFirstipmac(int afirstipmac){
  	firstipmac = afirstipmac;
  }
  public static int getFirstipmac(){
  	return firstipmac;
  }
  
  
  public static void setPingdat(Hashtable pdata){
  	pingdata = pdata;
  }
  public static void setPingdata(String ipaddress,Vector v){
  	if (pingdata.containsKey(ipaddress))
  		pingdata.remove(ipaddress);
  		pingdata.put(ipaddress,v);
  }
  

  public static Hashtable getPingdata(){ 
  	return pingdata;
  }
  public static void setSharedata(Hashtable sdata){
  	sharedata = sdata;
  }
  public static void setSharedata(String ipaddress,Vector v){
  	//sharedata = sdata;
  	if (sharedata.containsKey(ipaddress))
  		sharedata.remove(ipaddress);
  	sharedata.put(ipaddress,v);
  }
  
  public static void setLastPortdata(Hostlastcollectdata lastdata){
  	portdata.put(lastdata.getIpaddress()+"."+lastdata.getSubentity(),lastdata);
  }
   public static Hashtable getSendeddata(){
  	return sendeddata;
  }
  public static Hashtable getCreateEventdata(){
	  	return createeventdata;
	  }
  public static Hashtable getPortsendeddata(){
  	return portsendeddata;
  }
  public static Hashtable getAllpingdata(){
  	return allpingdata;
  }
  public static Hashtable getAllportdata(){
  	return allportdata;
  }

  public static Hashtable getPortdata(){
  	return portdata;
  }
  
  public static void setLastHostdata(Hostlastcollectdata lastdata){
  	hostdata.put(lastdata.getIpaddress(),lastdata);
  }
  
  public static Hashtable getHostdata(){
  	return hostdata;
  }
  public static void setLastMemdata(String index,String value){
  	hostdata.put(index,value);
  }
  
  public static Hashtable getSharedata(){
  	if (sharedata == null )sharedata=new Hashtable();
  	return sharedata;
  }
  
  public static Hashtable informixmonitordata = new Hashtable(); // �洢informix�ɼ���������Ϣ

	public static void setInfomixmonitordata(String ip, Hashtable hash) {
		informixmonitordata.put(ip, hash);
	}
public static Hashtable getInformixmonitordata() {
		return informixmonitordata;
	}


public static Hashtable getConnectConfigHashtable() {
	return connectConfigHashtable;
}


public static void setConnectConfigHashtable(Hashtable connectConfigHashtable) {
	ShareData.connectConfigHashtable = connectConfigHashtable;
}


public static Hashtable getPortConfigHash() {
	return portConfigHash;
}


public static void setPortConfigHash(Hashtable portConfigHash) {
	ShareData.portConfigHash = portConfigHash;
}

public static Hashtable getGatherHash() {
	return gatherHash;
}


public static void setGatherHash(Hashtable gatherHash) {
	ShareData.gatherHash = gatherHash;
}


public static Hashtable getCheckEventHash() {
	return checkEventHash;
}


public static void setCheckEventHash(Hashtable checkEventHash) {
	ShareData.checkEventHash = checkEventHash;
}

public static Hashtable getParamsHash() {
	return paramsHash;
}

public static void setParamsHash(Hashtable paramsHash) {
	ShareData.paramsHash = paramsHash;
}

	public static Hashtable getAclHash(String ip) {
		return (Hashtable)aclHash.get(ip); 
	}
	
	public static void setAclHash(String ip,Hashtable hash) {
		aclHash.put(ip,hash);
	}
	public static Hashtable getSlaHash() {
		return slaHash; 
	}
	
	public static void setSlaHash(String configid,Hashtable hash) {
		slaHash.put(configid,hash);
	}

	public static Hashtable getBusinessHash() {
		return businessHash;
	}

	public static void setBusinessHash(Hashtable businessHash) {
		ShareData.businessHash = businessHash;
	}

	  private static Hashtable allipaliasVSip; //��ű���IP-IP���ձ�
	  
	  public static Hashtable getAllipaliasVSip() { 
			return allipaliasVSip;
		}

		public static void setAllipaliasVSip(Hashtable _allipaliasVSip) {
			allipaliasVSip = _allipaliasVSip;
		}
		private static Hashtable resourceConfHashtable=new Hashtable();
		public static Hashtable getResourceConfHashtable() {
			return resourceConfHashtable;
		}

		public static void setResourceConfHashtable(Hashtable resourceConfHashtable) {
			ShareData.resourceConfHashtable = resourceConfHashtable;
		}
	
	
	
	
}

package com.afunms.polling.task;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.send.SendMailAlarm;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.Arith;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.NodeAlarmUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Huaweitelnetconf;
import com.afunms.config.model.Portconfig;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.polling.task.TaskXml;
import com.afunms.polling.telnet.Huawei3comvpn;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetInterfaceDataTemptosql;
import com.gatherResulttosql.NetinterfaceResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class InterfaceSnmp extends SnmpMonitor {
	
	private static Hashtable ifEntity_ifStatus = null;
	static {
		ifEntity_ifStatus = new Hashtable();
		ifEntity_ifStatus.put("1", "up");
		ifEntity_ifStatus.put("2", "down");
		ifEntity_ifStatus.put("3", "testing");
		ifEntity_ifStatus.put("5", "unknow");
		ifEntity_ifStatus.put("7", "unknow");
	};
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public InterfaceSnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		//SysLogger.info("-----------------------------interface start -----");
			Hashtable returnHash=new Hashtable();
			Vector interfaceVector=new Vector();
			Vector utilhdxVector = new Vector();
			Vector allutilhdxVector = new Vector();
			Vector packsVector = new Vector();
			Vector inpacksVector = new Vector();
			Vector outpacksVector = new Vector();
			Vector inpksVector = new Vector();
			Vector outpksVector = new Vector();
			Vector discardspercVector = new Vector();
			Vector errorspercVector = new Vector();
			Vector allerrorspercVector = new Vector();
			Vector alldiscardspercVector = new Vector();
			Vector allutilhdxpercVector=new Vector();
			Vector utilhdxpercVector = new Vector();
			Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
			//SysLogger.info(host.getIpAddress()+"-----------------------------interface start -----"+host.getBid());
			try {
				Interfacecollectdata interfacedata=null;
				UtilHdx utilhdx=new UtilHdx();
				InPkts inpacks = new InPkts();
				OutPkts outpacks = new OutPkts();
				UtilHdxPerc utilhdxperc=new UtilHdxPerc();
				AllUtilHdx allutilhdx = new AllUtilHdx();
				Calendar date=Calendar.getInstance();
			
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
					Date cc = date.getTime();
					String time = sdf.format(cc);
					snmpnode.setLastTime(time);
				}catch(Exception e){
				  
				}
			  //-------------------------------------------------------------------------------------------interface start			
			  try{
				I_HostLastCollectData lastCollectDataManager=new HostLastCollectDataManager();
				Hashtable hash=ShareData.getOctetsdata(host.getIpAddress());
				//ȡ����ѯ���ʱ��
				TaskXml taskxml=new TaskXml();
				Task task=taskxml.GetXml("netcollecttask");
				int interval=getInterval(task.getPolltime().floatValue(),task.getPolltimeunit());
				Hashtable hashSpeed=new Hashtable();
				Hashtable octetsHash = new Hashtable();
				if (hash==null)hash=new Hashtable();
						  String[] oids =                
							  new String[] {               
								"1.3.6.1.2.1.2.2.1.1", 
								"1.3.6.1.2.1.2.2.1.2",
								"1.3.6.1.2.1.2.2.1.3",
								"1.3.6.1.2.1.2.2.1.4",
								"1.3.6.1.2.1.2.2.1.5"
//								"1.3.6.1.2.1.2.2.1.6",
//								"1.3.6.1.2.1.2.2.1.7",//ifAdminStatus 6
//								"1.3.6.1.2.1.2.2.1.8",//ifOperStatus 7
//								"1.3.6.1.2.1.2.2.1.9",//ifLastChange 8
//								"1.3.6.1.2.1.31.1.1.1.1"				
								  };
						  String[] oids2 =                
							  new String[] {               
//								"1.3.6.1.2.1.2.2.1.1", 
//								"1.3.6.1.2.1.2.2.1.2",
//								"1.3.6.1.2.1.2.2.1.3",
//								"1.3.6.1.2.1.2.2.1.4",
//								"1.3.6.1.2.1.2.2.1.5",
								"1.3.6.1.2.1.2.2.1.6",
								"1.3.6.1.2.1.2.2.1.7",//ifAdminStatus 6
								"1.3.6.1.2.1.2.2.1.8",//ifOperStatus 7
								"1.3.6.1.2.1.2.2.1.9",//ifLastChange 8
								"1.3.6.1.2.1.31.1.1.1.1"				
								  };						  
						  
						  
						  
				String[] oids1=                
					 new String[] {     	
					"1.3.6.1.2.1.2.2.1.10",  //ifInOctets 0        
					"1.3.6.1.2.1.31.1.1.1.2",//ifInMulticastPkts 1
					"1.3.6.1.2.1.31.1.1.1.3",//ifInBroadcastPkts 2
					"1.3.6.1.2.1.2.2.1.13",//ifInDiscards 3
					"1.3.6.1.2.1.2.2.1.14"//ifInErrors 4
//					"1.3.6.1.2.1.2.2.1.16", //ifOutOctets 5
//					"1.3.6.1.2.1.31.1.1.1.4",//ifOutMulticastPkts 6
//					"1.3.6.1.2.1.31.1.1.1.5",//ifOutBroadcastPkts 7
//					"1.3.6.1.2.1.2.2.1.19",	//ifOutDiscards 8
//					"1.3.6.1.2.1.2.2.1.20"//ifOutErrors 9								
					};	
				
				String[] oids3=                
					 new String[] {     	
//					"1.3.6.1.2.1.2.2.1.10",  //ifInOctets 0        
//					"1.3.6.1.2.1.31.1.1.1.2",//ifInMulticastPkts 1
//					"1.3.6.1.2.1.31.1.1.1.3",//ifInBroadcastPkts 2
//					"1.3.6.1.2.1.2.2.1.13",//ifInDiscards 3
//					"1.3.6.1.2.1.2.2.1.14",//ifInErrors 4
					"1.3.6.1.2.1.2.2.1.16", //ifOutOctets 5
					"1.3.6.1.2.1.31.1.1.1.4",//ifOutMulticastPkts 6
					"1.3.6.1.2.1.31.1.1.1.5",//ifOutBroadcastPkts 7
					"1.3.6.1.2.1.2.2.1.19",	//ifOutDiscards 8
					"1.3.6.1.2.1.2.2.1.20"//ifOutErrors 9								
					};
								
				
				final String[] desc=SnmpMibConstants.NetWorkMibInterfaceDesc0;
				final String[] unit=SnmpMibConstants.NetWorkMibInterfaceUnit0;
				final String[] chname=SnmpMibConstants.NetWorkMibInterfaceChname0;
				final int[] scale=SnmpMibConstants.NetWorkMibInterfaceScale0;
				final String[] desc1=SnmpMibConstants.NetWorkMibInterfaceDesc1;
				final String[] chname1=SnmpMibConstants.NetWorkMibInterfaceChname1;
				final String[] unit1=SnmpMibConstants.NetWorkMibInterfaceUnit1;
				final int[] scale1=SnmpMibConstants.NetWorkMibInterfaceScale1;
				
				String[][] valueArray = null;   
				try {
					//SysLogger.info(host.getIpAddress()+"==="+host.getCommunity()+"===="+host.getSnmpversion());
					valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids, host.getSnmpversion(), 3, 1000*30);
					//valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
				} catch(Exception e){
				}
				
				String[][] valueArray2 = null;   
				try {
					//SysLogger.info(host.getIpAddress()+"==="+host.getCommunity()+"===="+host.getSnmpversion());
					valueArray2 = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids2, host.getSnmpversion(), 3, 1000*30);
					//valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
				} catch(Exception e){
				}
				
				String[][] valueArray1 = null;   	  
				try {
					//valueArray1 = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids1);
					valueArray1 = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids1, host.getSnmpversion(), 3, 1000*30);
				} catch(Exception e){
				}
				String[][] valueArray3 = null;   	  
				try {
					//valueArray1 = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids1);
					valueArray3 = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids3, host.getSnmpversion(), 3, 1000*30);
				} catch(Exception e){
				}
				long allSpeed=0;
				long allOutOctetsSpeed=0;
				long allInOctetsSpeed=0;
				long allOctetsSpeed=0;
				
				long allinpacks=0;
				long inupacks=0;//��ڵ����
				long innupacks=0;//�ǵ���
				long indiscards=0;
				long inerrors=0;
				long alloutpacks=0;
				long outupacks=0;//���ڵ���
				long outnupacks=0;//�ǵ���
				long outdiscards=0;
				long outerrors=0;
				long alldiscards=0;
				long allerrors=0;
				long allpacks=0;
			
				Vector tempV=new Vector();
				Hashtable tempHash = new Hashtable();
				if(valueArray != null){
			   	  for(int i=0;i<valueArray.length;i++)
			   	  {				   		  
			   		  if(valueArray[i][0] == null)continue;
						String sIndex=valueArray[i][0].toString();				
						tempV.add(sIndex);
						tempHash.put(i, sIndex);						
						for(int j=0;j<5;j++){								
								String sValue=valueArray[i][j];									
								interfacedata=new Interfacecollectdata();
								interfacedata.setIpaddress(host.getIpAddress());
								interfacedata.setCollecttime(date);
								interfacedata.setCategory("Interface");
								//if (desc[j].equals("ifAdminStatus"))continue;
								interfacedata.setEntity(desc[j]);
								interfacedata.setSubentity(sIndex);
								//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
								interfacedata.setRestype("static");
								interfacedata.setUnit(unit[j]);
								

								if((j==4)&&sValue!=null){
									//����
									long lValue=Long.parseLong(sValue);//yangjun
										hashSpeed.put(sIndex,Long.toString(lValue));
									allSpeed=allSpeed+lValue;					
								}
								if((j==2)&&sValue!=null){//�˿�����
									if (Interface_IfType.get(sValue) != null){
										interfacedata.setThevalue(Interface_IfType.get(sValue).toString());
									}else{
										interfacedata.setThevalue("0.0");	
									}
								}else{
									if(scale[j]==0){
										interfacedata.setThevalue(sValue);
									}
									else{
										if(sValue != null){
											interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale[j]));
										}else{
											interfacedata.setThevalue("0");
										}
										
									}
								}								
								interfacedata.setChname(chname[j]);
								if("ifPhysAddress".equals(interfacedata.getEntity())){
									if((interfacedata.getThevalue()==null)||(interfacedata.getThevalue().length() > 0 && !interfacedata.getThevalue().contains(":"))){//mac��ַ�ַ������ܳ���
//										SysLogger.info("ifPhysAddress--����--" + interfacedata.getThevalue());
										interfacedata.setThevalue("--");
									}
								}
								interfaceVector.addElement(interfacedata);
						   } //end for j
			   	  } //end for valueArray
				}
				
				
				if(valueArray2 != null){
					List portconfiglist = new ArrayList();			

					Portconfig portconfig = null;
					
					Hashtable allportconfighash = ShareData.getPortConfigHash();;
					if(allportconfighash != null && allportconfighash.size()>0){
						if(allportconfighash.containsKey(host.getIpAddress())){
							portconfiglist = (List)allportconfighash.get(host.getIpAddress());
						}
					}				
					Hashtable portconfigHash = new Hashtable();

					if(portconfiglist != null && portconfiglist.size()>0){
						for(int i=0;i<portconfiglist.size();i++){
							portconfig = (Portconfig)portconfiglist.get(i);
							portconfigHash.put(portconfig.getPortindex()+"", portconfig);
						}
					}
					portconfig = null;
					
			   	  for(int i=0;i<valueArray2.length;i++)
			   	  {				   		  
			   		String sIndex = (String)tempHash.get(i);				
					if (tempV.contains(sIndex)){
						if(valueArray2[i][0] == null)continue;
						
						for(int j=0;j<5;j++){
								//��Ԥ��״̬��ifLastChange���˵�
								//if (j==6 || j==8)continue;
								if (j==3)continue;
								
								String sValue=valueArray2[i][j];	
								
								interfacedata=new Interfacecollectdata();
								interfacedata.setIpaddress(host.getIpAddress());
								interfacedata.setCollecttime(date);
								interfacedata.setCategory("Interface");
								//if (desc[j].equals("ifAdminStatus"))continue;
								interfacedata.setEntity(desc[5+j]);
								interfacedata.setSubentity(sIndex);
								//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
								interfacedata.setRestype("static"); 
								interfacedata.setUnit(unit[5+j]);
								if((j==1 || j==2)&&sValue!=null){//Ԥ��״̬�͵�ǰ״̬
		
									if (ifEntity_ifStatus.get(sValue) != null){
										interfacedata.setThevalue(ifEntity_ifStatus.get(sValue).toString());
                                       
									}else{
										interfacedata.setThevalue("0.0");
									}
									if(j == 2){
										//�˿ڸ澯�ж�yangjun
										if(portconfigHash.containsKey(sIndex+"")){
											//�����ڶ˿�DOWN�澯����
											portconfig = (Portconfig)portconfigHash.get(sIndex+"");
											try {
												if (portconfig != null){
													if (portconfig.getSms().intValue()==1){
														//SysLogger.info("sValue-----------------"+sValue);
														if(ifEntity_ifStatus.get(sValue) != null){
															if (!"up".equalsIgnoreCase(ifEntity_ifStatus.get(sValue).toString())){
																Host _host = (Host) PollingEngine.getInstance().getNodeByID(host.getId());
																_host.setAlarm(true);
																_host.setStatus(3);
																_host.setAlarmlevel(3);
																_host.getAlarmMessage().add(" �˿� "+portconfig.getName()+" "+portconfig.getLinkuse()+" down");
																
																String userids = _host.getSendemail();
												        		if(userids != null && userids.trim().length()>0){
												        			SendMailAlarm sendMailAlarm = new SendMailAlarm();
													        		try{
													        			EventList eventList = new EventList();
													        			eventList.setContent(host.getIpAddress()+" �˿� "+portconfig.getName()+" "+portconfig.getLinkuse()+" down");
													        			sendMailAlarm.sendAlarm(eventList, userids);
													        			NodeAlarmUtil.saveNodeAlarmInfo(eventList, "interface");
													        		}catch(Exception e){
													        			e.printStackTrace();
													        		}
												        		}
																
																createSMS("net","interface",host.getIpAddress(),host.getId()+"",host.getIpAddress()+" �˿� "+portconfig.getName()+" "+portconfig.getLinkuse()+" down",2,1,sIndex,host.getBid());
															}
														}
														
													}
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
			 							
									}
									
								}
								else{
									if(scale[5+j]==0){
										interfacedata.setThevalue(sValue);
									}
									else{
										if(sValue != null){
											interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale[5+j]));
										}else{
											interfacedata.setThevalue("0");
										}
										
									}
								}
								
								interfacedata.setChname(chname[5+j]);
								if("ifPhysAddress".equals(interfacedata.getEntity())){
									if((interfacedata.getThevalue()==null)||(interfacedata.getThevalue().length() > 0 && !interfacedata.getThevalue().contains(":"))){//mac��ַ�ַ������ܳ���
//										SysLogger.info("ifPhysAddress--����--" + interfacedata.getThevalue());
										interfacedata.setThevalue("--");
									}
								}
								interfaceVector.addElement(interfacedata);
						   } //end for j
					}
			   		  
			   	  } //end for valueArray
				}
				
				Hashtable packhash=new Hashtable();
				Hashtable discardshash=new Hashtable();
				Hashtable errorshash=new Hashtable();
				
				if(valueArray1!= null){
					for(int i=0;i<valueArray1.length;i++){
						allinpacks=0;
						inupacks=0;//��ڹ㲥
						innupacks=0;//��ڶಥ
						indiscards=0;
						inerrors=0;																		

						String sIndex = (String)tempHash.get(i);				
						if (tempV.contains(sIndex)){
										
							for(int j=0;j<5;j++){																														
								if(valueArray1[i][j]!=null){
									String sValue=valueArray1[i][j];
									interfacedata=new Interfacecollectdata();
									if(scale1[j]==0){
										interfacedata.setThevalue(sValue);
									}
									else{
										interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale1[j]));
									}
									if (j==1 || j==2){
										//��ڹ㲥���ݰ�,��ڶಥ���ݰ�												
										if (sValue != null){
											allinpacks=allinpacks+Long.parseLong(sValue);											
											Calendar cal=(Calendar)hash.get("collecttime");
											long timeInMillis=0;
											if(cal!=null)timeInMillis=cal.getTimeInMillis();
											long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
											
											inpacks=new InPkts();
											inpacks.setIpaddress(host.getIpAddress());
											inpacks.setCollecttime(date);
											inpacks.setCategory("Interface");
											String chnameBand="";
											if(j==1){
												inpacks.setEntity("ifInMulticastPkts");
												chnameBand="�ಥ";
											}
											if(j==2){
												inpacks.setEntity("ifInBroadcastPkts");
												chnameBand="�㲥";
											}
											inpacks.setSubentity(sIndex);
											inpacks.setRestype("dynamic");
											inpacks.setUnit("");	
											inpacks.setChname(chnameBand);
											long currentPacks=Long.parseLong(sValue);												
											long lastPacks=0;	
											long l=0;											
													
											//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
											if(longinterval<2*interval){
												String lastvalue="";
												
												if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
												//ȡ���ϴλ�õ�Octets
												if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
											}
								
											if(longinterval!=0){
												if(currentPacks<lastPacks){
													currentPacks=currentPacks+4294967296L;
												}
												//������-ǰ����
												//SysLogger.info(host.getIpAddress()+"==="+sIndex+"�Ͽ�==="+currentPacks+"===="+lastPacks);
												long octetsBetween=currentPacks-lastPacks;
												l=octetsBetween;
												if(lastPacks == 0)l=0;
											}
											inpacks.setThevalue(Long.toString(l));	
											//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"inpacks "+Long.toString(l));
											if (cal != null)
												inpacksVector.addElement(inpacks);	
										}
											//continue;
									}
								if (j==3){
									//��ڶ��������ݰ�
									if (sValue != null) indiscards=Long.parseLong(sValue);
										continue;
								}
								if (j==4){
									//��ڴ�������ݰ�
									if (sValue != null) inerrors=Long.parseLong(sValue);
									continue;
								}			
								Calendar cal=(Calendar)hash.get("collecttime");
								long timeInMillis=0;
								if(cal!=null)timeInMillis=cal.getTimeInMillis();
								long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;											

								//����ÿ���˿����ټ�������
								if(j==0){
									utilhdx=new UtilHdx();
									utilhdx.setIpaddress(host.getIpAddress());
									utilhdx.setCollecttime(date);
									utilhdx.setCategory("Interface");
									String chnameBand="";
									if(j==0){
										utilhdx.setEntity("InBandwidthUtilHdx");
										chnameBand="���";
									}
//									if(j==5){
//										utilhdx.setEntity("OutBandwidthUtilHdx");
//										chnameBand="����";
//										}
									utilhdx.setSubentity(sIndex);
									utilhdx.setRestype("dynamic");
									utilhdx.setUnit(unit1[j]+"/s");	
									utilhdx.setChname(sIndex+"�˿�"+"����");
									long currentOctets=Long.parseLong(sValue)/scale1[j];
									long lastOctets=0;	
									long l=0;											
											
									//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
									//if(longinterval<2*interval){
										String lastvalue="";
										
										if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
										//SysLogger.info(desc1[j]+":"+sIndex+"===="+lastvalue);
										//ȡ���ϴλ�õ�Octets
										if(lastvalue!=null && !lastvalue.equals(""))lastOctets=Long.parseLong(lastvalue);
									//}   
						
									if(longinterval!=0){
										long octetsBetween = 0;  
										if(currentOctets<lastOctets){
											currentOctets=currentOctets+4294967296L/scale1[j];
										} 
										//������-ǰ����	
										octetsBetween=currentOctets-lastOctets;
										//SysLogger.info(sIndex+"===currentOctets:"+currentOctets+"===lastOctets:"+lastOctets+"===octetsBetween:"+octetsBetween);
										//����˿�����
										l=octetsBetween/longinterval;
										//ͳ���ܳ����ֽ�������,���ü��㣨�����롢�ۺϣ�����������
//										if(j==0 && lastOctets!=0)allInOctetsSpeed=allInOctetsSpeed+l;
//										if(j==5 && lastOctets!=0)allOutOctetsSpeed=allOutOctetsSpeed+l;
//										//SysLogger.info("allInOctetsSpeed:"+allInOctetsSpeed+"===allOutOctetsSpeed:"+allOutOctetsSpeed);
//										if(lastOctets!=0)allOctetsSpeed=allOctetsSpeed+l;
										//yangjun
										if(j==0)allInOctetsSpeed=allInOctetsSpeed+l;
//										if(j==5)allOutOctetsSpeed=allOutOctetsSpeed+l;
										allOctetsSpeed=allOctetsSpeed+l;
										
									}
									DecimalFormat df=new DecimalFormat("#.##");//yangjun 
									utilhdx.setThevalue(df.format(l*8));	
									//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
									if (cal != null)
									utilhdxVector.addElement(utilhdx);												
									
									utilhdxperc=new UtilHdxPerc();
									utilhdxperc.setIpaddress(host.getIpAddress());
									utilhdxperc.setCollecttime(date);
									utilhdxperc.setCategory("Interface");
									if(j==0)utilhdxperc.setEntity("InBandwidthUtilHdxPerc");
//									if(j==5)utilhdxperc.setEntity("OutBandwidthUtilHdxPerc");
									utilhdxperc.setSubentity(sIndex);
									utilhdxperc.setRestype("dynamic");
									utilhdxperc.setUnit("%");	
									utilhdxperc.setChname(sIndex+"�˿�"+chnameBand+"����������");												
									double speed=0.0;
									if (hashSpeed.get(sIndex) != null){
									speed = Double.parseDouble(hashSpeed.get(sIndex).toString());
									}else{
										speed = Double.parseDouble("0.0");
									}
									double d=0.0;
									if(speed>0){
										//���������ʣ����١�8*100/ifspeed%
										d=Arith.div(l*800*1024,speed);
										//d=l*800/speed;
									}
									utilhdxperc.setThevalue(Double.toString(d));
									//SysLogger.info(utilhdxperc.getIpaddress()+"   "+utilhdxperc.getEntity()+"   :"+utilhdxperc.getThevalue());
									if (cal != null)
									utilhdxpercVector.addElement(utilhdxperc);	
									
												
												
								} //end j=0 j=5
								//SysLogger.info(host.getIpAddress()+"==="+desc1[j]+":"+sIndex+"===="+sValue);
								octetsHash.put(desc1[j]+":"+sIndex,interfacedata.getThevalue());
							} //valueArray1[i][j]==null
						} //end for j
						packhash=ShareData.getPacksdata(host.getIpAddress()+":"+sIndex);
						discardshash=ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex);
						errorshash=ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex);									
						//���㴫������ݰ�
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllInCastPkts");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(allinpacks+"");
						interfacedata.setChname("��������ݰ�");
						
//						/******************/
//						Hashtable hasht = new Hashtable();									
//						hasht.put("AllInCastPkts"+":"+sIndex,allinpacks+"");																		


//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllOutCastPkts");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(alloutpacks+"");
//						interfacedata.setChname("���������ݰ�");
//						/******************/									
//						hasht.put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
						
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllInDiscards");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(indiscards+"");
						interfacedata.setChname("����ܶ�����");
						
//						Hashtable tempDiscards = new Hashtable();
//						tempDiscards.put("AllInDiscards"+":"+sIndex,indiscards+"");

//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllOutDiscards");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(outdiscards+"");
//						interfacedata.setChname("�����ܶ�����");
//						tempDiscards.put("AllOutDiscards"+":"+sIndex,outdiscards+"");
						
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllInErrors");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(inerrors+"");
						interfacedata.setChname("��ڴ������");
						
//						Hashtable errHash = new Hashtable();
//						errHash.put("AllInErrors"+":"+sIndex,inerrors+"");
						//interfaceVector.addElement(interfacedata);

//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllOutErrors");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(outerrors+"");
//						interfacedata.setChname("���ڴ������");
//						errHash.put("AllOutErrors"+":"+sIndex,outerrors+"");
//						//interfaceVector.addElement(interfacedata);

						String lastvalue="";
						long lastpacks=0;
						//��ڴ������ݰ�
						if (packhash != null){
							if(packhash.get("AllInCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllInCastPkts"+":"+sIndex).toString();
						}
						
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){										
							lastpacks=Long.parseLong(lastvalue);									
						}
									
						Packs packs = new Packs();
						packs.setIpaddress(host.getIpAddress());
						packs.setCollecttime(date);
						packs.setCategory("Interface");
						packs.setEntity("InCastPkts");
						packs.setSubentity(sIndex);
						packs.setRestype("dynamic");
						packs.setUnit("��");
						packs.setChname("������ݰ�");
						if (lastpacks > 0){
							packs.setThevalue(allinpacks-lastpacks+"");
						}else{
							packs.setThevalue("0");
						}									
						packsVector.add(packs);
						
						//��ڶ�����
						lastvalue="";
						long lastdiscards=0;
						if (discardshash != null){
							if(discardshash.get("AllInDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllInDiscards"+":"+sIndex).toString();
						}
						
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){
							lastdiscards=Long.parseLong(lastvalue);									
						}
									
						DiscardsPerc discardsPerc = new DiscardsPerc();
						discardsPerc.setIpaddress(host.getIpAddress());
						discardsPerc.setCollecttime(date);
						discardsPerc.setCategory("Interface");
						discardsPerc.setEntity("InDiscardsPerc");
						discardsPerc.setSubentity(sIndex);
						discardsPerc.setRestype("dynamic");
						discardsPerc.setUnit("%");
						discardsPerc.setChname("��ڶ�����");
						double indiscardserc=0.0;
						if (allinpacks ==0){
							indiscardserc=0;
						}else{
							if (allinpacks-lastpacks > 0){
								indiscardserc = (indiscards-lastdiscards)/(allinpacks-lastpacks);
							}else{
								indiscardserc = 0;
							}
							
						}									
						discardsPerc.setThevalue(Double.toString(indiscardserc));
						discardspercVector.add(discardsPerc);
									
						//��ڴ�����
						lastvalue="";
						long lasterrors=0;
						if (errorshash != null){
							if(errorshash.get("AllInErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllInErrors"+":"+sIndex).toString();
						}									
						//ȡ���ϴλ�õ�error
						if(lastvalue!=null && !lastvalue.equals("")){
							lasterrors=Long.parseLong(lastvalue);									
						}																		
						ErrorsPerc errorsPerc = new  ErrorsPerc();
						errorsPerc.setIpaddress(host.getIpAddress());
						errorsPerc.setCollecttime(date);
						errorsPerc.setCategory("Interface");
						errorsPerc.setEntity("InErrorsPerc");
						errorsPerc.setSubentity(sIndex);
						errorsPerc.setRestype("dynamic");
						errorsPerc.setUnit("%");
						errorsPerc.setChname("��ڴ�����");
						double inerrorsperc=0.0;
						if (allinpacks==0){
							inerrorsperc=0;
						}else{
							if (allinpacks-lastpacks > 0){
								inerrorsperc=(inerrors-lasterrors)/(allinpacks-lastpacks);
							}else{
								inerrorsperc=0;
							}
							
						}									
						errorsPerc.setThevalue(Double.toString(inerrorsperc));
						errorspercVector.add(errorsPerc);
									
						lastvalue="";
						lastpacks=0;
						lastdiscards=0;
						lasterrors=0;
//						//���ڴ������ݰ�
//						if (packhash != null){
//							if(packhash.get("AllOutCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllOutCastPkts"+":"+sIndex).toString();
//						}
//						
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lastpacks=Long.parseLong(lastvalue);																		
//						}
//						packs = new Packs();
//						packs.setIpaddress(host.getIpAddress());
//						packs.setCollecttime(date);
//						packs.setCategory("Interface");
//						packs.setEntity("OutCastPkts");
//						packs.setSubentity(sIndex);
//						packs.setRestype("dynamic");
//						packs.setUnit("");
//						packs.setChname("�������ݰ�");
//						if (lastpacks>0){
//							packs.setThevalue(alloutpacks-lastpacks+"");
//						}else{
//							packs.setThevalue("0");
//						}
//						
//						packsVector.add(packs);
									
									
//						//���㶪���ʺʹ�����
//						if (discardshash != null){
//							if(discardshash.get("AllOutDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllOutDiscards"+":"+sIndex).toString();
//						}									
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lastdiscards=Long.parseLong(lastvalue);									
//						}																		
//						discardsPerc = new DiscardsPerc();
//						discardsPerc.setIpaddress(host.getIpAddress());
//						discardsPerc.setCollecttime(date);
//						discardsPerc.setCategory("Interface");
//						discardsPerc.setEntity("OutDiscardsPerc");
//						discardsPerc.setSubentity(sIndex);
//						discardsPerc.setRestype("dynamic");
//						discardsPerc.setUnit("%");
//						discardsPerc.setChname("���ڶ�����");
//						double outdiscardserc=0.0;
//						if (alloutpacks==0){
//							outdiscardserc = 0;
//						}else{
//							if (alloutpacks-lastpacks>0){
//								outdiscardserc = (outdiscards-lastdiscards)/(alloutpacks-lastpacks);
//							}else{
//								outdiscardserc = 0;
//							}
//							
//						}
//									
//						discardsPerc.setThevalue(Double.toString(outdiscardserc));
//						discardspercVector.add(discardsPerc);
						
//						lastvalue="";
//						if (errorshash != null){
//							if(errorshash.get("AllOutErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllOutErrors"+":"+sIndex).toString();
//						}									
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lasterrors=Long.parseLong(lastvalue);									
//						}																											
//						errorsPerc = new  ErrorsPerc();
//						errorsPerc.setIpaddress(host.getIpAddress());
//						errorsPerc.setCollecttime(date);
//						errorsPerc.setCategory("Interface");
//						errorsPerc.setEntity("OutErrorsPerc");
//						errorsPerc.setSubentity(sIndex);
//						errorsPerc.setRestype("dynamic");
//						errorsPerc.setUnit("%");
//						errorsPerc.setChname("���ڴ�����");
//						double outerrorsperc=0.0;
//						if (alloutpacks>0){
//							if ((alloutpacks-lastpacks)>0){
//								outerrorsperc=(outerrors-lasterrors)/(alloutpacks-lastpacks);
//							}else{
//								outerrorsperc=0;
//							}
//							
//						}									
//						errorsPerc.setThevalue(Double.toString(outerrorsperc));
//						errorspercVector.add(errorsPerc);									
									
						/* ��ӵ��ڴ���*/
						if(ShareData.getPacksdata(host.getIpAddress()+":"+sIndex)!=null){
							ShareData.getPacksdata(host.getIpAddress()+":"+sIndex).put("AllInCastPkts"+":"+sIndex,allinpacks+"");
						}else{
							Hashtable hasht = new Hashtable();									
							hasht.put("AllInCastPkts"+":"+sIndex,allinpacks+"");
							ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
						}					
						
						if(ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex)!=null){
							ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex).put("AllInDiscards"+":"+sIndex,indiscards+"");
						}else{
							Hashtable tempDiscards = new Hashtable();
							tempDiscards.put("AllInDiscards"+":"+sIndex,indiscards+"");
							ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
						}
						
						if(ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex)!=null){
							ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex).put("AllInErrors"+":"+sIndex,inerrors+"");
						}else{
							Hashtable errHash = new Hashtable();
							errHash.put("AllInErrors"+":"+sIndex,inerrors+"");
							ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
						}
						
						//ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
						//ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
						//ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
									
						/*
						//��ǰ�˿��ۺ϶�����
						AllDiscardsPerc alldiscardsperc = new AllDiscardsPerc();
						alldiscardsperc.setIpaddress(nethost);
						alldiscardsperc.setCollecttime(date);
						alldiscardsperc.setCategory("Interface");
						alldiscardsperc.setEntity("AllDiscardsPerc");
						alldiscardsperc.setSubentity(sIndex);
						alldiscardsperc.setRestype("dynamic");
						alldiscardsperc.setUnit("%");
						alldiscardsperc.setChname("�ۺ϶�����");
						double alldiscards_perc=0.0;
						if ((allinpacks+alloutpacks)>0)
							alldiscards_perc=(indiscards+outdiscards)/(allinpacks+alloutpacks);
						alldiscardsperc.setThevalue(Double.toString(alldiscards_perc));
						alldiscardspercVector.add(alldiscardsperc);									
						*/
						/*
						//��ǰ�˿��ۺϴ�����
						AllErrorsPerc allerrorsperc = new AllErrorsPerc();
						allerrorsperc.setIpaddress(nethost);
						allerrorsperc.setCollecttime(date);
						allerrorsperc.setCategory("Interface");
						allerrorsperc.setEntity("AllErrorsPerc");
						allerrorsperc.setSubentity(sIndex);
						allerrorsperc.setRestype("dynamic");
						allerrorsperc.setUnit("%");
						allerrorsperc.setChname("�ۺϴ�����");
						double allerrors_perc=0.0;
						if ((allinpacks+alloutpacks)>0)
							allerrors_perc=(inerrors+outerrors)/(allinpacks+alloutpacks);
						allerrorsperc.setThevalue(Double.toString(allerrors_perc));
						allerrorspercVector.add(allerrorsperc);
						*/
									
					} //end for contains
							
				}
				}
				
				if(valueArray3!= null){
					for(int i=0;i<valueArray3.length;i++){
						alloutpacks=0;
						outupacks=0;//���ڵ���
						outnupacks=0;//�ǵ���
						outdiscards=0;
						outerrors=0;																		

						String sIndex = (String)tempHash.get(i);				
						if (tempV.contains(sIndex)){
											
							for(int j=0;j<5;j++){																														
								if(valueArray3[i][j]!=null){
									String sValue=valueArray3[i][j];
									//if (j==6)continue;
									interfacedata=new Interfacecollectdata();
									if(scale1[5+j]==0){
										interfacedata.setThevalue(sValue);
									}
									else{
										interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale1[5+j]));
									}	
									if (j==1 || j==2){
										//���ڵ��������ݰ�,���ڷǵ��������ݰ�
										if (sValue != null){
											alloutpacks=alloutpacks+Long.parseLong(sValue);
											
											Calendar cal=(Calendar)hash.get("collecttime");
											long timeInMillis=0;
											if(cal!=null)timeInMillis=cal.getTimeInMillis();
											long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
											
											outpacks=new OutPkts();
											outpacks.setIpaddress(host.getIpAddress());
											outpacks.setCollecttime(date);
											outpacks.setCategory("Interface");
											String chnameBand="";
											
											if(j==1){
												outpacks.setEntity("ifOutMulticastPkts");
												chnameBand="�ಥ";
											}
											if(j==2){
												outpacks.setEntity("ifOutBroadcastPkts");
												chnameBand="�㲥";
											}
											outpacks.setSubentity(sIndex);
											outpacks.setRestype("dynamic");
											outpacks.setUnit("");	
											outpacks.setChname(chnameBand);
											long currentPacks=Long.parseLong(sValue);												
											long lastPacks=0;	
											long l=0;											
													
											//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
											if(longinterval<2*interval){
												String lastvalue="";
												
												if(hash.get(desc1[5+j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[5+j]+":"+sIndex).toString();
												//ȡ���ϴλ�õ�Octets
												if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
											}
								
											if(longinterval!=0){
												if(currentPacks<lastPacks){
													currentPacks=currentPacks+4294967296L;
												}
												//������-ǰ����	
												long octetsBetween=currentPacks-lastPacks;
												l=octetsBetween;
												if(lastPacks == 0)l=0;
											}
											outpacks.setThevalue(Long.toString(l));	
											//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+chnameBand+" "+Long.toString(l));
											if (cal != null)
												outpacksVector.addElement(outpacks);
											
										}
										//continue;
									}
									if (j==3){
										//��ڶ��������ݰ�
										if (sValue != null) outdiscards=Long.parseLong(sValue);
										continue;
									}
									if (j==4){
										//��ڴ�������ݰ�
										if (sValue != null) outerrors=Long.parseLong(sValue);
										continue;
									}		
									Calendar cal=(Calendar)hash.get("collecttime");
									long timeInMillis=0;
									if(cal!=null)timeInMillis=cal.getTimeInMillis();
									long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;											

									//����ÿ���˿����ټ�������
									if(j==0){
										utilhdx=new UtilHdx();
										utilhdx.setIpaddress(host.getIpAddress());
										utilhdx.setCollecttime(date);
										utilhdx.setCategory("Interface");
										String chnameBand="";
										if(j==0){
											utilhdx.setEntity("OutBandwidthUtilHdx");
											chnameBand="����";
											}
										utilhdx.setSubentity(sIndex);
										utilhdx.setRestype("dynamic");
										utilhdx.setUnit(unit1[5+j]+"/s");	
										utilhdx.setChname(sIndex+"�˿�"+"����");
										long currentOctets=Long.parseLong(sValue)/scale1[5+j];
										long lastOctets=0;	
										long l=0;											
												
										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										//if(longinterval<2*interval){
											String lastvalue="";
											
											if(hash.get(desc1[5+j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[5+j]+":"+sIndex).toString();
											//SysLogger.info(desc1[j]+":"+sIndex+"===="+lastvalue);
											//ȡ���ϴλ�õ�Octets
											if(lastvalue!=null && !lastvalue.equals(""))lastOctets=Long.parseLong(lastvalue);
										//}   
							
										if(longinterval!=0){
											long octetsBetween = 0;  
											if(currentOctets<lastOctets){
												currentOctets=currentOctets+4294967296L/scale1[5+j];
											} 
											//������-ǰ����	
											octetsBetween=currentOctets-lastOctets;
											//SysLogger.info(sIndex+"===currentOctets:"+currentOctets+"===lastOctets:"+lastOctets+"===octetsBetween:"+octetsBetween);
											//����˿�����
											l=octetsBetween/longinterval;
											//ͳ���ܳ����ֽ�������,���ü��㣨�����롢�ۺϣ�����������
//											if(j==0 && lastOctets!=0)allInOctetsSpeed=allInOctetsSpeed+l;
//											if(j==5 && lastOctets!=0)allOutOctetsSpeed=allOutOctetsSpeed+l;
//											//SysLogger.info("allInOctetsSpeed:"+allInOctetsSpeed+"===allOutOctetsSpeed:"+allOutOctetsSpeed);
//											if(lastOctets!=0)allOctetsSpeed=allOctetsSpeed+l;
											//yangjun
//											if(j==0)allInOctetsSpeed=allInOctetsSpeed+l;
											if(j==0)allOutOctetsSpeed=allOutOctetsSpeed+l;
											allOctetsSpeed=allOctetsSpeed+l;
											
										}
										DecimalFormat df=new DecimalFormat("#.##");//yangjun 
										utilhdx.setThevalue(df.format(l*8));	
										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
										if (cal != null)
										utilhdxVector.addElement(utilhdx);												
										
										utilhdxperc=new UtilHdxPerc();
										utilhdxperc.setIpaddress(host.getIpAddress());
										utilhdxperc.setCollecttime(date);
										utilhdxperc.setCategory("Interface");
										//if(j==0)utilhdxperc.setEntity("InBandwidthUtilHdxPerc");
										if(j==0)utilhdxperc.setEntity("OutBandwidthUtilHdxPerc");
										utilhdxperc.setSubentity(sIndex);
										utilhdxperc.setRestype("dynamic");
										utilhdxperc.setUnit("%");	
										utilhdxperc.setChname(sIndex+"�˿�"+chnameBand+"����������");												
										double speed=0.0;
										if (hashSpeed.get(sIndex) != null){
										speed = Double.parseDouble(hashSpeed.get(sIndex).toString());
										}else{
											speed = Double.parseDouble("0.0");
										}
										double d=0.0;
										if(speed>0){
											//���������ʣ����١�8*100/ifspeed%
//											SysLogger.info("########################");
//											SysLogger.info("##### "+host.getIpAddress()+" l:"+df.format(l*800)+" speed:"+df.format(speed)+"############");
//											SysLogger.info("########################");
											d=Arith.div(l*800*1024,speed);
											//d=l*800/speed;
										}
										utilhdxperc.setThevalue(Double.toString(d));
										//SysLogger.info(utilhdxperc.getIpaddress()+"   "+utilhdxperc.getEntity()+"   :"+utilhdxperc.getThevalue());
										if (cal != null)
										utilhdxpercVector.addElement(utilhdxperc);	
										
													
													
									} //end j=0 j=5
									//SysLogger.info(host.getIpAddress()+"==="+desc1[j]+":"+sIndex+"===="+sValue);
									octetsHash.put(desc1[5+j]+":"+sIndex,interfacedata.getThevalue());
								} //valueArray1[i][j]==null
							} //end for j
							packhash=ShareData.getPacksdata(host.getIpAddress()+":"+sIndex);
							discardshash=ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex);
							errorshash=ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex);									
							//���㴫������ݰ�
//							interfacedata=new Interfacecollectdata();
//							interfacedata.setIpaddress(host.getIpAddress());
//							interfacedata.setCollecttime(date);
//							interfacedata.setCategory("Interface");
//							interfacedata.setEntity("AllInCastPkts");
//							interfacedata.setSubentity(sIndex);									
//							interfacedata.setRestype("static");
//							interfacedata.setUnit("��");
//							interfacedata.setThevalue(allinpacks+"");
//							interfacedata.setChname("��������ݰ�");
							/******************/
							//Hashtable hasht = new Hashtable();									
//							hasht.put("AllInCastPkts"+":"+sIndex,allinpacks+"");																		

							//ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutCastPkts");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(alloutpacks+"");
							interfacedata.setChname("���������ݰ�");
							/******************/
//							if(ShareData.getPacksdata(host.getIpAddress()+":"+sIndex) != null){
//								ShareData.getPacksdata(host.getIpAddress()+":"+sIndex).put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
//							}
							//hasht.put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
							
//							interfacedata=new Interfacecollectdata();
//							interfacedata.setIpaddress(host.getIpAddress());
//							interfacedata.setCollecttime(date);
//							interfacedata.setCategory("Interface");
//							interfacedata.setEntity("AllInDiscards");
//							interfacedata.setSubentity(sIndex);									
//							interfacedata.setRestype("static");
//							interfacedata.setUnit("��");
//							interfacedata.setThevalue(indiscards+"");
//							interfacedata.setChname("����ܶ�����");
							//Hashtable tempDiscards = new Hashtable();
							//tempDiscards.put("AllInDiscards"+":"+sIndex,indiscards+"");

							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutDiscards");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(outdiscards+"");
							interfacedata.setChname("�����ܶ�����");
							
							//ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
//							if(ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex) != null){
//								ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex).put("AllOutDiscards"+":"+sIndex,outdiscards+"");
//							}
							
							//tempDiscards.put("AllOutDiscards"+":"+sIndex,outdiscards+"");
							
//							interfacedata=new Interfacecollectdata();
//							interfacedata.setIpaddress(host.getIpAddress());
//							interfacedata.setCollecttime(date);
//							interfacedata.setCategory("Interface");
//							interfacedata.setEntity("AllInErrors");
//							interfacedata.setSubentity(sIndex);									
//							interfacedata.setRestype("static");
//							interfacedata.setUnit("��");
//							interfacedata.setThevalue(inerrors+"");
//							interfacedata.setChname("��ڴ������");
							//Hashtable errHash = new Hashtable();
							//errHash.put("AllInErrors"+":"+sIndex,inerrors+"");
							//interfaceVector.addElement(interfacedata);

							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(date);
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutErrors");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(outerrors+"");
							interfacedata.setChname("���ڴ������");
							
							//ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
//							if(ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex)!= null){
//								ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex).put("AllOutErrors"+":"+sIndex,outerrors+"");
//							}
							
							//errHash.put("AllOutErrors"+":"+sIndex,outerrors+"");
							//interfaceVector.addElement(interfacedata);

							String lastvalue="";
							long lastpacks=0;
//							//��ڴ������ݰ�
//							if (packhash != null){
//								if(packhash.get("AllInCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllInCastPkts"+":"+sIndex).toString();
//							}
							
//							//ȡ���ϴλ�õ�packs
//							if(lastvalue!=null && !lastvalue.equals("")){										
//								lastpacks=Long.parseLong(lastvalue);									
//							}
										
							Packs packs = new Packs();
//							packs.setIpaddress(host.getIpAddress());
//							packs.setCollecttime(date);
//							packs.setCategory("Interface");
//							packs.setEntity("InCastPkts");
//							packs.setSubentity(sIndex);
//							packs.setRestype("dynamic");
//							packs.setUnit("��");
//							packs.setChname("������ݰ�");
//							if (lastpacks > 0){
//								packs.setThevalue(allinpacks-lastpacks+"");
//							}else{
//								packs.setThevalue("0");
//							}									
//							packsVector.add(packs);
							
							//��ڶ�����
							lastvalue="";
							long lastdiscards=0;
//							if (discardshash != null){
//								if(discardshash.get("AllInDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllInDiscards"+":"+sIndex).toString();
//							}
							
//							//ȡ���ϴλ�õ�packs
//							if(lastvalue!=null && !lastvalue.equals("")){
//								lastdiscards=Long.parseLong(lastvalue);									
//							}
										
							DiscardsPerc discardsPerc = new DiscardsPerc();
//							discardsPerc.setIpaddress(host.getIpAddress());
//							discardsPerc.setCollecttime(date);
//							discardsPerc.setCategory("Interface");
//							discardsPerc.setEntity("InDiscardsPerc");
//							discardsPerc.setSubentity(sIndex);
//							discardsPerc.setRestype("dynamic");
//							discardsPerc.setUnit("%");
//							discardsPerc.setChname("��ڶ�����");
//							double indiscardserc=0.0;
//							if (allinpacks ==0){
//								indiscardserc=0;
//							}else{
//								if (allinpacks-lastpacks > 0){
//									indiscardserc = (indiscards-lastdiscards)/(allinpacks-lastpacks);
//								}else{
//									indiscardserc = 0;
//								}
//								
//							}									
//							discardsPerc.setThevalue(Double.toString(indiscardserc));
//							discardspercVector.add(discardsPerc);
										
							//��ڴ�����
							lastvalue="";
							long lasterrors=0;
//							if (errorshash != null){
//								if(errorshash.get("AllInErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllInErrors"+":"+sIndex).toString();
//							}									
//							//ȡ���ϴλ�õ�error
//							if(lastvalue!=null && !lastvalue.equals("")){
//								lasterrors=Long.parseLong(lastvalue);									
//							}																		
							ErrorsPerc errorsPerc = new  ErrorsPerc();
//							errorsPerc.setIpaddress(host.getIpAddress());
//							errorsPerc.setCollecttime(date);
//							errorsPerc.setCategory("Interface");
//							errorsPerc.setEntity("InErrorsPerc");
//							errorsPerc.setSubentity(sIndex);
//							errorsPerc.setRestype("dynamic");
//							errorsPerc.setUnit("%");
//							errorsPerc.setChname("��ڴ�����");
//							double inerrorsperc=0.0;
//							if (allinpacks==0){
//								inerrorsperc=0;
//							}else{
//								if (allinpacks-lastpacks > 0){
//									inerrorsperc=(inerrors-lasterrors)/(allinpacks-lastpacks);
//								}else{
//									inerrorsperc=0;
//								}
//								
//							}									
//							errorsPerc.setThevalue(Double.toString(inerrorsperc));
//							errorspercVector.add(errorsPerc);
										
							lastvalue="";
							lastpacks=0;
							lastdiscards=0;
							lasterrors=0;
							//���ڴ������ݰ�
							if (packhash != null){
								if(packhash.get("AllOutCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllOutCastPkts"+":"+sIndex).toString();
							}
							
							//ȡ���ϴλ�õ�packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lastpacks=Long.parseLong(lastvalue);																		
							}
							packs = new Packs();
							packs.setIpaddress(host.getIpAddress());
							packs.setCollecttime(date);
							packs.setCategory("Interface");
							packs.setEntity("OutCastPkts");
							packs.setSubentity(sIndex);
							packs.setRestype("dynamic");
							packs.setUnit("");
							packs.setChname("�������ݰ�");
							if (lastpacks>0){
								packs.setThevalue(alloutpacks-lastpacks+"");
							}else{
								packs.setThevalue("0");
							}
							
							packsVector.add(packs);
										
										
							//���㶪���ʺʹ�����
							if (discardshash != null){
								if(discardshash.get("AllOutDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllOutDiscards"+":"+sIndex).toString();
							}									
							//ȡ���ϴλ�õ�packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lastdiscards=Long.parseLong(lastvalue);									
							}																		
							discardsPerc = new DiscardsPerc();
							discardsPerc.setIpaddress(host.getIpAddress());
							discardsPerc.setCollecttime(date);
							discardsPerc.setCategory("Interface");
							discardsPerc.setEntity("OutDiscardsPerc");
							discardsPerc.setSubentity(sIndex);
							discardsPerc.setRestype("dynamic");
							discardsPerc.setUnit("%");
							discardsPerc.setChname("���ڶ�����");
							double outdiscardserc=0.0;
							if (alloutpacks==0){
								outdiscardserc = 0;
							}else{
								if (alloutpacks-lastpacks>0){
									outdiscardserc = (outdiscards-lastdiscards)/(alloutpacks-lastpacks);
								}else{
									outdiscardserc = 0;
								}
								
							}
										
							discardsPerc.setThevalue(Double.toString(outdiscardserc));
							discardspercVector.add(discardsPerc);
							
							lastvalue="";
							if (errorshash != null){
								if(errorshash.get("AllOutErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllOutErrors"+":"+sIndex).toString();
							}									
							//ȡ���ϴλ�õ�packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lasterrors=Long.parseLong(lastvalue);									
							}																											
							errorsPerc = new  ErrorsPerc();
							errorsPerc.setIpaddress(host.getIpAddress());
							errorsPerc.setCollecttime(date);
							errorsPerc.setCategory("Interface");
							errorsPerc.setEntity("OutErrorsPerc");
							errorsPerc.setSubentity(sIndex);
							errorsPerc.setRestype("dynamic");
							errorsPerc.setUnit("%");
							errorsPerc.setChname("���ڴ�����");
							double outerrorsperc=0.0;
							if (alloutpacks>0){
								if ((alloutpacks-lastpacks)>0){
									outerrorsperc=(outerrors-lasterrors)/(alloutpacks-lastpacks);
								}else{
									outerrorsperc=0;
								}
								
							}									
							errorsPerc.setThevalue(Double.toString(outerrorsperc));
							errorspercVector.add(errorsPerc);	
							
							/* ��ӵ��ڴ���*/
							if(ShareData.getPacksdata(host.getIpAddress()+":"+sIndex)!=null){
								ShareData.getPacksdata(host.getIpAddress()+":"+sIndex).put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
							}else{
								Hashtable hasht = new Hashtable();									
								hasht.put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
								ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
							}					
							
							if(ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex)!=null){
								ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex).put("AllOutDiscards"+":"+sIndex,outdiscards+"");
							}else{
								Hashtable tempDiscards = new Hashtable();
								tempDiscards.put("AllOutDiscards"+":"+sIndex,outdiscards+"");
								ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
							}
							
							if(ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex)!=null){
								ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex).put("AllOutErrors"+":"+sIndex,outerrors+"");
							}else{
								Hashtable errHash = new Hashtable();
								errHash.put("AllOutErrors"+":"+sIndex,outerrors+"");
								ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
							}
										
							/* ��ӵ��ڴ���*/
							//ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
							//ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
							//ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
										
							/*
							//��ǰ�˿��ۺ϶�����
							AllDiscardsPerc alldiscardsperc = new AllDiscardsPerc();
							alldiscardsperc.setIpaddress(nethost);
							alldiscardsperc.setCollecttime(date);
							alldiscardsperc.setCategory("Interface");
							alldiscardsperc.setEntity("AllDiscardsPerc");
							alldiscardsperc.setSubentity(sIndex);
							alldiscardsperc.setRestype("dynamic");
							alldiscardsperc.setUnit("%");
							alldiscardsperc.setChname("�ۺ϶�����");
							double alldiscards_perc=0.0;
							if ((allinpacks+alloutpacks)>0)
								alldiscards_perc=(indiscards+outdiscards)/(allinpacks+alloutpacks);
							alldiscardsperc.setThevalue(Double.toString(alldiscards_perc));
							alldiscardspercVector.add(alldiscardsperc);									
							*/
							/*
							//��ǰ�˿��ۺϴ�����
							AllErrorsPerc allerrorsperc = new AllErrorsPerc();
							allerrorsperc.setIpaddress(nethost);
							allerrorsperc.setCollecttime(date);
							allerrorsperc.setCategory("Interface");
							allerrorsperc.setEntity("AllErrorsPerc");
							allerrorsperc.setSubentity(sIndex);
							allerrorsperc.setRestype("dynamic");
							allerrorsperc.setUnit("%");
							allerrorsperc.setChname("�ۺϴ�����");
							double allerrors_perc=0.0;
							if ((allinpacks+alloutpacks)>0)
								allerrors_perc=(inerrors+outerrors)/(allinpacks+alloutpacks);
							allerrorsperc.setThevalue(Double.toString(allerrors_perc));
							allerrorspercVector.add(allerrorsperc);
							*/
										
						} //end for contains
								
					}
					}
				
				AllUtilHdx allInutilhdx = new AllUtilHdx();
				allInutilhdx = new AllUtilHdx();
				allInutilhdx.setIpaddress(host.getIpAddress());
				allInutilhdx.setCollecttime(date);
				allInutilhdx.setCategory("Interface");
				allInutilhdx.setEntity("AllInBandwidthUtilHdx");
				allInutilhdx.setSubentity("AllInBandwidthUtilHdx");
				allInutilhdx.setRestype("dynamic");
				allInutilhdx.setUnit(unit1[0]+"/��");	
				allInutilhdx.setChname("�������");
				allInutilhdx.setThevalue(Long.toString(allInOctetsSpeed*8));	
				allutilhdxVector.addElement(allInutilhdx);
				
				//System.out.println("===============AllInBandwidthUtilHdx===============");
				//System.out.println(allInutilhdx.getThevalue());
				
				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllInBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllInBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("��ڴ���������");
				double lInUsagePerc=0;
				if(allSpeed>0){
					lInUsagePerc=Arith.div(allInOctetsSpeed*800.0,allSpeed);
					//lInUsagePerc=allInOctetsSpeed*800.0/allSpeed;
					if(lInUsagePerc>95)lInUsagePerc=95;
				}
				allutilhdxperc.setThevalue(Double.toString(lInUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);	
				*/
				//out
				
				AllUtilHdx alloututilhdx = new AllUtilHdx();
				alloututilhdx = new AllUtilHdx();
				alloututilhdx.setIpaddress(host.getIpAddress());
				alloututilhdx.setCollecttime(date);
				alloututilhdx.setCategory("Interface");
				alloututilhdx.setEntity("AllOutBandwidthUtilHdx");
				alloututilhdx.setSubentity("AllOutBandwidthUtilHdx");
				alloututilhdx.setRestype("dynamic");
				alloututilhdx.setUnit(unit1[0]+"/��");
				alloututilhdx.setChname("��������");	
				alloututilhdx.setThevalue(Long.toString(allOutOctetsSpeed*8));	
				allutilhdxVector.addElement(alloututilhdx);
				//System.out.println("===============AllOutBandwidthUtilHdx===============");
				//System.out.println(alloututilhdx.getThevalue());
				
				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllOutBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllOutBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("���ڴ���������");
				double lOutUsagePerc=0;
				if(allSpeed>0){
					lOutUsagePerc=Arith.div(allOutOctetsSpeed*800.0,allSpeed);
					//lOutUsagePerc=allOutOctetsSpeed*800.0/allSpeed;
					if(lOutUsagePerc>96)lOutUsagePerc=96;
				}
				allutilhdxperc.setThevalue(Double.toString(lOutUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);
				*/	
				
				
				allutilhdx = new AllUtilHdx();
				allutilhdx.setIpaddress(host.getIpAddress());
				allutilhdx.setCollecttime(date);
				allutilhdx.setCategory("Interface");
				allutilhdx.setEntity("AllBandwidthUtilHdx");
				allutilhdx.setSubentity("AllBandwidthUtilHdx");
				allutilhdx.setRestype("dynamic");
				allutilhdx.setUnit(unit1[0]+"/��");	
				allutilhdx.setChname("�ۺ�����");
				allutilhdx.setThevalue(Long.toString(allOctetsSpeed*8));	
				allutilhdxVector.addElement(allutilhdx);
				//System.out.println("===============�ۺ�����===============");
				//System.out.println(allutilhdx.getThevalue());
				
				

				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("�ۺϴ���������");
				double lAllUsagePerc=0;
				if(allSpeed>0){
					lAllUsagePerc=allOctetsSpeed*800.0/allSpeed;
					if(lAllUsagePerc>196)lAllUsagePerc=196;
				 }
				allutilhdxperc.setThevalue(Double.toString(lAllUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);	
				*/
				String flag ="0";
				//hash=null;
				hashSpeed=null;
				octetsHash.put("collecttime",date);	
				if (hash != null){					
					flag = (String)hash.get("flag");
					if (flag == null){
						flag="0";
					}else{
						if (flag.equals("0")){
							flag = "1";
						}else{
							flag = "0";
						}
					}
				}				
				octetsHash.put("flag",flag);
				ShareData.setOctetsdata(host.getIpAddress(),octetsHash);				
			}catch(Exception e){e.printStackTrace();}
//			  -------------------------------------------------------------------------------------------interface end
			}catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}finally{
				System.gc();
			}
			
			//SysLogger.info("-----------------------------interface end -----");
			
//			/**
//		     * @author GZM
//		     */
//		    Hashtable vpnlist=new Hashtable();
//			
//			
//			try{
//				
//				Huaweitelnetconf hmo=(Huaweitelnetconf) Huawei3comtelnetUtil.telnetconf.get(host.getId());//�õ�telnet�����Ӷ���
//				if(null !=hmo)
//				{
//					SysLogger.info("=================��ʼtelnet�ɼ�"+hmo.getIpaddress()+"������Ϣ=======***************");
//					//System.out.println("=========="+hmo.getIpaddress());
//					Huawei3comvpn tvpn=new Huawei3comvpn();
//					tvpn.setDEFAULT_TELNET_PORT(hmo.getPort());//�˿�
//					tvpn.setSuuser(hmo.getSuuser());//su
//					tvpn.setSupassword(hmo.getSupassword());//su����
//					tvpn.setUser(hmo.getUser());//�û�
//					tvpn.setPassword(hmo.getPassword());//����
//					tvpn.setIp(hmo.getIpaddress());//ip��ַ
//					tvpn.setDEFAULT_PROMPT(hmo.getDefaultpromtp());//������Ƿ���
//					tvpn.setPort(hmo.getPort());
//					String result=tvpn.Getcommantvalue("disp cu");
//					vpnlist=Huawei3comtelnetUtil.Getvpnlist(result);
//				}
//				
//				
//			}catch(Exception e){
//				
//				e.printStackTrace();
//			}
		
//		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
//		if(ipAllData == null)ipAllData = new Hashtable();
//		if (interfaceVector != null && interfaceVector.size()>0)ipAllData.put("interface",interfaceVector);		
//		if (allutilhdxpercVector != null && allutilhdxpercVector.size()>0)ipAllData.put("allutilhdxperc",allutilhdxpercVector);
//		if (allutilhdxVector != null && allutilhdxVector.size()>0)ipAllData.put("allutilhdx",allutilhdxVector);
//		if (utilhdxpercVector != null && utilhdxpercVector.size()>0)ipAllData.put("utilhdxperc",utilhdxpercVector);
//		if (utilhdxVector != null && utilhdxVector.size()>0)ipAllData.put("utilhdx",utilhdxVector);		
//		if (discardspercVector != null && discardspercVector.size()>0)ipAllData.put("discardsperc",discardspercVector);
//		if (errorspercVector != null && errorspercVector.size()>0)ipAllData.put("errorsperc",errorspercVector);
//		if (allerrorspercVector != null && allerrorspercVector.size()>0)ipAllData.put("allerrorsperc",allerrorspercVector);
//		if (alldiscardspercVector != null && alldiscardspercVector.size()>0)ipAllData.put("alldiscardsperc",alldiscardspercVector);
//		if (packsVector != null && packsVector.size()>0)ipAllData.put("packs",packsVector);
//		if (inpacksVector != null && inpacksVector.size()>0)ipAllData.put("inpacks",inpacksVector);
//		if (outpacksVector != null && outpacksVector.size()>0)ipAllData.put("outpacks",outpacksVector);
//		/**
//		 * @author GZM
//		 */
//		if (vpnlist != null && vpnlist.size()>0)ipAllData.put("hh3comvpn",vpnlist);
//		ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	    
	    if (!(ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			Hashtable ipAllData = new Hashtable();
			if (ipAllData == null) ipAllData = new Hashtable();
			if (interfaceVector != null && interfaceVector.size() > 0) ipAllData.put("interface", interfaceVector);
			if (allutilhdxpercVector != null && allutilhdxpercVector.size() > 0) ipAllData.put("allutilhdxperc", allutilhdxpercVector);
			if (allutilhdxVector != null && allutilhdxVector.size() > 0) ipAllData.put("allutilhdx", allutilhdxVector);
			if (utilhdxpercVector != null && utilhdxpercVector.size() > 0) ipAllData.put("utilhdxperc", utilhdxpercVector);
			if (utilhdxVector != null && utilhdxVector.size() > 0) ipAllData.put("utilhdx", utilhdxVector);
			if (discardspercVector != null && discardspercVector.size() > 0) ipAllData.put("discardsperc", discardspercVector);
			if (errorspercVector != null && errorspercVector.size() > 0) ipAllData.put("errorsperc", errorspercVector);
			if (allerrorspercVector != null && allerrorspercVector.size() > 0) ipAllData.put("allerrorsperc", allerrorspercVector);
			if (alldiscardspercVector != null && alldiscardspercVector.size() > 0) ipAllData.put("alldiscardsperc", alldiscardspercVector);
			if (packsVector != null && packsVector.size() > 0) ipAllData.put("packs", packsVector);
			if (inpacksVector != null && inpacksVector.size() > 0) ipAllData.put("inpacks", inpacksVector);
			if (outpacksVector != null && outpacksVector.size() > 0) ipAllData.put("outpacks", inpacksVector);
			ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		} else {
			if (interfaceVector != null && interfaceVector.size() > 0) ((Hashtable) ShareData.getSharedata().get(host.getIpAddress())) .put("interface", interfaceVector);
			if (allutilhdxpercVector != null && allutilhdxpercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("allutilhdxperc",allutilhdxpercVector);
			if (allutilhdxVector != null && allutilhdxVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("allutilhdx",allutilhdxVector);
			if (utilhdxpercVector != null && utilhdxpercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("utilhdxperc",utilhdxpercVector);
			if (utilhdxVector != null && utilhdxVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("utilhdx",utilhdxVector);
			if (discardspercVector != null && discardspercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("discardsperc",discardspercVector);
			if (errorspercVector != null && errorspercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("errorsperc",errorspercVector);
			if (allerrorspercVector != null && allerrorspercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("allerrorsperc",allerrorspercVector);
			if (alldiscardspercVector != null && alldiscardspercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("alldiscardsperc",alldiscardspercVector);
			if (packsVector != null && packsVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("packs",packsVector);
			if (inpacksVector != null && inpacksVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("inpacks",inpacksVector);
			if (outpacksVector != null && outpacksVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("outpacks",outpacksVector);
		}
	    returnHash.put("interface",interfaceVector);		
	    returnHash.put("allutilhdxperc",allutilhdxpercVector);
	    returnHash.put("allutilhdx",allutilhdxVector);
	    returnHash.put("utilhdxperc",utilhdxpercVector);
	    returnHash.put("utilhdx",utilhdxVector);		
	    returnHash.put("discardsperc",discardspercVector);
	    returnHash.put("errorsperc",errorspercVector);
	    returnHash.put("allerrorsperc",allerrorspercVector);
	    returnHash.put("alldiscardsperc",alldiscardspercVector);
	    returnHash.put("packs",packsVector);
	    returnHash.put("inpacks",inpacksVector);
	    returnHash.put("outpacks",outpacksVector);
	    
	    System.out.println("#######alarmIndicatorsNode.getType().equals()##########"+alarmIndicatorsNode.getType()+"##############");
	    System.out.println("#######host.getId()##########"+host.getId()+"##############");
	    if (alarmIndicatorsNode.getType().equals("net")) {
	    	try{
	    		System.out.println("#######alarmIndicatorsNode.getType().equals()##########"+alarmIndicatorsNode.getType().equals("net")+"##############");
		    	CheckEventUtil checkutil = new CheckEventUtil();
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), alarmIndicatorsNode.getType(), alarmIndicatorsNode.getSubtype());
				System.out.println("#######list##########"+list.size()+"##############");
				List portconfiglist = new ArrayList();			

				Portconfig portconfig = null;
				
				Hashtable allportconfighash = ShareData.getPortConfigHash();
				if(allportconfighash != null && allportconfighash.size()>0){
					if(allportconfighash.containsKey(host.getIpAddress())){
						portconfiglist = (List)allportconfighash.get(host.getIpAddress());
					}
				}				
				Hashtable portconfigHash = new Hashtable();

				if(portconfiglist != null && portconfiglist.size()>0){
					for(int m=0;m<portconfiglist.size();m++){
						portconfig = (Portconfig)portconfiglist.get(m);
						portconfigHash.put(portconfig.getPortindex()+"", portconfig);
					}
				}
				portconfig = null;
				
				if(list == null){
					list = new ArrayList();
				}
				for(int i = 0 ; i < list.size(); i ++){
					AlarmIndicatorsNode alarmIndicatorsNode1 = (AlarmIndicatorsNode)list.get(i);
					if("0".equals(alarmIndicatorsNode1.getEnabled())){
						continue;
					}
				System.out.println("#######"+alarmIndicatorsNode1.getName()+"##########"+"utilhdx".equalsIgnoreCase(alarmIndicatorsNode1.getName())+"##############");	
				 if ("utilhdx".equalsIgnoreCase(alarmIndicatorsNode1.getName())){
						// cisco �˿ڵ� ��� �� ���� ����
						
						try {
							if(returnHash!=null){
								if(utilhdxVector != null && utilhdxVector.size() > 0){
									for(int j = 0 ; j < utilhdxVector.size(); j++){
										UtilHdx utilhdx = (UtilHdx)utilhdxVector.get(j);
										System.out.println("##########portconfigHash.containsKey(utilhdx.getSubentity())#######"+portconfigHash.containsKey(utilhdx.getSubentity())+"##############");
										if(portconfigHash.containsKey(utilhdx.getSubentity())){
											//�����ڶ˿�DOWN�澯����
											portconfig = (Portconfig)portconfigHash.get(utilhdx.getSubentity());
											System.out.println("#######portconfig##########"+portconfig+"##############");
											try {
												if (portconfig != null){
													alarmIndicatorsNode1.setEnabled(portconfig.getSms()+"");
													System.out.println(portconfig.getName()+"*****************************"+utilhdx.getThevalue());
													System.out.println(portconfig.getName()+"*****************************"+utilhdx.getThevalue());
													System.out.println(portconfig.getName()+"*****************************"+utilhdx.getThevalue());
													checkutil.checkEvent(host, alarmIndicatorsNode1, utilhdx.getThevalue(), portconfig.getName());
												}
											}catch(Exception e){
											}
										}
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							SysLogger.error("�����豸 " + alarmIndicatorsNode1.getName() + " �˿�������� �澯����" , e);
						}
					}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
    	}
	    
	    
	    outpacksVector=null;
	    inpacksVector=null;
	    packsVector=null;
	    alldiscardspercVector=null;
	    allerrorspercVector=null;
	    errorspercVector=null;
	    discardspercVector=null;
	    utilhdxVector=null;
	    utilhdxpercVector=null;
	    allutilhdxVector=null;
	    allutilhdxpercVector=null;
	    interfaceVector=null;
	    
	    NetinterfaceResultTosql tosql =new NetinterfaceResultTosql();
	    tosql.CreateResultTosql(returnHash, host.getIpAddress());
	    NetInterfaceDataTemptosql datatemp=new NetInterfaceDataTemptosql();
	    datatemp.CreateResultTosql(returnHash, host);
	    
	    
	    return returnHash;
	}
	
	public int getInterval(float d,String t){
		int interval=0;
		  if(t.equals("d"))
			 interval =(int) d*24*60*60; //����
		  else if(t.equals("h"))
			 interval =(int) d*60*60;    //Сʱ
		  else if(t.equals("m"))
			 interval = (int)d*60;       //����
		else if(t.equals("s"))
					 interval =(int) d;       //��
		return interval;
	}
	
	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,String sIndex,String bids){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	Hashtable sendeddata = ShareData.getSendeddata();
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	//SysLogger.info("�˿��¼�--------------------"+bids);
	 	try{
 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress+":"+sIndex)) {
 				//�����ڣ��������ţ�������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel(flag+"");
	 			smscontent.setObjid(objid);
	 			smscontent.setMessage(content);
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(subtype);
	 			smscontent.setSubentity(subentity);
	 			smscontent.setIp(ipaddress);
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);	
				
 			} else {
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				SmsDao smsDao = new SmsDao();
 				List list = new ArrayList();
 				String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
 				String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 				try {
 					list = smsDao.findByEvent(content,startTime,endTime);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					smsDao.close();
				}
				if(list!=null&&list.size()>0){//�����б����Ѿ����͵���Ķ���
					Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress+":"+sIndex);		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			Date ccc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(ccc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();	
		 			if(checkday == 1){
		 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
		 				if (subvalue/(1000*60*60*24)>=1){
			 				//����һ�죬���ٷ���Ϣ
				 			Smscontent smscontent = new Smscontent();
				 			String time = sdf.format(date.getTime());
				 			smscontent.setLevel(flag+"");
				 			smscontent.setObjid(objid);
				 			smscontent.setMessage(content);
				 			smscontent.setRecordtime(time);
				 			smscontent.setSubtype(subtype);
				 			smscontent.setSubentity(subentity);
				 			smscontent.setIp(ipaddress);//���Ͷ���
				 			SmscontentDao smsmanager=new SmscontentDao();
				 			smsmanager.sendURLSmscontent(smscontent);
							//�޸��Ѿ����͵Ķ��ż�¼	
							sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
				 		} else {
	                        //��ʼд�¼�
			 	            String sysLocation = "";
			 				createEvent("poll",sysLocation,bids,content,flag,subtype,subentity,ipaddress,objid);
				 		}
		 			}
				} else {
 					Smscontent smscontent = new Smscontent();
 		 			String time = sdf.format(date.getTime());
 		 			smscontent.setLevel(flag+"");
 		 			smscontent.setObjid(objid);
 		 			smscontent.setMessage(content);
 		 			smscontent.setRecordtime(time);
 		 			smscontent.setSubtype(subtype);
 		 			smscontent.setSubentity(subentity);
 		 			smscontent.setIp(ipaddress);
 		 			//���Ͷ���
 		 			SmscontentDao smsmanager=new SmscontentDao();
 		 			smsmanager.sendURLSmscontent(smscontent);	
 					sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
 				}
 				
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }
	
	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
		//�����¼�
		SysLogger.info("##############��ʼ�����¼�############");
		EventList eventlist = new EventList();
		eventlist.setEventtype(eventtype);
		eventlist.setEventlocation(eventlocation);
		eventlist.setContent(content);
		eventlist.setLevel1(level1);
		eventlist.setManagesign(0);
		eventlist.setBak("");
		eventlist.setRecordtime(Calendar.getInstance());
		eventlist.setReportman("ϵͳ��ѯ");
		//SysLogger.info("bid============="+bid);
		eventlist.setBusinessid(bid);
		eventlist.setNodeid(Integer.parseInt(objid));
		eventlist.setOid(0);
		eventlist.setSubtype(subtype);
		eventlist.setSubentity(subentity);
		EventListDao eventlistdao = new EventListDao();
		try{
			eventlistdao.save(eventlist);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			eventlistdao.close();
		}
	}
}






package com.afunms.polling.task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.common.util.Arith;
import com.afunms.common.util.DateUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpService;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.PortIPS;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.polling.task.TaskXml;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.InterfaceNode;
import com.gatherResulttosql.NetinterfaceResultTosql;

public class InterfaceTask extends MonitorTask {
	public static Hashtable Interface_IfType = null;
	static {
		Interface_IfType = new Hashtable();
		Interface_IfType.put("1", "other(1)");
		Interface_IfType.put("6", "ethernetCsmacd(6)");
		Interface_IfType.put("23", "ppp(23)");
		Interface_IfType.put("28", "slip(28)");
		Interface_IfType.put("33", "Console port");
		Interface_IfType.put("53", "propVirtual(53)");
		Interface_IfType.put("117", "gigabitEthernet(117)");

		Interface_IfType.put("131", "tunnel(131)");

		Interface_IfType.put("135", "others(135)");
		Interface_IfType.put("136", "others(136)");
		Interface_IfType.put("142", "others(142)");
		Interface_IfType.put("54", "others(54)");
		Interface_IfType.put("5", "others(5)");

	}
	
	protected static SnmpService snmp = new SnmpService();
	private static Hashtable ifEntity_ifStatus = null;
	static {
		ifEntity_ifStatus = new Hashtable();
		ifEntity_ifStatus.put("1", "up");
		ifEntity_ifStatus.put("2", "down");
		ifEntity_ifStatus.put("3", "testing");
		ifEntity_ifStatus.put("5", "unknow");
		ifEntity_ifStatus.put("7", "unknow");
	};

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private HostNode node = new HostNode();

	public HostNode getNode() {
		return node;
	}

	public void setNode(HostNode node) {
		this.node = node;
	}

	public InterfaceTask() {
	}

	public void collectData(HostNode node) {

	}

	public Hashtable collect_Data(HostNode host,Calendar date) {
		host.setSnmpversion(1);
		SysLogger.info("----------流量信息采集interface start -----"
				+ host.getIpAddress());
		Hashtable returnHash = new Hashtable();
		Vector interfaceVector = new Vector();
		Vector utilhdxVector = new Vector();
		Vector packsVector = new Vector();
		Vector inpacksVector = new Vector();
		Vector outpacksVector = new Vector();
		Vector discardspercVector = new Vector();
		Vector errorspercVector = new Vector();
		Vector utilhdxpercVector = new Vector();
		String utilfalg="";
		try {
			Interfacecollectdata interfacedata = null;
			UtilHdx utilhdx = new UtilHdx();
			PortIPS portIPS = new PortIPS();
			InPkts inpacks = new InPkts();
			OutPkts outpacks = new OutPkts();
			// start
			try {
				//System.out.println("ipaddress====="+host.getIpAddress());
				Hashtable hash = ShareData.getOctetsdata(host.getIpAddress());
				//System.out.println("hash.size===================="+hash.size());
				//System.out.println("hash对象" + "流速" + hash + "综合流速综合流" + hash+ "速综合流速信息");
				// 取得轮询间隔时间
				TaskXml taskxml = new TaskXml();
				Task task = taskxml.GetXml("netcollecttask");
				int interval = getInterval(task.getPolltime().floatValue(),task.getPolltimeunit());
				Hashtable hashSpeed = new Hashtable();
				Hashtable hashHighSpeed = new Hashtable();
				Hashtable octetsHash = new Hashtable();
				if (hash == null)
					hash = new Hashtable();
				String[] oids = new String[] { 
						"1.3.6.1.2.1.2.2.1.1",
						"1.3.6.1.2.1.2.2.1.2",
						"1.3.6.1.2.1.2.2.1.3",
						"1.3.6.1.2.1.2.2.1.4", 
						"1.3.6.1.2.1.2.2.1.5",
						"1.3.6.1.2.1.31.1.1.1.15"};//ifHighSpeed
				
				String[] oids2 = new String[] { 
						"1.3.6.1.2.1.2.2.1.6",
						"1.3.6.1.2.1.2.2.1.7",// ifAdminStatus 6
						"1.3.6.1.2.1.2.2.1.8",// ifOperStatus 7
						"1.3.6.1.2.1.2.2.1.9",// ifLastChange 8
						"1.3.6.1.2.1.31.1.1.1.1",
						"1.3.6.1.2.1.4.20.1.1",// ipAdEntAddr
						"1.3.6.1.2.1.4.20.1.2"};// ipAdEntIfIndex
				
				String[] oids1 = new String[] { 
						"1.3.6.1.2.1.31.1.1.1.6", // ifHCInOctets
						"1.3.6.1.2.1.31.1.1.1.8",// ifHCInMulticastPkts 10
						"1.3.6.1.2.1.31.1.1.1.9",// ifHCInBroadcastPkts 11
						"1.3.6.1.2.1.2.2.1.13",// ifInDiscards 3
						"1.3.6.1.2.1.2.2.1.14"// ifInErrors 4
				};

				String[] oids3 = new String[] {
						"1.3.6.1.2.1.31.1.1.1.10", // ifHCOutOctets
						"1.3.6.1.2.1.31.1.1.1.12",// ifHCOutMulticastPkts 12
						"1.3.6.1.2.1.31.1.1.1.13",// ifHCOutBroadcastPkts 13
						"1.3.6.1.2.1.2.2.1.19", // ifOutDiscards 8
						"1.3.6.1.2.1.2.2.1.20"// ifOutErrors 9
						
				};
				
				final String[] desc = SnmpMibConstants.NetWorkMibInterfaceDesc0;
				final String[] unit = SnmpMibConstants.NetWorkMibInterfaceUnit0;
				final String[] chname = SnmpMibConstants.NetWorkMibInterfaceChname0;
				final int[] scale = SnmpMibConstants.NetWorkMibInterfaceScale0;
				final String[] desc1 = SnmpMibConstants.NetWorkMibInterfaceDesc1;
				final String[] chname1 = SnmpMibConstants.NetWorkMibInterfaceChname1;
				final String[] unit1 = SnmpMibConstants.NetWorkMibInterfaceUnit3;
				final int[] scale1 = SnmpMibConstants.NetWorkMibInterfaceScale1;
				
				String[][] valueArray = null;
				try {
					valueArray = SnmpUtils.getTableData(host.getIpAddress(),
							host.getCommunity(), oids, host.getSnmpversion(),
							3, 1000 * 30);
				} catch (Exception e) {
				}

				String[][] valueArray2 = null;
				try {
					valueArray2 = SnmpUtils.getTableData(host.getIpAddress(),
							host.getCommunity(), oids2, host.getSnmpversion(),
							3, 1000 * 30);
				} catch (Exception e) {
				}

				String[][] valueArray1 = null;
				try {
					valueArray1 = SnmpUtils.getTableData(host.getIpAddress(),
							host.getCommunity(), oids1, host.getSnmpversion(),
							3, 1000 * 30);
				} catch (Exception e) {
				}
				String[][] valueArray3 = null;
				try {
					valueArray3 = SnmpUtils.getTableData(host.getIpAddress(),
							host.getCommunity(), oids3, host.getSnmpversion(),
							3, 1000 * 30);
				} catch (Exception e) {
				}
				
				long allSpeed = 0;
				long allHighSpeed = 0;
				long allHighInOctetsSpeed = 0;
				long allHighOutOctetsSpeed = 0;
				long allHighOctetsSpeed = 0;

				long allinpacks = 0;
				long inupacks = 0;// 入口单向
				long innupacks = 0;// 非单向
				long indiscards = 0;
				long inerrors = 0;
				long alloutpacks = 0;
				long outupacks = 0;// 出口单向
				long outnupacks = 0;// 非单向
				long outdiscards = 0;
				long outerrors = 0;
				
				//Calendar cal=(Calendar)hash.get("collecttime");
				HostInterfaceDao hidao = new HostInterfaceDao();
				List hiList = null;
				try{
					hiList =hidao.loadInterfaces(host.getIpAddress());
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					hidao.close();
				}
				String hiindex="";
				Vector tempV = new Vector();
				Hashtable tempHash = new Hashtable();
				if (valueArray != null) {
					for (int i = 0; i < valueArray.length; i++) {
						if (valueArray[i][0] == null)
							continue;
						String sIndex = valueArray[i][0].toString();
						
						for(int h=0;h<hiList.size();h++){
							InterfaceNode ifnode = new InterfaceNode();
							ifnode=(InterfaceNode)hiList.get(h);
							hiindex=ifnode.getIfIndex();
							if(hiindex.equals(sIndex)){
								tempV.add(sIndex);
								tempHash.put(i, sIndex);
							}
						}
						HostNodeDao dao = new HostNodeDao();
						String allipstr = SysUtil.doip(host.getIpAddress());
						int count = dao.getTableCount("PORTSTATUS"+allipstr);
						dao.close();
						
						for (int j = 0; j < 6; j++) {
							
							String sValue = valueArray[i][j];
							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity(desc[j]);
							interfacedata.setSubentity(sIndex);
							// 端口状态不保存，只作为静态数据放到临时表里
							interfacedata.setRestype("static");
							interfacedata.setUnit(unit[j]);
							
							// 流速
							if ((j == 4) && sValue != null) {
								// 流速
								long lValue = Long.parseLong(sValue);// yangjun
								hashSpeed.put(sIndex, Long.toString(lValue));
								allSpeed = allSpeed + lValue;
							}
							//HC流速
							if((j == 5) && sValue != null){
								//HC流速
								long lValue = Long.parseLong(sValue);
								hashHighSpeed.put(sIndex, Long.toString(lValue));
								allHighSpeed = allHighSpeed +lValue;
							}
							// 端口类型
							if ((j == 2) && sValue != null) {
								if (Interface_IfType.get(sValue) != null) {
									interfacedata.setThevalue(Interface_IfType.get(sValue).toString());
								} else {
									interfacedata.setThevalue("0.0");
								}
							} else {
								if (scale[j] == 0) {
									interfacedata.setThevalue(sValue);
								} else {
									if (sValue != null) {
										interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/ scale[j]));
									} else {
										interfacedata.setThevalue("0");
									}
								}
							}
							interfacedata.setChname(chname[j]);
							if ("ifPhysAddress".equals(interfacedata.getEntity())) {
								if ((interfacedata.getThevalue() == null)|| (interfacedata.getThevalue().length() > 0 && !interfacedata.getThevalue().contains(":"))) {// mac地址字符串的总长度
									interfacedata.setThevalue("--");
								}
							}
							interfacedata.setCount(count);
							interfaceVector.addElement(interfacedata);
						}
					}
				}

				if (valueArray2 != null) {

					for (int i = 0; i < valueArray2.length; i++) {
						String sIndex = (String) tempHash.get(i);
						if (tempV.contains(sIndex)) {
							if (valueArray2[i][0] == null)
								continue;

							for (int j = 0; j < 5; j++) {
								// 把预期状态和ifLastChange过滤掉
								if (j == 3)
									continue;

								String sValue = valueArray2[i][j];

								interfacedata = new Interfacecollectdata();
								interfacedata.setIpaddress(host.getIpAddress());
								interfacedata.setCollecttime(sdf.format(date.getTime()));
								interfacedata.setCategory("Interface");
								interfacedata.setEntity(desc[5 + j]);
								interfacedata.setSubentity(sIndex);
								// 端口状态不保存，只作为静态数据放到临时表里
								interfacedata.setRestype("static");
								interfacedata.setUnit(unit[5 + j]);
								if ((j == 1 || j == 2) && sValue != null) {// 预期状态和当前状态
									if (ifEntity_ifStatus.get(sValue) != null) {
										interfacedata.setThevalue(ifEntity_ifStatus.get(sValue).toString());
									} else {
										interfacedata.setThevalue("0.0");
									}
								} else {
									if (scale[5 + j] == 0) {
										interfacedata.setThevalue(sValue);
									} else {
										if (sValue != null) {
											interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/ scale[5 + j]));
										} else {
											interfacedata.setThevalue("0");
										}
									}
								}

								interfacedata.setChname(chname[5 + j]);
								if ("ifPhysAddress".equals(interfacedata
										.getEntity())) {
									if ((interfacedata.getThevalue() == null)
											|| (interfacedata.getThevalue()
													.length() > 0 && !interfacedata
													.getThevalue()
													.contains(":"))) {// mac地址字符串的总长度
										interfacedata.setThevalue("--");
									}
								}
								interfaceVector.addElement(interfacedata);
							}
						}
					}
				}

				Hashtable packhash = new Hashtable();
				Hashtable discardshash = new Hashtable();
				Hashtable errorshash = new Hashtable();
				
				if (valueArray1 != null) {
					Calendar cal= null;
					for (int i = 0; i < valueArray1.length; i++) {
						allinpacks = 0;
						inupacks = 0;// 入口广播
						innupacks = 0;// 入口多播
						indiscards = 0;
						inerrors = 0;
						InterfaceNode ifNode = new InterfaceNode();
						
						String sIndex = (String) tempHash.get(i);
						
						if (tempV.contains(sIndex)) {
							for (int j = 0; j < 5; j++) {
								if (valueArray1[i][j] != null) {
									String sValue = valueArray1[i][j];
									interfacedata = new Interfacecollectdata();
									interfacedata.setThevalue(sValue);
									if (j==1 || j==2){
										//入口广播数据包,入口多播数据包												
										if (sValue != null){
											allinpacks=allinpacks+Long.parseLong(sValue);
											cal=(Calendar)hash.get("collecttime");
											long timeInMillis=0;
											if(cal!=null)timeInMillis=cal.getTimeInMillis();
											long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
											
											inpacks=new InPkts();
											inpacks.setIpaddress(host.getIpAddress());
											inpacks.setCollecttime(sdf.format(date.getTime()));
											inpacks.setCategory("Interface");
											String chnameBand="";
											if(j==1){
												inpacks.setEntity("ifHCInMulticastPkts");
												chnameBand="多播";
											}
											if(j==2){
												inpacks.setEntity("ifHCInBroadcastPkts");
												chnameBand="广播";
											}
											inpacks.setSubentity(sIndex);
											inpacks.setRestype("dynamic");
											inpacks.setUnit("");	
											inpacks.setChname(chnameBand);
											BigDecimal currentPacks=new BigDecimal(sValue);
											BigDecimal lastPacks=new BigDecimal(0);	
											BigDecimal l=new BigDecimal(0);								
													
											//如果当前采集时间与上次采集时间的差小于采集间隔两倍，则计算带宽利用率，否则带宽利用率为0；
											if(longinterval<2*interval){
												String lastvalue="";
												
												if(hash.get(desc1[j]+":"+sIndex)!=null){
													lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
												}else{
													lastvalue="0";
												}
												//取得上次获得的Octets
												if(lastvalue!=null && !lastvalue.equals("")){
													lastPacks=new BigDecimal(lastvalue);
												}else{
													lastPacks = new BigDecimal("0");
												}
											}
											if(longinterval!=0){
												BigDecimal packsBetween=currentPacks.subtract(lastPacks);
												l=packsBetween;
												if(lastPacks.equals("0")){
													l=new BigDecimal("0");
												}
											}
											if(sValue == null){
												inpacks.setThevalue("0");
											}else{
												inpacks.setThevalue(l.toString());	
											}
											
											//SysLogger.info(host.getIpAddress()+" 第"+utilhdx.getSubentity()+"端口 "+"inpacks "+Long.toString(l));
											if (cal != null)
												inpacksVector.addElement(inpacks);	
										}
											//continue;
									}
									if (j == 3) {
										// 入口丢弃的数据包
										if (sValue != null){
											indiscards = Long.parseLong(sValue);
										}else{
											indiscards = 0;
										}continue;
									}
									if (j == 4) {
										// 入口错误的数据包
										if (sValue != null){
											inerrors = Long.parseLong(sValue);
										}else{
											inerrors = 0;
										}continue;
									}
									
									// 计算每个端口流速及利用率
									if (j == 0) {
										cal=(Calendar)hash.get("collecttime");
										long timeInMillis=0;
										if(cal!=null)timeInMillis=cal.getTimeInMillis();
										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
										portIPS = new PortIPS();
										portIPS.setIpaddress(host.getIpAddress());
										portIPS.setCollecttime(sdf.format(date.getTime()));
										portIPS.setCategory("Interface");
										if (j == 0) {
											portIPS.setEntity("入口");
										}
										portIPS.setRestype("dynamic");
										portIPS.setSubentity(sIndex);
										portIPS.setUtilhdxunit(unit1[j]);
										BigDecimal currentHighOctets = new BigDecimal(sValue);
										BigDecimal lastHighOctets = new BigDecimal(0);
										double l = 0.00;
										double octets = 0.00;
										// 如果当前采集时间与上次采集时间的差小于采集间隔两倍，则计算带宽利用率，否则带宽利用率为0；
										String lastvalue = "";

										if (hash.get(desc1[j] + ":" + sIndex) != null)
											lastvalue = hash.get(desc1[j] + ":" + sIndex).toString();
										// 取得上次获得的HighOctets
										if (lastvalue != null && !lastvalue.equals(""))
											lastHighOctets = new BigDecimal(lastvalue);
										if (longinterval != 0) {
											double octetsHighBetween = 0.00;
											// 现流量-前流量
											octetsHighBetween = Long.valueOf(currentHighOctets.subtract(lastHighOctets).toString());
											octets = octetsHighBetween/(1000*1000*8);
											if(octets<0){
												octets=0;
											}
											//上次流量
											double beforeOctets=0.00;
											if(ShareData.getUtilhdxdata(desc1[j]+":"+host.getIpAddress()+":"+sIndex)!=null){
												beforeOctets= ShareData.getUtilhdxdata(desc1[j]+":"+host.getIpAddress()+":"+sIndex);
												utilfalg=String.valueOf(beforeOctets);
												//当前流量都不为0
												if (beforeOctets!=0) {
													//大于10倍为异常流量
													if (10*beforeOctets<=octets ){
														//给插入标识为异常流量
														portIPS.setUtilhdxflag("1");
													} else {
														ShareData.setUtilhdxdata(desc1[j]+":"+host.getIpAddress()+":"+sIndex,octets);
														portIPS.setUtilhdxflag("0");
													}
												}else{
													ShareData.setUtilhdxdata(desc1[j]+":"+host.getIpAddress()+":"+sIndex,octets);
													portIPS.setUtilhdxflag("0");
												}
												
											}else{
												ShareData.setUtilhdxdata(desc1[j]+":"+host.getIpAddress()+":"+sIndex,octets);
											}
											// 计算端口速率
											l = octets / longinterval;
											
											// yangjun
											if (j == 0)
												allHighInOctetsSpeed = allHighInOctetsSpeed+ new Double(l).longValue();
											allHighOctetsSpeed = allHighOctetsSpeed + new Double(l).longValue();
										}
										DecimalFormat df = new DecimalFormat("#.##");// yangjun
										portIPS.setUtilhdx(df.format(octets));
										portIPS.setPercunit("%");
										double highSpeed = 0.0;
										if (hashHighSpeed.get(sIndex) != null) {
											highSpeed = Double.parseDouble(hashHighSpeed.get(sIndex).toString());
										} else {
											highSpeed = Double.parseDouble("0.0");
										}
										double d = 0.0;
										portIPS.setIfSpeed(highSpeed);
										if (highSpeed > 0) {
											
											// 带宽利用率＝流速×8*100/ifspeed%
											d = Arith.div(8*l*100, highSpeed);
										}
										portIPS.setUtilhdxPerc(Double.toString(d));
									} 
									octetsHash.put(desc1[j] + ":" + sIndex,interfacedata.getThevalue());
								} 
							}
							packhash=ShareData.getPacksdata(host.getIpAddress()+":"+sIndex);
							discardshash=ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex);
							errorshash=ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex);									
							//计算传输的数据包
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllInCastPkts");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("个");
							interfacedata.setThevalue(allinpacks+"");
							interfacedata.setChname("入口总数据包");
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllInDiscards");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("个");
							interfacedata.setThevalue(indiscards+"");
							interfacedata.setChname("入口总丢包数");
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllInErrors");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("个");
							interfacedata.setThevalue(inerrors+"");
							interfacedata.setChname("入口错误包数");

							String lastvalue="";
							long lastpacks=0;
							//入口传输数据包
							if (packhash != null){
								if(packhash.get("AllInCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllInCastPkts"+":"+sIndex).toString();
							}
							
							//取得上次获得的packs
							if(lastvalue!=null && !lastvalue.equals("")){										
								lastpacks=Long.parseLong(lastvalue);									
							}
							
							//入口丢包率
							lastvalue="";
							long lastdiscards=0;
							if (discardshash != null){
								if(discardshash.get("AllInDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllInDiscards"+":"+sIndex).toString();
							}
							
							//取得上次获得的packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lastdiscards=Long.parseLong(lastvalue);									
							}
										
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
							portIPS.setDiscardsPerc(Double.toString(indiscardserc));
										
							//入口错误率
							lastvalue="";
							long lasterrors=0;
							if (errorshash != null){
								if(errorshash.get("AllInErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllInErrors"+":"+sIndex).toString();
							}									
							//取得上次获得的error
							if(lastvalue!=null && !lastvalue.equals("")){
								lasterrors=Long.parseLong(lastvalue);									
							}																		
							
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
							portIPS.setErrorsPerc(Double.toString(inerrorsperc));
							if(!utilfalg.equals(""))
								utilhdxVector.addElement(portIPS);
							lastvalue="";
							lastpacks=0;
							lastdiscards=0;
							lasterrors=0;
										
							/* 添加到内存里*/
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

						} 
					}
				}

				if (valueArray3 != null) {
					for (int i = 0; i < valueArray3.length; i++) {
						alloutpacks = 0;
						outupacks = 0;// 出口单向
						outnupacks = 0;// 非单向
						outdiscards = 0;
						outerrors = 0;
						
						String sIndex = (String) tempHash.get(i);
						if (tempV.contains(sIndex)) {
							Calendar cal=null;
							for (int j = 0; j < 5; j++) {
								if (valueArray3[i][j] != null) {
									String sValue = valueArray3[i][j];
									interfacedata = new Interfacecollectdata();
									interfacedata.setThevalue(sValue);
									if (j==1 || j==2){
										//出口广播数据包,出口多播数据包												
										if (sValue != null){
											alloutpacks=alloutpacks+Long.parseLong(sValue);
											cal=(Calendar)hash.get("collecttime");
											long timeInMillis=0;
											if(cal!=null)timeInMillis=cal.getTimeInMillis();
											long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
											
											outpacks=new OutPkts();
											outpacks.setIpaddress(host.getIpAddress());
											outpacks.setCollecttime(sdf.format(date.getTime()));
											outpacks.setCategory("Interface");
											String chnameBand="";
											if(j==1){
												outpacks.setEntity("ifHCOutMulticastPkts");
												chnameBand="多播";
											}
											if(j==2){
												outpacks.setEntity("ifHCOutBroadcastPkts");
												chnameBand="广播";
											}
											outpacks.setSubentity(sIndex);
											outpacks.setRestype("dynamic");
											outpacks.setUnit("");	
											outpacks.setChname(chnameBand);
											BigDecimal currentPacks=new BigDecimal(sValue);												
											BigDecimal lastPacks=new BigDecimal(0);	
											BigDecimal l=new BigDecimal(0);												
													
											//如果当前采集时间与上次采集时间的差小于采集间隔两倍，则计算带宽利用率，否则带宽利用率为0；
											if(longinterval<2*interval){
												String lastvalue="";
												if(hash.get(desc1[j]+":"+sIndex)!=null){
													lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
												}else{
													lastvalue="0";
												}
												//取得上次获得的Octets
												if(lastvalue!=null && !lastvalue.equals("")){
													lastPacks=new BigDecimal(lastvalue);
												}else{
													lastPacks = new BigDecimal("0");
												}
											}
											if(longinterval!=0){
												
												BigDecimal packsBetween=currentPacks.subtract(lastPacks);
												l=packsBetween;
												if(lastPacks.equals("0")){
													l=new BigDecimal("0");
												}
											}
											if(sValue == null){
												outpacks.setThevalue("0");
											}else{
												outpacks.setThevalue(l.toString());	
											}
											if (cal != null)
												outpacksVector.addElement(outpacks);	
										}
											//continue;
									}
									if (j == 3) {
										// 出口丢弃的数据包
										if (sValue != null){
											outdiscards = Long.parseLong(sValue);
										}else{
											outdiscards = 0;
										}
											
										continue;
									}
									if (j == 4) {
										// 出口错误的数据包
										if (sValue != null){
											outerrors = Long.parseLong(sValue);
										}else{
											outerrors = 0;
										}
											
										continue;
									}
									
									// 计算每个端口流速及利用率
									if (j == 0) {
										long timeInMillis = 0;
										cal=(Calendar)hash.get("collecttime");
										if (cal != null)
											timeInMillis = cal.getTimeInMillis();
										long longinterval = (date.getTimeInMillis() - timeInMillis) / 1000;
										portIPS = new PortIPS();
										portIPS.setIpaddress(host.getIpAddress());
										portIPS.setCollecttime(sdf.format(date.getTime()));
										portIPS.setCategory("Interface");
										if (j == 0) {
											portIPS.setEntity("出口");
										}
										portIPS.setSubentity(sIndex);
										portIPS.setRestype("dynamic");
										portIPS.setUtilhdxunit(unit1[1+ j]);
										BigDecimal currentHighOctets =new BigDecimal(sValue);
										BigDecimal lastHighOctets = new BigDecimal(0);
										double l = 0.00;
										double octets = 0.00;
										// 如果当前采集时间与上次采集时间的差小于采集间隔两倍，则计算带宽利用率，否则带宽利用率为0；
										String lastvalue = "";

										if (hash.get(desc1[5 + j] + ":"+ sIndex) != null)
											lastvalue = hash.get(desc1[5 + j] + ":"+ sIndex).toString();
										// 取得上次获得的Octets
										if (lastvalue != null && !lastvalue.equals(""))
											lastHighOctets =new BigDecimal(lastvalue);
										if (longinterval != 0) {
											double octetsHighBetween = 0.00;
											
											// 现流量-前流量
											octetsHighBetween = Long.valueOf(currentHighOctets.subtract(lastHighOctets).toString());
											octets = octetsHighBetween/(1000*1000*8);
											if(octets<0){
												octets=0;
											}
											//上次流量
											double beforeOctets=0.00;
											if(ShareData.getUtilhdxdata(desc1[5+j]+":"+host.getIpAddress()+":"+sIndex)!=null){
												beforeOctets= ShareData.getUtilhdxdata(desc1[5+j]+":"+host.getIpAddress()+":"+sIndex);
												utilfalg=String.valueOf(beforeOctets);
												//上次流量都不为0
												if (beforeOctets!=0) {
													//大于10倍为异常流量
													if ((10*beforeOctets)<=octets ){
														//给插入标识为异常流量
														portIPS.setUtilhdxflag("1");
													} else {
														ShareData.setUtilhdxdata(desc1[5+j]+":"+host.getIpAddress()+":"+sIndex,octets);
														portIPS.setUtilhdxflag("0");
													}
												} else {
													ShareData.setUtilhdxdata(desc1[5+j]+":"+host.getIpAddress()+":"+sIndex,octets);
													portIPS.setUtilhdxflag("0");
												}
												
											}else{
												ShareData.setUtilhdxdata(desc1[5+j]+":"+host.getIpAddress()+":"+sIndex,octets);
											}
											// 计算端口速率
											l = octets / longinterval;
											// 统计总出入字节利用率,备用计算（出、入、综合）带宽利用率
											if (j == 0)
												allHighOutOctetsSpeed = allHighOutOctetsSpeed+ new Double(l).longValue();
											allHighOctetsSpeed = allHighOctetsSpeed + new Double(l).longValue();

										}
										DecimalFormat df = new DecimalFormat("#.##");
										portIPS.setUtilhdx(df.format(octets));
										
										portIPS.setPercunit("%");
										double highSpeed = 0.0;
										if (hashHighSpeed.get(sIndex) != null) {
											highSpeed = Double.parseDouble(hashHighSpeed.get(sIndex).toString());
										} else {
											highSpeed = Double.parseDouble("0.0");
										}
										portIPS.setIfSpeed(highSpeed);
										double d = 0.00;
										if (highSpeed > 0) {
											d = Arith.div(8*l*100, highSpeed);
										}
										portIPS.setUtilhdxPerc(Double.toString(d));
									}
									octetsHash.put(desc1[5 + j] + ":" + sIndex,interfacedata.getThevalue());
								} 
							}
							packhash=ShareData.getPacksdata(host.getIpAddress()+":"+sIndex);
							discardshash=ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex);
							errorshash=ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex);									
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutCastPkts");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("个");
							interfacedata.setThevalue(alloutpacks+"");
							interfacedata.setChname("出口总数据包");

							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutDiscards");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("个");
							interfacedata.setThevalue(outdiscards+"");
							interfacedata.setChname("出口总丢包数");
							
							interfacedata=new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutErrors");
							interfacedata.setSubentity(sIndex);									
							interfacedata.setRestype("static");
							interfacedata.setUnit("个");
							interfacedata.setThevalue(outerrors+"");
							interfacedata.setChname("出口错误包数");
							
							String lastvalue="";
							long lastpacks=0;
										
							Packs packs = new Packs();
							//出口丢包率
							lastvalue="";
							long lastdiscards=0;
										
							//出口错误率
							lastvalue="";
							long lasterrors=0;
							
							lastvalue="";
							lastpacks=0;
							lastdiscards=0;
							lasterrors=0;
							//出口传输数据包
							if (packhash != null){
								if(packhash.get("AllOutCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllOutCastPkts"+":"+sIndex).toString();
							}
							//取得上次获得的packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lastpacks=Long.parseLong(lastvalue);																		
							}			
							//计算丢包率和错误率
							if (discardshash != null){
								if(discardshash.get("AllOutDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllOutDiscards"+":"+sIndex).toString();
							}									
							//取得上次获得的packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lastdiscards=Long.parseLong(lastvalue);									
							}																		
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
										
							portIPS.setDiscardsPerc(Double.toString(outdiscardserc));
							
							lastvalue="";
							if (errorshash != null){
								if(errorshash.get("AllOutErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllOutErrors"+":"+sIndex).toString();
							}									
							//取得上次获得的packs
							if(lastvalue!=null && !lastvalue.equals("")){
								lasterrors=Long.parseLong(lastvalue);									
							}																											
							double outerrorsperc=0.0;
							if (alloutpacks>0){
								if ((alloutpacks-lastpacks)>0){
									outerrorsperc=(outerrors-lasterrors)/(alloutpacks-lastpacks);
								}else{
									outerrorsperc=0;
								}
							}else{
								outerrorsperc=0;
							}									
							portIPS.setErrorsPerc(Double.toString(outerrorsperc));
							
							if (!utilfalg.equals(""))
								utilhdxVector.addElement(portIPS);

							
							/* 添加到内存里*/
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
						} // end for contains

					}
				}

				String flag = "0";
				hashSpeed = null;
				octetsHash.put("collecttime", date);

				if (hash != null) {
					flag = (String) hash.get("flag");
					if (flag == null) {
						flag = "0";
					} else {
						if (flag.equals("0")) {
							flag = "1";
						} else {
							flag = "0";
						}
					}
				}
				octetsHash.put("flag", flag);

				ShareData.setOctetsdata(host.getIpAddress(), octetsHash);

			} catch (Exception e) {
				e.printStackTrace();
			}
			// -------------------------------------------------------------------------------------------interface
			// end
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		if (!(ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			Hashtable ipAllData = new Hashtable();
			if (ipAllData == null) ipAllData = new Hashtable();
			if (interfaceVector != null && interfaceVector.size() > 0) ipAllData.put("interface", interfaceVector);
			if (utilhdxVector != null && utilhdxVector.size() > 0) ipAllData.put("utilhdx", utilhdxVector);
//			if (discardspercVector != null && discardspercVector.size() > 0) ipAllData.put("discardsperc", discardspercVector);
//			if (errorspercVector != null && errorspercVector.size() > 0) ipAllData.put("errorsperc", errorspercVector);
//			if (packsVector != null && packsVector.size() > 0) ipAllData.put("packs", packsVector);
//			if (inpacksVector != null && inpacksVector.size() > 0) ipAllData.put("inpacks", inpacksVector);
//			if (outpacksVector != null && outpacksVector.size() > 0) ipAllData.put("outpacks", inpacksVector);
			ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		} else {
			if (interfaceVector != null && interfaceVector.size() > 0) ((Hashtable) ShareData.getSharedata().get(host.getIpAddress())) .put("interface", interfaceVector);
//			if (utilhdxpercVector != null && utilhdxpercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("utilhdxperc",utilhdxpercVector);
			if (utilhdxVector != null && utilhdxVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("utilhdx",utilhdxVector);
//			if (discardspercVector != null && discardspercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("discardsperc",discardspercVector);
//			if (errorspercVector != null && errorspercVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("errorsperc",errorspercVector);
//			if (packsVector != null && packsVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("packs",packsVector);
//			if (inpacksVector != null && inpacksVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("inpacks",inpacksVector);
//			if (outpacksVector != null && outpacksVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("outpacks",outpacksVector);
		}
		//returnHash.put("interface", interfaceVector); // 端口状态
		returnHash.put("utilhdx", utilhdxVector); // 改成 端口流量
		System.out.println("utilhdxVector大小========="+utilhdxVector.size());

		outpacksVector = null;
		inpacksVector = null;
		packsVector = null;
		errorspercVector = null;
		discardspercVector = null;
		utilhdxVector = null;
		utilhdxpercVector = null;
		interfaceVector = null;
		System.out.println("--------collectdata-----1---------------");
		NetinterfaceResultTosql tosql = new NetinterfaceResultTosql();
		tosql.CreateResultTosql(returnHash, host.getIpAddress());
		System.out.println("--------collectdata-----2---------------");
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
		try {
			int numThreads = 200;
			try {
				List numList = new ArrayList();
				TaskXml taskxml = new TaskXml();
				numList = taskxml.ListXml();
				for (int i = 0; i < numList.size(); i++) {
					Task task = new Task();
					BeanUtils.copyProperties(task, numList.get(i));
					if (task.getTaskname().equals("netcollecttask")) {
						numThreads = task.getPolltime().intValue();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			int threadInterval = 5;
			long interval = 0;		
			long currentTime = 0;		
			long operateTime = 0;		
			boolean flag;		
			int offset = 0;		
			long initTime = 0;	
			ThreadPool threadPool = null;
			
			initTime=System.currentTimeMillis()/1000;
			if(initTime%(60*5)==0){
				threadPool=new ThreadPool(numThreads);
				Calendar date = Calendar.getInstance();
				//date.setTime(new Date(initTime));
				threadPool.runTask(createTask(node,date));
				threadPool.join();
				threadPool.close();
				threadPool = null;
			}else{
				String curDate=DateUtil.getRightNow();
				String curDayAndHour=curDate.substring(0,14);
				String curMin = curDate.substring(14,16);
				String curHour = curDate.substring(11,13);
				int cu=Integer.valueOf(curMin)/5;
				int ho = Integer.valueOf(curHour);
				String inMin = "";
				if(cu==11){
					if(ho==23){
						curDayAndHour=DateUtil.getDateAddByDate(curDate, 1).substring(0,11)+"00:00:00";
					}else{
						if(ho<9){
							curDayAndHour=curDate.substring(0,11)+"0"+String.valueOf(ho+1)+":00:00";
						}else{
							curDayAndHour=curDate.substring(0,11)+String.valueOf(ho+1)+":00:00";
						}
						
					}
				}else{
					inMin=String.valueOf(((cu+1)*5));
					curDayAndHour=curDayAndHour+inMin+":00";
				}
				try {
					initTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(curDayAndHour).getTime()/1000;
				} catch (ParseException e1) {
					e1.printStackTrace();	
				}
				while (true) {	
					operateTime = initTime + interval;
					currentTime = System.currentTimeMillis()/1000;
					flag = true;
					while (flag) {		
						if(currentTime == operateTime){
							threadPool=new ThreadPool(numThreads);
							Calendar date = Calendar.getInstance();
							//date.setTime(new Date(currentTime));
							threadPool.runTask(createTask(node,date));
							threadPool.join();
							threadPool.close();
							threadPool = null;
							flag = false;				
						}else {
							currentTime = System.currentTimeMillis()/1000;
						}			
					}		
					interval += threadInterval * 60;
					currentTime = System.currentTimeMillis()/1000;		
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// SysLogger.info("********Ping Thread Count : "+Thread.activeCount());
		}

	}



	/**
	 * 创建任务
	 */
	private static Runnable createTask(final HostNode hostnode,final Calendar date) {
		return new Runnable() {
			public void run() {
				try {
					System.out.println("------createTask-------1---------------");
					System.out.println("------createTask-------2---------------");
					new InterfaceTask().collect_Data(hostnode,date);
					System.out.println("------createTask-------3---------------");
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		};
	}
	
	private static String dateIPS;
	public static void main(String[] args) throws Exception{
		InterfaceTask init = new InterfaceTask();
		init.run();
		//String curDate=DateUtil.getRightNow();
//		String curDate="2016-11-28 00:05:34";
//		String curDayAndHour=curDate.substring(0,14);
//		String curMin = curDate.substring(14,16);
//		String curHour = curDate.substring(11,13);
//		int cu=Integer.valueOf(curMin)/5;
//		int ho = Integer.valueOf(curHour);
//		String inMin = "";
//		if(cu==11){
//			if(ho==23){
//				curDayAndHour=DateUtil.getDateAddByDate(curDate, 1).substring(0,11)+"00:00:00";
//			}else{
//				if(ho<9){
//					curDayAndHour=curDate.substring(0,11)+"0"+String.valueOf(ho+1)+":00:00";
//				}else{
//					curDayAndHour=curDate.substring(0,11)+String.valueOf(ho+1)+":00:00";
//				}
//				
//			}
//		}else{
//			inMin=String.valueOf(((cu+1)*5));
//			curDayAndHour=curDayAndHour+inMin+":00";
//		}
//		try {
//			Long initTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(curDayAndHour).getTime()/1000;
//		} catch (ParseException e1) {
//			e1.printStackTrace();	
//		}	
//		System.out.println(curDayAndHour);
//		System.out.println(initTime1);
			
	}
}

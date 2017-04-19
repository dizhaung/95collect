package com.afunms.polling.task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.afunms.common.util.Arith;
import com.afunms.common.util.DateUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpService;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.PortIPS;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.topology.dao.HostInterfaceDao;
import com.afunms.topology.model.HostNode;
import com.afunms.topology.model.InterfaceNode;
import com.gatherResulttosql.NetinterfaceResultTosql;

public class InterfaceTaskHC extends MonitorTask {
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

	public InterfaceTaskHC() {
	}



	private Log logger = LogFactory.getLog(InterfaceTaskHC.class);

	public Hashtable collect_Data(HostNode host, Calendar date) {
		host.setSnmpversion(1);
		logger.info("开始采集" + host.getIpAddress() + "数据");
		Hashtable returnHash = new Hashtable();
		Vector utilhdxVector = new Vector();
		Vector inpacksVector = new Vector();
		Vector outpacksVector = new Vector();
//		String utilfalg = "";
		try {
			Interfacecollectdata interfacedata = null;
			PortIPS portIPS = new PortIPS();
			InPkts inpacks = new InPkts();
			OutPkts outpacks = new OutPkts();
			// start
			try {
				Hashtable hash = ShareData.getOctetsdata(host.getIpAddress());

				int interval = getInterval(1f, "m");
				Hashtable hashHighSpeed = new Hashtable();
				Hashtable octetsHash = new Hashtable();
				if (hash == null)
					hash = new Hashtable();
				/*String[] oids = new String[] { "1.3.6.1.2.1.2.2.1.1",
						"1.3.6.1.2.1.2.2.1.2", "1.3.6.1.2.1.2.2.1.3",
						"1.3.6.1.2.1.2.2.1.4", "1.3.6.1.2.1.31.1.1.1.15" };// ifHighSpeed

				String[] oids1 = new String[] { "1.3.6.1.2.1.31.1.1.1.6", // ifHCInOctets
						"1.3.6.1.2.1.31.1.1.1.8",// ifHCInMulticastPkts 10
						"1.3.6.1.2.1.31.1.1.1.9",// ifHCInBroadcastPkts 11
						"1.3.6.1.2.1.2.2.1.13",// ifInDiscards 3
						"1.3.6.1.2.1.2.2.1.14"// ifInErrors 4
				};

				String[] oids3 = new String[] { "1.3.6.1.2.1.31.1.1.1.10", // ifHCOutOctets
						"1.3.6.1.2.1.31.1.1.1.12",// ifHCOutMulticastPkts 12
						"1.3.6.1.2.1.31.1.1.1.13",// ifHCOutBroadcastPkts 13
						"1.3.6.1.2.1.2.2.1.19", // ifOutDiscards 8
						"1.3.6.1.2.1.2.2.1.20"// ifOutErrors 9

				};*/
				String[] oids =                
						  new String[] {               
							"1.3.6.1.2.1.2.2.1.1", 
							"1.3.6.1.2.1.2.2.1.2",
							"1.3.6.1.2.1.2.2.1.3",
							"1.3.6.1.2.1.2.2.1.4",
							"1.3.6.1.2.1.2.2.1.5"				
							  };
					  String[] oids2 =                
						  new String[] {          
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
				};	
			
			String[] oids3=                
				 new String[] {     	
				"1.3.6.1.2.1.2.2.1.16", //ifOutOctets 5
				"1.3.6.1.2.1.31.1.1.1.4",//ifOutMulticastPkts 6
				"1.3.6.1.2.1.31.1.1.1.5",//ifOutBroadcastPkts 7
				"1.3.6.1.2.1.2.2.1.19",	//ifOutDiscards 8
				"1.3.6.1.2.1.2.2.1.20"//ifOutErrors 9								
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
				long inupacks = 0;// ��ڵ���
				long innupacks = 0;// �ǵ���
				long indiscards = 0;
				long inerrors = 0;
				long alloutpacks = 0;
				long outupacks = 0;// ���ڵ���
				long outnupacks = 0;// �ǵ���
				long outdiscards = 0;
				long outerrors = 0;

				HostInterfaceDao hidao = new HostInterfaceDao();
				List hiList = null;
				try {
					hiList = hidao.loadInterfaces(host.getIpAddress());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					hidao.close();
				}
				String hiindex = "";
				Vector tempV = new Vector();
				Hashtable tempHash = new Hashtable();
				if (valueArray != null) {
					for (int i = 0; i < valueArray.length; i++) {
						if (valueArray[i][0] == null)
							continue;
						String sIndex = valueArray[i][0].toString();
						tempV.add(sIndex);
						tempHash.put(i, sIndex);
//						for (int h = 0; h < hiList.size(); h++) {
//							InterfaceNode ifnode = new InterfaceNode();
//							ifnode = (InterfaceNode) hiList.get(h);
//							hiindex = ifnode.getIfIndex();
//							if (hiindex.equals(sIndex)) {
//								tempV.add(sIndex);
//								tempHash.put(i, sIndex);
//							}
//						}

						for (int j = 0; j < 5; j++) {

							String sValue = valueArray[i][j];

							// HC����
							if ((j == 4) && sValue != null) {
								// HC����
								long lValue = Long.parseLong(sValue);
								hashHighSpeed
										.put(sIndex, Long.toString(lValue));
								allHighSpeed = allHighSpeed + lValue;
							}
						}
					}
				}

				Hashtable packhash = new Hashtable();
				Hashtable discardshash = new Hashtable();
				Hashtable errorshash = new Hashtable();

				if (valueArray1 != null) {
					Calendar cal = null;
					for (int i = 0; i < valueArray1.length; i++) {
						allinpacks = 0;
						inupacks = 0;// ��ڹ㲥
						innupacks = 0;// ��ڶಥ
						indiscards = 0;
						inerrors = 0;

						String sIndex = (String) tempHash.get(i);

						if (tempV.contains(sIndex)) {
							for (int j = 0; j < 5; j++) {
								if (valueArray1[i][j] != null) {
									String sValue = valueArray1[i][j];
									interfacedata = new Interfacecollectdata();
									interfacedata.setThevalue(sValue);
									if (j == 1 || j == 2) {
										// ��ڹ㲥���ݰ�,��ڶಥ���ݰ�
										if (sValue != null) {
											allinpacks = allinpacks
													+ Long.parseLong(sValue);
											cal = (Calendar) hash
													.get("collecttime");
											long timeInMillis = 0;
											if (cal != null)
												timeInMillis = cal
														.getTimeInMillis();
											long longinterval = (date
													.getTimeInMillis() - timeInMillis) / 1000;

											inpacks = new InPkts();
											inpacks.setIpaddress(host
													.getIpAddress());
											inpacks.setCollecttime(sdf
													.format(date.getTime()));
											inpacks.setCategory("Interface");
											String chnameBand = "";
											if (j == 1) {
												inpacks.setEntity("ifHCInMulticastPkts");
												chnameBand = "�ಥ";
											}
											if (j == 2) {
												inpacks.setEntity("ifHCInBroadcastPkts");
												chnameBand = "�㲥";
											}
											inpacks.setSubentity(sIndex);
											inpacks.setRestype("dynamic");
											inpacks.setUnit("");
											inpacks.setChname(chnameBand);
											BigDecimal currentPacks = new BigDecimal(
													sValue);
											BigDecimal lastPacks = new BigDecimal(
													0);
											BigDecimal l = new BigDecimal(0);

											if (longinterval < 2 * interval) {
												String lastvalue = "";

												if (hash.get(desc1[j] + ":"
														+ sIndex) != null) {
													lastvalue = hash.get(
															desc1[j] + ":"
																	+ sIndex)
															.toString();
												} else {
													lastvalue = "0";
												}
												// ȡ���ϴλ�õ�Octets
												if (lastvalue != null
														&& !lastvalue
																.equals("")
														&& !lastvalue
																.equals("0")) {
													lastPacks = new BigDecimal(
															lastvalue);
												} else {
													lastPacks = new BigDecimal(
															"0");
												}
											}
											if (longinterval != 0) {
												BigDecimal packsBetween = currentPacks
														.subtract(lastPacks);
												l = packsBetween;
												if (lastPacks.equals("0")) {
													l = new BigDecimal("0");
												}
											}
											if (sValue == null) {
												inpacks.setThevalue("0");
											} else {
												inpacks.setThevalue(l
														.toString());
											}

											if (cal != null)
												inpacksVector
														.addElement(inpacks);
										}
										// continue;
									}
									if (j == 3) {
										// ��ڶ��������ݰ�
										if (sValue != null) {
											indiscards = Long.parseLong(sValue);
										} else {
											indiscards = 0;
										}
										continue;
									}
									if (j == 4) {
										// ��ڴ�������ݰ�
										if (sValue != null) {
											inerrors = Long.parseLong(sValue);
										} else {
											inerrors = 0;
										}
										continue;
									}

									// ����ÿ���˿����ټ�������
									if (j == 0) {
										cal = (Calendar) hash
												.get("collecttime");
										long timeInMillis = 0;
										if (cal != null)
											timeInMillis = cal
													.getTimeInMillis();
										long longinterval = (date
												.getTimeInMillis() - timeInMillis) / 1000;
										portIPS = new PortIPS();
										portIPS.setIpaddress(host
												.getIpAddress());
										portIPS.setCollecttime(sdf.format(date
												.getTime()));
										portIPS.setCategory("Interface");
										if (j == 0) {
											portIPS.setEntity("���");
										}
										portIPS.setRestype("dynamic");
										portIPS.setSubentity(sIndex);
										portIPS.setUtilhdxunit(unit1[j]);
										BigDecimal currentHighOctets = new BigDecimal(
												sValue);
										BigDecimal lastHighOctets = new BigDecimal(
												0);
										double l = 0.00;
										double octets = 0.00;
										// �����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										String lastvalue = "";

										if (hash.get(desc1[j] + ":" + sIndex) != null)
											lastvalue = hash.get(
													desc1[j] + ":" + sIndex)
													.toString();
										// ȡ���ϴλ�õ�HighOctets
										if (lastvalue != null
												&& !lastvalue.equals("")
												&& !lastvalue.equals("0"))
											lastHighOctets = new BigDecimal(
													lastvalue);
										if (longinterval != 0) {
											double octetsHighBetween = 0.00;
											// ������-ǰ����
											octetsHighBetween = Long
													.valueOf(currentHighOctets
															.subtract(
																	lastHighOctets)
															.toString());
											octets = octetsHighBetween
													/ (1000 * 1000 * 8);
											if (octets < 0) {
												octets = 0;
											}
											// �ϴ�����
											double beforeOctets = 0.00;
											if (ShareData
													.getUtilhdxdata(desc1[j]
															+ ":"
															+ host.getIpAddress()
															+ ":" + sIndex) != null) {
												beforeOctets = ShareData
														.getUtilhdxdata(desc1[j]
																+ ":"
																+ host.getIpAddress()
																+ ":" + sIndex);
//												utilfalg = String.valueOf(beforeOctets);
												// ��ǰ��������Ϊ0
												if (beforeOctets != 0) {
													// ����10��Ϊ�쳣����
													if (10 * beforeOctets <= octets) {
														// �������ʶΪ�쳣����
														portIPS.setUtilhdxflag("1");
													} else {
														ShareData
																.setUtilhdxdata(
																		desc1[j]
																				+ ":"
																				+ host.getIpAddress()
																				+ ":"
																				+ sIndex,
																		octets);
														portIPS.setUtilhdxflag("0");
													}
												} else {
													ShareData
															.setUtilhdxdata(
																	desc1[j]
																			+ ":"
																			+ host.getIpAddress()
																			+ ":"
																			+ sIndex,
																	octets);
													portIPS.setUtilhdxflag("0");
												}

											} else {
												ShareData
														.setUtilhdxdata(
																desc1[j]
																		+ ":"
																		+ host.getIpAddress()
																		+ ":"
																		+ sIndex,
																octets);
											}
											// ����˿�����
											l = octets / longinterval;

											// yangjun
											if (j == 0)
												allHighInOctetsSpeed = allHighInOctetsSpeed
														+ new Double(l)
																.longValue();
											allHighOctetsSpeed = allHighOctetsSpeed
													+ new Double(l).longValue();
										}
										DecimalFormat df = new DecimalFormat(
												"#.##");// yangjun
										portIPS.setUtilhdx(df.format(octets));
										portIPS.setPercunit("%");
										double highSpeed = 0.0;
										if (hashHighSpeed.get(sIndex) != null) {
											highSpeed = Double
													.parseDouble(hashHighSpeed
															.get(sIndex)
															.toString());
										} else {
											highSpeed = Double
													.parseDouble("0.0");
										}
										double d = 0.0;
										portIPS.setIfSpeed(highSpeed);
										if (highSpeed > 0) {

											// ���������ʣ����١�8*100/ifspeed%
											d = Arith.div(8 * l * 100,
													highSpeed);
										}
										portIPS.setUtilhdxPerc(Double
												.toString(d));
									}
									octetsHash.put(desc1[j] + ":" + sIndex,
											interfacedata.getThevalue());
								}
							}
							packhash = ShareData.getPacksdata(host
									.getIpAddress() + ":" + sIndex);
							discardshash = ShareData.getDiscardsdata(host
									.getIpAddress() + ":" + sIndex);
							errorshash = ShareData.getErrorsdata(host
									.getIpAddress() + ":" + sIndex);
							// ���㴫������ݰ�
							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date
									.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllInCastPkts");
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(allinpacks + "");
							interfacedata.setChname("��������ݰ�");

							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date
									.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllInDiscards");
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(indiscards + "");
							interfacedata.setChname("����ܶ�����");

							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date
									.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllInErrors");
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(inerrors + "");
							interfacedata.setChname("��ڴ������");

							String lastvalue = "";
							long lastpacks = 0;
							// ��ڴ������ݰ�
							if (packhash != null) {
								if (packhash
										.get("AllInCastPkts" + ":" + sIndex) != null)
									lastvalue = packhash.get(
											"AllInCastPkts" + ":" + sIndex)
											.toString();
							}

							// ȡ���ϴλ�õ�packs
							if (lastvalue != null && !lastvalue.equals("")
									&& !lastvalue.equals("0")) {
								lastpacks = Long.parseLong(lastvalue);
							}

							// ��ڶ�����
							lastvalue = "";
							long lastdiscards = 0;
							if (discardshash != null) {
								if (discardshash.get("AllInDiscards" + ":"
										+ sIndex) != null)
									lastvalue = discardshash.get(
											"AllInDiscards" + ":" + sIndex)
											.toString();
							}

							// ȡ���ϴλ�õ�packs
							if (lastvalue != null && !lastvalue.equals("")) {
								lastdiscards = Long.parseLong(lastvalue);
							}

							double indiscardserc = 0.0;
							if (allinpacks == 0) {
								indiscardserc = 0;
							} else {
								if (allinpacks - lastpacks > 0) {
									indiscardserc = (indiscards - lastdiscards)
											/ (allinpacks - lastpacks);
								} else {
									indiscardserc = 0;
								}

							}
							portIPS.setDiscardsPerc(Double
									.toString(indiscardserc));

							// ��ڴ�����
							lastvalue = "";
							long lasterrors = 0;
							if (errorshash != null) {
								if (errorshash
										.get("AllInErrors" + ":" + sIndex) != null)
									lastvalue = errorshash.get(
											"AllInErrors" + ":" + sIndex)
											.toString();
							}
							// ȡ���ϴλ�õ�error
							if (lastvalue != null && !lastvalue.equals("")) {
								lasterrors = Long.parseLong(lastvalue);
							}

							double inerrorsperc = 0.0;
							if (allinpacks == 0) {
								inerrorsperc = 0;
							} else {
								if (allinpacks - lastpacks > 0) {
									inerrorsperc = (inerrors - lasterrors)
											/ (allinpacks - lastpacks);
								} else {
									inerrorsperc = 0;
								}

							}
							portIPS.setErrorsPerc(Double.toString(inerrorsperc));
//							if (!utilfalg.equals(""))
								utilhdxVector.addElement(portIPS);
							lastvalue = "";
							lastpacks = 0;
							lastdiscards = 0;
							lasterrors = 0;

							/* ��ӵ��ڴ��� */
							if (ShareData.getPacksdata(host.getIpAddress()
									+ ":" + sIndex) != null) {
								ShareData.getPacksdata(
										host.getIpAddress() + ":" + sIndex)
										.put("AllInCastPkts" + ":" + sIndex,
												allinpacks + "");
							} else {
								Hashtable hasht = new Hashtable();
								hasht.put("AllInCastPkts" + ":" + sIndex,
										allinpacks + "");
								ShareData.setPacksdata(host.getIpAddress()
										+ ":" + sIndex, hasht);
							}

							if (ShareData.getDiscardsdata(host.getIpAddress()
									+ ":" + sIndex) != null) {
								ShareData.getDiscardsdata(
										host.getIpAddress() + ":" + sIndex)
										.put("AllInDiscards" + ":" + sIndex,
												indiscards + "");
							} else {
								Hashtable tempDiscards = new Hashtable();
								tempDiscards.put(
										"AllInDiscards" + ":" + sIndex,
										indiscards + "");
								ShareData.setDiscardsdata(host.getIpAddress()
										+ ":" + sIndex, tempDiscards);
							}

							if (ShareData.getErrorsdata(host.getIpAddress()
									+ ":" + sIndex) != null) {
								ShareData.getErrorsdata(
										host.getIpAddress() + ":" + sIndex)
										.put("AllInErrors" + ":" + sIndex,
												inerrors + "");
							} else {
								Hashtable errHash = new Hashtable();
								errHash.put("AllInErrors" + ":" + sIndex,
										inerrors + "");
								ShareData.setErrorsdata(host.getIpAddress()
										+ ":" + sIndex, errHash);
							}

						}
					}
				}

				if (valueArray3 != null) {
					for (int i = 0; i < valueArray3.length; i++) {
						alloutpacks = 0;
						outupacks = 0;// ���ڵ���
						outnupacks = 0;// �ǵ���
						outdiscards = 0;
						outerrors = 0;

						String sIndex = (String) tempHash.get(i);
						if (tempV.contains(sIndex)) {
							Calendar cal = null;
							for (int j = 0; j < 5; j++) {
								if (valueArray3[i][j] != null) {
									String sValue = valueArray3[i][j];
									interfacedata = new Interfacecollectdata();
									interfacedata.setThevalue(sValue);
									if (j == 1 || j == 2) {
										// ���ڹ㲥���ݰ�,���ڶಥ���ݰ�
										if (sValue != null) {
											alloutpacks = alloutpacks
													+ Long.parseLong(sValue);
											cal = (Calendar) hash
													.get("collecttime");
											long timeInMillis = 0;
											if (cal != null)
												timeInMillis = cal
														.getTimeInMillis();
											long longinterval = (date
													.getTimeInMillis() - timeInMillis) / 1000;

											outpacks = new OutPkts();
											outpacks.setIpaddress(host
													.getIpAddress());
											outpacks.setCollecttime(sdf
													.format(date.getTime()));
											outpacks.setCategory("Interface");
											String chnameBand = "";
											if (j == 1) {
												outpacks.setEntity("ifHCOutMulticastPkts");
												chnameBand = "�ಥ";
											}
											if (j == 2) {
												outpacks.setEntity("ifHCOutBroadcastPkts");
												chnameBand = "�㲥";
											}
											outpacks.setSubentity(sIndex);
											outpacks.setRestype("dynamic");
											outpacks.setUnit("");
											outpacks.setChname(chnameBand);
											BigDecimal currentPacks = new BigDecimal(
													sValue);
											BigDecimal lastPacks = new BigDecimal(
													0);
											BigDecimal l = new BigDecimal(0);

											// �����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
											if (longinterval < 2 * interval) {
												String lastvalue = "";
												if (hash.get(desc1[j] + ":"
														+ sIndex) != null) {
													lastvalue = hash.get(
															desc1[j] + ":"
																	+ sIndex)
															.toString();
												} else {
													lastvalue = "0";
												}
												// ȡ���ϴλ�õ�Octets
												if (lastvalue != null
														&& !lastvalue
																.equals("")
														&& !lastvalue
																.equals("0")) {
													lastPacks = new BigDecimal(
															lastvalue);
												} else {
													lastPacks = new BigDecimal(
															"0");
												}
											}
											if (longinterval != 0) {

												BigDecimal packsBetween = currentPacks
														.subtract(lastPacks);
												l = packsBetween;
												if (lastPacks.equals("0")) {
													l = new BigDecimal("0");
												}
											}
											if (sValue == null) {
												outpacks.setThevalue("0");
											} else {
												outpacks.setThevalue(l
														.toString());
											}
											if (cal != null)
												outpacksVector
														.addElement(outpacks);
										}
										// continue;
									}
									if (j == 3) {
										// ���ڶ��������ݰ�
										if (sValue != null) {
											outdiscards = Long
													.parseLong(sValue);
										} else {
											outdiscards = 0;
										}

										continue;
									}
									if (j == 4) {
										// ���ڴ�������ݰ�
										if (sValue != null) {
											outerrors = Long.parseLong(sValue);
										} else {
											outerrors = 0;
										}

										continue;
									}

									// ����ÿ���˿����ټ�������
									if (j == 0) {
										long timeInMillis = 0;
										cal = (Calendar) hash
												.get("collecttime");
										if (cal != null)
											timeInMillis = cal
													.getTimeInMillis();
										long longinterval = (date
												.getTimeInMillis() - timeInMillis) / 1000;
										portIPS = new PortIPS();
										portIPS.setIpaddress(host
												.getIpAddress());
										portIPS.setCollecttime(sdf.format(date
												.getTime()));
										portIPS.setCategory("Interface");
										if (j == 0) {
											portIPS.setEntity("����");
										}
										portIPS.setSubentity(sIndex);
										portIPS.setRestype("dynamic");
										portIPS.setUtilhdxunit(unit1[1 + j]);
										BigDecimal currentHighOctets = new BigDecimal(
												sValue);
										BigDecimal lastHighOctets = new BigDecimal(
												0);
										double l = 0.00;
										double octets = 0.00;
										// �����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										String lastvalue = "";

										if (hash.get(desc1[5 + j] + ":"
												+ sIndex) != null)
											lastvalue = hash
													.get(desc1[5 + j] + ":"
															+ sIndex)
													.toString();
										// ȡ���ϴλ�õ�Octets
										if (lastvalue != null
												&& !lastvalue.equals("")
												&& !lastvalue.equals("0"))
											lastHighOctets = new BigDecimal(
													lastvalue);
										if (longinterval != 0) {
											double octetsHighBetween = 0.00;

											// ������-ǰ����
											octetsHighBetween = Long
													.valueOf(currentHighOctets
															.subtract(
																	lastHighOctets)
															.toString());
											octets = octetsHighBetween
													/ (1000 * 1000 * 8);
											if (octets < 0) {
												octets = 0;
											}
											// �ϴ�����
											double beforeOctets = 0.00;
											if (ShareData
													.getUtilhdxdata(desc1[5 + j]
															+ ":"
															+ host.getIpAddress()
															+ ":" + sIndex) != null) {
												beforeOctets = ShareData
														.getUtilhdxdata(desc1[5 + j]
																+ ":"
																+ host.getIpAddress()
																+ ":" + sIndex);
//												utilfalg = String
//														.valueOf(beforeOctets);
												// �ϴ���������Ϊ0
												if (beforeOctets != 0) {
													// ����10��Ϊ�쳣����
													if ((10 * beforeOctets) <= octets) {
														// �������ʶΪ�쳣����
														portIPS.setUtilhdxflag("1");
													} else {
														ShareData
																.setUtilhdxdata(
																		desc1[5 + j]
																				+ ":"
																				+ host.getIpAddress()
																				+ ":"
																				+ sIndex,
																		octets);
														portIPS.setUtilhdxflag("0");
													}
												} else {
													ShareData
															.setUtilhdxdata(
																	desc1[5 + j]
																			+ ":"
																			+ host.getIpAddress()
																			+ ":"
																			+ sIndex,
																	octets);
													portIPS.setUtilhdxflag("0");
												}

											} else {
												ShareData
														.setUtilhdxdata(
																desc1[5 + j]
																		+ ":"
																		+ host.getIpAddress()
																		+ ":"
																		+ sIndex,
																octets);
											}
											// ����˿�����
											l = octets / longinterval;
											// ͳ���ܳ����ֽ�������,���ü��㣨�����롢�ۺϣ�����������
											if (j == 0)
												allHighOutOctetsSpeed = allHighOutOctetsSpeed
														+ new Double(l)
																.longValue();
											allHighOctetsSpeed = allHighOctetsSpeed
													+ new Double(l).longValue();

										}
										DecimalFormat df = new DecimalFormat(
												"#.##");
										portIPS.setUtilhdx(df.format(octets));

										portIPS.setPercunit("%");
										double highSpeed = 0.0;
										if (hashHighSpeed.get(sIndex) != null) {
											highSpeed = Double
													.parseDouble(hashHighSpeed
															.get(sIndex)
															.toString());
										} else {
											highSpeed = Double
													.parseDouble("0.0");
										}
										portIPS.setIfSpeed(highSpeed);
										double d = 0.00;
										if (highSpeed > 0) {
											d = Arith.div(8 * l * 100,
													highSpeed);
										}
										portIPS.setUtilhdxPerc(Double
												.toString(d));
									}
									octetsHash.put(desc1[5 + j] + ":" + sIndex,
											interfacedata.getThevalue());
								}
							}
							packhash = ShareData.getPacksdata(host
									.getIpAddress() + ":" + sIndex);
							discardshash = ShareData.getDiscardsdata(host
									.getIpAddress() + ":" + sIndex);
							errorshash = ShareData.getErrorsdata(host
									.getIpAddress() + ":" + sIndex);

							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date
									.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutCastPkts");
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(alloutpacks + "");
							interfacedata.setChname("���������ݰ�");

							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date
									.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutDiscards");
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(outdiscards + "");
							interfacedata.setChname("�����ܶ�����");

							interfacedata = new Interfacecollectdata();
							interfacedata.setIpaddress(host.getIpAddress());
							interfacedata.setCollecttime(sdf.format(date
									.getTime()));
							interfacedata.setCategory("Interface");
							interfacedata.setEntity("AllOutErrors");
							interfacedata.setSubentity(sIndex);
							interfacedata.setRestype("static");
							interfacedata.setUnit("��");
							interfacedata.setThevalue(outerrors + "");
							interfacedata.setChname("���ڴ������");

							String lastvalue = "";
							long lastpacks = 0;

							Packs packs = new Packs();
							// ���ڶ�����
							lastvalue = "";
							long lastdiscards = 0;

							// ���ڴ�����
							lastvalue = "";
							long lasterrors = 0;

							lastvalue = "";
							lastpacks = 0;
							lastdiscards = 0;
							lasterrors = 0;
							// ���ڴ������ݰ�
							if (packhash != null) {
								if (packhash.get("AllOutCastPkts" + ":"
										+ sIndex) != null)
									lastvalue = packhash.get(
											"AllOutCastPkts" + ":" + sIndex)
											.toString();
							}
							// ȡ���ϴλ�õ�packs
							if (lastvalue != null && !lastvalue.equals("")
									&& !lastvalue.equals("0")) {
								lastpacks = Long.parseLong(lastvalue);
							}
							// ���㶪���ʺʹ�����
							if (discardshash != null) {
								if (discardshash.get("AllOutDiscards" + ":"
										+ sIndex) != null)
									lastvalue = discardshash.get(
											"AllOutDiscards" + ":" + sIndex)
											.toString();
							}
							// ȡ���ϴλ�õ�packs
							if (lastvalue != null && !lastvalue.equals("")) {
								lastdiscards = Long.parseLong(lastvalue);
							}
							double outdiscardserc = 0.0;
							if (alloutpacks == 0) {
								outdiscardserc = 0;
							} else {
								if (alloutpacks - lastpacks > 0) {
									outdiscardserc = (outdiscards - lastdiscards)
											/ (alloutpacks - lastpacks);
								} else {
									outdiscardserc = 0;
								}
							}

							portIPS.setDiscardsPerc(Double
									.toString(outdiscardserc));

							lastvalue = "";
							if (errorshash != null) {
								if (errorshash.get("AllOutErrors" + ":"
										+ sIndex) != null)
									lastvalue = errorshash.get(
											"AllOutErrors" + ":" + sIndex)
											.toString();
							}
							// ȡ���ϴλ�õ�packs
							if (lastvalue != null && !lastvalue.equals("")) {
								lasterrors = Long.parseLong(lastvalue);
							}
							double outerrorsperc = 0.0;
							if (alloutpacks > 0) {
								if ((alloutpacks - lastpacks) > 0) {
									outerrorsperc = (outerrors - lasterrors)
											/ (alloutpacks - lastpacks);
								} else {
									outerrorsperc = 0;
								}
							} else {
								outerrorsperc = 0;
							}
							portIPS.setErrorsPerc(Double
									.toString(outerrorsperc));

//							if (!utilfalg.equals(""))
								utilhdxVector.addElement(portIPS);

							/* ��ӵ��ڴ��� */
							if (ShareData.getPacksdata(host.getIpAddress()
									+ ":" + sIndex) != null) {
								ShareData.getPacksdata(
										host.getIpAddress() + ":" + sIndex)
										.put("AllOutCastPkts" + ":" + sIndex,
												alloutpacks + "");
							} else {
								Hashtable hasht = new Hashtable();
								hasht.put("AllOutCastPkts" + ":" + sIndex,
										alloutpacks + "");
								ShareData.setPacksdata(host.getIpAddress()
										+ ":" + sIndex, hasht);
							}

							if (ShareData.getDiscardsdata(host.getIpAddress()
									+ ":" + sIndex) != null) {
								ShareData.getDiscardsdata(
										host.getIpAddress() + ":" + sIndex)
										.put("AllOutDiscards" + ":" + sIndex,
												outdiscards + "");
							} else {
								Hashtable tempDiscards = new Hashtable();
								tempDiscards.put("AllOutDiscards" + ":"
										+ sIndex, outdiscards + "");
								ShareData.setDiscardsdata(host.getIpAddress()
										+ ":" + sIndex, tempDiscards);
							}

							if (ShareData.getErrorsdata(host.getIpAddress()
									+ ":" + sIndex) != null) {
								ShareData.getErrorsdata(
										host.getIpAddress() + ":" + sIndex)
										.put("AllOutErrors" + ":" + sIndex,
												outerrors + "");
							} else {
								Hashtable errHash = new Hashtable();
								errHash.put("AllOutErrors" + ":" + sIndex,
										outerrors + "");
								ShareData.setErrorsdata(host.getIpAddress()
										+ ":" + sIndex, errHash);
							}
						}
					}
				}

				String flag = "0";
				hashHighSpeed = null;
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.gc();
		}
		if (!(ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			Hashtable ipAllData = new Hashtable();
			if (ipAllData == null)
				ipAllData = new Hashtable();
			if (utilhdxVector != null && utilhdxVector.size() > 0)
				ipAllData.put("utilhdx", utilhdxVector);
			ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		} else {
			if (utilhdxVector != null && utilhdxVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress()))
						.put("utilhdx", utilhdxVector);
		}
		returnHash.put("utilhdx", utilhdxVector); // �ĳ� �˿�����
		outpacksVector = null;
		inpacksVector = null;
		utilhdxVector = null;
		NetinterfaceResultTosql tosql = new NetinterfaceResultTosql();
		logger.info("To SQL start");
		tosql.CreateResultTosql(returnHash, host.getIpAddress());
		return returnHash;
	}

	public int getInterval(float d, String t) {
		int interval = 0;
		if (t.equals("d"))
			interval = (int) d * 24 * 60 * 60; // ����
		else if (t.equals("h"))
			interval = (int) d * 60 * 60; // Сʱ
		else if (t.equals("m"))
			interval = (int) d * 60; // ����
		else if (t.equals("s"))
			interval = (int) d; // ��
		return interval;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return
	 */
	private Calendar getOperateTime() {
		Calendar opertatedate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		try {
			long initTime = System.currentTimeMillis() / 1000;
			if (initTime % (60 * 5) == 0) {
				opertatedate = Calendar.getInstance();
			} else {
				String curDate = DateUtil.getRightNow();
				String curDayAndHour = curDate.substring(0, 14);
				String curMin = curDate.substring(14, 16);
				String curHour = curDate.substring(11, 13);
				int cu = Integer.valueOf(curMin) / 5;
				int ho = Integer.valueOf(curHour);
				String inMin = "";
				if (cu == 11) {
					if (ho == 23) {
						curDayAndHour = DateUtil.getDateAddByDate(curDate, 1)
								.substring(0, 11) + "00:00:00";
					} else {
						if (ho < 9) {
							curDayAndHour = curDate.substring(0, 11) + "0"
									+ String.valueOf(ho + 1) + ":00:00";
						} else {
							curDayAndHour = curDate.substring(0, 11)
									+ String.valueOf(ho + 1) + ":00:00";
						}
					}
				} else {
					inMin = String.valueOf(((cu + 1) * 5));
					curDayAndHour = curDayAndHour + inMin + ":00";
				}
				Date date = dateFormat.parse(curDayAndHour);
				opertatedate = Calendar.getInstance();
				opertatedate.setTime(date);
			}
		} catch (Exception de) {
			de.printStackTrace();
		}

		return opertatedate;

	}

	public void run() {
		try {
			int numThreads = 200;

			// �����̳߳�
			ThreadPool threadPool = new ThreadPool(numThreads);
			// ��������
			Calendar date = getOperateTime();
			threadPool.runTask(createTask(node, date));
			// �ر��̳߳ز��ȴ������������
			threadPool.join();
		} catch (Exception e) {
			SysLogger.info("error in ExecutePing!" + e.getMessage());
		}

	}

	/**
	 * ��������
	 */
	private static Runnable createTask(final HostNode hostnode,
			final Calendar date) {
		return new Runnable() {
			public void run() {
				try {
					new InterfaceTaskHC().collect_Data(hostnode, date);
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}
		};
	}

	private static String dateIPS;

	public static void main(String[] args) throws Exception {
		InterfaceTaskHC init = new InterfaceTaskHC();
		init.run();
	}
}

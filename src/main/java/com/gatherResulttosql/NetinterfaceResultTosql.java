package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.om.Interfacecollectdata;
import com.gatherdb.GathersqlListManager;
import com.afunms.polling.om.*;

public class NetinterfaceResultTosql {
	/**
	 * 
	 * 把采集数据生成sql放入的内存列表中
	 */
	public void CreateResultTosql(Hashtable ipdata, String ip) {
		if(ipdata.containsKey("utilhdx")){
			String allipstr = SysUtil.doip(ip);
			// 端口流量数据
			Vector portipsVector = (Vector) ipdata.get("utilhdx");
			if (portipsVector != null && portipsVector.size() > 0) {
				String tablename = "portips" + allipstr;
				PortIPS portips = null;
				for (int si = 0; si < portipsVector.size(); si++) {
					portips = (PortIPS) portipsVector.elementAt(si);
					if (portips.getRestype().equals("dynamic")) {
						StringBuffer sBuffer = new StringBuffer();
						sBuffer.append("insert into ");
						sBuffer.append(tablename);
						sBuffer.append("(ipaddress,restype,category,entity,subentity,utilhdx,utilhdxperc,discardsperc,errorsperc,utilhdxunit,percunit,utilhdxflag,ifspeed,collecttime) ");
						sBuffer.append("values('");
						sBuffer.append(ip);
						sBuffer.append("','");
						sBuffer.append(portips.getRestype());
						sBuffer.append("','");
						sBuffer.append(portips.getCategory());
						sBuffer.append("','");
						sBuffer.append(portips.getEntity());
						sBuffer.append("','");
						sBuffer.append(portips.getSubentity());
						sBuffer.append("','");
						sBuffer.append(portips.getUtilhdx());
						sBuffer.append("','");
						sBuffer.append(portips.getUtilhdxPerc());
						sBuffer.append("','");
						sBuffer.append(portips.getDiscardsPerc());
						sBuffer.append("','");
						sBuffer.append(portips.getErrorsPerc());
						sBuffer.append("','");
						sBuffer.append(portips.getUtilhdxunit());
						sBuffer.append("','");
						sBuffer.append(portips.getPercunit());
						sBuffer.append("','");
						sBuffer.append(portips.getUtilhdxflag());
						sBuffer.append("','");
						sBuffer.append(portips.getIfSpeed());
						if ("mysql".equalsIgnoreCase(SystemConstant.DBType)) {
							sBuffer.append("','");
							sBuffer.append(portips.getCollecttime());
							sBuffer.append("')");
						} else if ("oracle"
								.equalsIgnoreCase(SystemConstant.DBType)) {
							sBuffer.append("',");
							sBuffer.append("to_date('"
									+ portips.getCollecttime()
									+ "','YYYY-MM-DD HH24:MI:SS')");
							sBuffer.append(")");
						}
						GathersqlListManager.Addsql(sBuffer.toString());

						portips = null;
						sBuffer = null;
					}
				}
			}
			portipsVector = null;
		}
	}
}

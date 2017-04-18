package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.om.Interfacecollectdata;
import com.gatherdb.GathersqlListManager;
import com.afunms.polling.om.*;

public class NetinterfaceResultTosql {
	private Log logger = LogFactory.getLog(NetinterfaceResultTosql.class);
	/**
	 * 
	 * �Ѳɼ���������sql������ڴ��б���
	 */
	public void CreateResultTosql(Hashtable ipdata, String ip) {
		if(ipdata.containsKey("utilhdx")){
			
			Vector portipsVector = (Vector) ipdata.get("utilhdx");
			if (portipsVector != null && portipsVector.size() > 0) {
				String tablename = "portips";
		
				for (int si = 0; si < portipsVector.size(); si++) {
					PortIPS portips = (PortIPS) portipsVector.elementAt(si);
					StringBuffer sBuffer = new StringBuffer();

					
					sBuffer.append("upsert into ")
					.append(tablename)
					.append("(ip_address,restype,category,entity,subentity,utilhdx,utilhdx_perc,discards_perc,errors_perc,utilhdx_unit,perc_unit,utilhdx_flag,ifspeed) ")
					.append("values('")
					.append(ip)
					.append("','")
					.append(portips.getRestype())
					.append("','")
					.append(portips.getCategory())
					.append("','")
					.append(portips.getEntity())
					.append("','")
					.append(portips.getSubentity())
					.append("','")
					.append(portips.getUtilhdx())
					.append("','")
					.append(portips.getUtilhdxPerc())
					.append("','")
					.append(portips.getDiscardsPerc())
					.append("','")
					.append(portips.getErrorsPerc())
					.append("','")
					.append(portips.getUtilhdxunit())
					.append("','")
					.append(portips.getPercunit())
					.append("','")
					.append(portips.getUtilhdxflag())
					.append("','")
					.append(portips.getIfSpeed())
					.append("')");
					
						GathersqlListManager.Addsql(sBuffer.toString());
						logger.info(sBuffer);
					}
				}
			}
		}
}

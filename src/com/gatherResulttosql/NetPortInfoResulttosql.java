package com.gatherResulttosql;

import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.om.Portinfocollectdata;
import com.afunms.topology.dao.HostNodeDao;
import com.gatherdb.GathersqlListManager;

public class NetPortInfoResulttosql {
	public void CreateResultTosql(Hashtable ipdata,String ip)
	{
		System.out.println("==========NetPortInfoResulttosql======1======");
		if(ipdata.containsKey("portinfo")){
			System.out.println("==========NetPortInfoResulttosql======2======");
			Vector portinfoVector = (Vector) ipdata.get("portinfo");	
			System.out.println("==========portinfoVector======3======"+portinfoVector.size());
			if (portinfoVector != null && portinfoVector.size() > 0) {
				try{
					Portinfocollectdata portinfodata=null;
					System.out.println("==========NetPortInfoResulttosql======3======");
					for(int i=0;i<portinfoVector.size();i++){
						portinfodata = (Portinfocollectdata) portinfoVector.elementAt(i);
						String timedate=portinfodata.getCollecttime();
						if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
							timedate=timedate;
						}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
							timedate="to_date('"+timedate+"','YYYY-MM-DD HH24:MI:SS')";
						}
						String tablename = "topo_interface";
						String sql = "insert into " + tablename
						+ "(node_id,port,ip_address,collecttime) "
						+ "values('" + ip + "','" + portinfodata.getPort() + "','" + portinfodata.getIpAddress() + "',"
						+"" + timedate + ")";
						
						
						GathersqlListManager.Addsql(sql);
						System.out.println("==========NetPortInfoResulttosql======4======");
						sql=null;
						
						portinfodata = null;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				
				
			}
			portinfoVector = null;
		}
		
	
	}
}

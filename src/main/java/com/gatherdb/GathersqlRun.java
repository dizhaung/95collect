package com.gatherdb;


import java.util.TimerTask;
import org.apache.log4j.Logger;
import com.gatherdb.GathersqlListManager;






/**
 * 
 * 
 * ��ʱ�Ѷ���������ݲ������ݿ�
 * @author Administrator
 *
 */
public class GathersqlRun extends TimerTask{
	
 
	public  Logger logger = Logger.getLogger(GathersqlRun.class);
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		if(!GathersqlListManager.idbstatus)
		{
		   GathersqlListManager.Addsql("DHCC-DB");
		}
	}

}

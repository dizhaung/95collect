package com.afunms.common.util;

import java.util.Hashtable;

public class ShareDataLsf {
//	public static String collecttime = "";    //lsf�ɼ�ʱ��
	private static Hashtable collecttime_table = new Hashtable();  // lsf�ɼ�ʱ��
	private static Hashtable hashLsf = new Hashtable();
	private static Hashtable masterlist=new Hashtable();//������¼�������Ƿ���master
	
	
	private static Hashtable alarmlist=new Hashtable();//������¼�������Ƿ���master
	
	
	
	public static Hashtable getAlarmlist() {
		return alarmlist;
	}

	public static void setAlarmlist(Hashtable alarmlist) {
		ShareDataLsf.alarmlist = alarmlist;
	}

	public static Hashtable getMasterlist() {
		return masterlist;
	}

	public static void setMasterlist(Hashtable masterlist) {
		ShareDataLsf.masterlist = masterlist;
	}

	public static Hashtable getCollecttime_table() {
		return collecttime_table;
	}

	public static void setCollecttime_table(Hashtable collecttime_table) {
		ShareDataLsf.collecttime_table = collecttime_table;
	}

	public static Hashtable getHashLsf() {
		return hashLsf;
	}

	public static void setHashLsf(Hashtable hashLsf) {
		ShareDataLsf.hashLsf = hashLsf;
	}

}

package com.afunms.common.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;

/**
 * @author Administrator
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CreateTableManager {

	public void createTable(DBManager conn, String tablename, String ipstr,
			String tablestr) {
		try {
			createRootTable(conn, tablename, ipstr);
		} catch (Exception ex) {
		} finally {
		}
	}

	public void createGrapesTable(DBManager conn, String tablename, String ipstr) {
		try {
			createRootTable(conn, tablename, ipstr);
		} catch (Exception ex) {
			conn.rollback();
		} finally {
			conn.close();
		}
	}

	public void deleteTable(DBManager conn, String tablename, String ipstr,
			String tablestr) {
		try {
			dropRootTable(conn, tablename, ipstr);
		} catch (Exception ex) {
		} finally {
		}
	}
	
	public void createRootTable(DBManager conn, String tablename, String ipstr) {
		String sql = "";
		if(SystemConstant.getDBType().equals("oracle")){
//			if (tablename.indexOf("hour")>=0){
//		 		//创建天的表
//			 	sql="create table "+tablename+ipstr
//			 	+"(ID number(20) not null ,IPADDRESS VARCHAR2(30),RESTYPE VARCHAR2(20),CATEGORY VARCHAR2(50),ENTITY VARCHAR2(100),SUBENTITY VARCHAR2(60),"
//			 	+"THEVALUE VARCHAR2(100),COLLECTTIME date default sysdate-1,UNIT VARCHAR2(30),"
//			 	+" PRIMARY KEY  (ID)) ";
//		 	} 
//			else 
//		 		if (tablename.indexOf("portipsday") >= 0) {
//				// 创建天的表
//		 		sql="create table "+tablename+ipstr
//			 	+"(ID number(20) not null ,IPADDRESS VARCHAR2(30),RESTYPE VARCHAR2(20),CATEGORY VARCHAR2(50),ENTITY VARCHAR2(100),SUBENTITY VARCHAR2(60),"
//			 	+"UTILHDX VARCHAR2(100),UTILHDXPERC VARCHAR2(30),DISCARDSPERC VARCHAR2(30),ERRORSPERC VARCHAR2(30),COLLECTTIME date default sysdate-1,UTILHDXUNIT VARCHAR2(30),PERCUNIT VARCHAR2(30),utilhdxflag varchar2(5),RECOVER VARCHAR2(100),"
//			 	+" PRIMARY KEY  (ID)) ";
		 		if (tablename.indexOf("portipshour") >= 0) {
					// 创建天的表
			 		sql="create table "+tablename+ipstr
				 	+"(ID number(20) not null ,IPADDRESS VARCHAR2(30),RESTYPE VARCHAR2(20),CATEGORY VARCHAR2(50),ENTITY VARCHAR2(100),SUBENTITY VARCHAR2(60),"
				 	+"UTILHDX VARCHAR2(100),UTILHDXPERC VARCHAR2(30),DISCARDSPERC VARCHAR2(30),ERRORSPERC VARCHAR2(30),COLLECTTIME date default sysdate-1,UTILHDXUNIT VARCHAR2(30),PERCUNIT VARCHAR2(30),utilhdxflag varchar2(5),RECOVER VARCHAR2(100),"
				 	+" PRIMARY KEY  (ID)) ";
			} 
//		 		else if (tablename.indexOf("portips") >= 0) {
//				// 创建PORTIPS的表
//		 		sql="create table "+tablename+ipstr
//			 	+"(ID number(20) not null ,IPADDRESS VARCHAR2(30),RESTYPE VARCHAR2(20),CATEGORY VARCHAR2(50),ENTITY VARCHAR2(100),SUBENTITY VARCHAR2(60),"
//			 	+"UTILHDX VARCHAR2(100),UTILHDXPERC VARCHAR2(30),DISCARDSPERC VARCHAR2(30),ERRORSPERC VARCHAR2(30),COLLECTTIME date,UTILHDXUNIT VARCHAR2(30),PERCUNIT VARCHAR2(30),utilhdxflag varchar2(5),RECOVER VARCHAR2(100),"
//			 	+" PRIMARY KEY  (ID)) ";
//			}
//			else{
//		 		//创建按分钟采集数据的表
//			 	sql="create table "+tablename+ipstr
//			 	+"(ID number(20) not null ,IPADDRESS VARCHAR2(30),RESTYPE VARCHAR2(20),CATEGORY VARCHAR2(50),ENTITY VARCHAR2(100),SUBENTITY VARCHAR2(60),"
//			 	+"THEVALUE VARCHAR2(100),COLLECTTIME date,"
//			 	+" PRIMARY KEY  (ID)) ";
//		 	}
		}
		
		SysLogger.info(sql);
		try {
			conn.executeUpdate(sql);
			if(SystemConstant.DBType.equals("oracle")){
				CreateTableManager.createSeqOrcl(conn, tablename, ipstr);
				CreateTableManager.createTrigerOrcl(conn, tablename, ipstr, tablename);
			}
		} catch (Exception e) {
			try {
				// conn.rollback();
			} catch (Exception ex) {

			}

		} finally {
		}
	}

	public void dropRootTable(DBManager conn, String tablename, String ipstr) {
		String sql = "";
		sql = "drop table if exists " + tablename + ipstr;
		try {
			conn.executeUpdate(sql);
		} catch (Exception e) {

			try {
				// conn.rollback();
			} catch (Exception ex) {

			}

		} finally {
			// conn.commit();
		}
	}

	public void createIndex(Connection con, String tname, String ipstr,
			String indexsub, String tablename, String fieldname) {
		PreparedStatement stmt = null;
		String indexstr = "";
		indexstr = "create index " + tname + ipstr + indexsub + " on "
				+ tablename + ipstr + " (" + fieldname
				+ ") tablespace DHCC_ITTABSPACE";
		System.out.println(indexstr);
		try {
			stmt = con.prepareStatement(indexstr);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				con.rollback();
				// DataGate.freeCon(con);
			} catch (Exception ex) {

			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}
	}

	public void createSeq(Connection con, String tablestr, String ipstr) {
		PreparedStatement stmt = null;
		String createSeqStr = "";
		createSeqStr = "create sequence "
				+ tablestr
				+ "_"
				+ ipstr
				+ "SEQ minvalue 1 maxvalue 999999999999999999999999999 start with 5413921 increment by 1 cache 10";
		System.out.println(createSeqStr);
		try {
			stmt = con.prepareStatement(createSeqStr);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// con.rollback();
				// DataGate.freeCon(con);
			} catch (Exception ex) {

			}

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}

	}

	public void dropSeq(Connection con, String tablestr, String ipstr) {
		PreparedStatement stmt = null;
		String createSeqStr = "";
		createSeqStr = "drop sequence " + tablestr + "_" + ipstr + "SEQ";
		System.out.println(createSeqStr);
		try {
			stmt = con.prepareStatement(createSeqStr);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// con.rollback();
				// DataGate.freeCon(con);
			} catch (Exception ex) {

			}

		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}

	}

	public void createTriger(Connection con, String tablestr, String ipstr,
			String tablename) {
		PreparedStatement stmt = null;
		String trigerstr = "";
		trigerstr = "create or replace trigger " + tablestr + ipstr
				+ "id before insert on " + tablename + ipstr
				+ " for each row when (new.id is null) begin " + " select "
				+ tablestr + "_" + ipstr
				+ "SEQ.nextval into :new.id from dual; end;";
		System.out.println(trigerstr);
		try {
			stmt = con.prepareStatement(trigerstr);
			stmt.execute();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// con.rollback();
				// DataGate.freeCon(con);
			} catch (Exception ex) {

			}
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {

			}
		}
	}

	public void dropDbconfigInfo(DBManager conn, String tablename, String ipstr) {
		PreparedStatement stmt = null;
		String sql = "";
		sql = "delete from " + tablename + " where ipaddress = '" + ipstr + "'";
		try {
			conn.executeUpdate(sql);
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {

			}

		} finally {
		}
	}

	/**
	 * 清空多张指定表的数据
	 * 
	 * @param tableNames
	 *            表名称
	 * @param uniqueKey
	 *            唯一键 如：nodeid
	 * @param nodeids
	 *            唯一键对应的值 如：结点ID数组
	 * @return
	 */
	public Boolean clearTablesData(String[] tableNames, String uniqueKey,
			String[] uniqueKeyValues) {
		DBManager dbmanager = new DBManager();
		try {
			for (String uniqueValue : uniqueKeyValues) {
				for (String tableName : tableNames) {
					String sql = "delete from " + tableName + " where "
							+ uniqueKey + " = '" + uniqueValue + "'";
					dbmanager.addBatch(sql);
				}
			}
			// //System.out.println(sql);
			dbmanager.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			dbmanager.close();
		}
		return true;
	}

	public static void main(String[] args) {
		CreateTableManager ctable = new CreateTableManager();
		DBManager conn = new com.afunms.common.util.DBManager();
		HostNodeDao dao =new HostNodeDao();
		List list = null;
		list=dao.loadIsMonitoredNode();
		for(int i=0;i<list.size();i++){
			HostNode node = (HostNode) list.get(i);
			String ip = node.getIpAddress();
			//String ip = "183.203.0.65";
			String vip = SysUtil.doip(ip);
//			ctable.createRootTable(conn,"ping",vip);//ping
//			ctable.createRootTable(conn,"pingday",vip);// 天 ping
			
			ctable.createRootTable(conn,"portipshour",vip);//  端口流量
//			ctable.createRootTable(conn,"portipsday",vip);//端口流量 、天
				
//			ctable.createRootTable(conn,"portstatus",vip);//  端口状态
//			ctable.createRootTable(conn,"portstatusday",vip);//端口状态 、天
			conn.executeBatch();
		}
		conn.commit();
		conn.close();
		
		
		
	}
	/**
	 * 新加的方法，Oracle实现主键自增长的方法
	 * @param con
	 * @param tablestr
	 * @param ipstr
	 */
	public static void createSeqOrcl(DBManager conn,String tablestr,String ipstr){
	 	String createSeqStr="";
	 	createSeqStr="create sequence "+tablestr+"_"+ipstr+"_SEQ minvalue 1 maxvalue 999999999999999999999999999 start with 1 increment by 1 cache 10";
	 	try{
	 		conn.executeUpdate(createSeqStr);
	 	}catch(Exception e){
	 		e.printStackTrace();
	 		try{		 			
	 			//con.rollback();
	 			//DataGate.freeCon(con);		 			
	 		}catch(Exception ex){
	 			
	 		}
	 		
	 	}finally{
	 		
	 	}
	 }
	/**
	 * 创建Oracle的触发器
	 * @param con
	 * @param tablestr
	 * @param ipstr
	 * @param tablename
	 */

	public static void createTrigerOrcl(DBManager conn,String tablestr,String ipstr,String tablename){
	 	PreparedStatement stmt = null;
	 	String trigerstr="";
	 	trigerstr="create or replace trigger "+tablestr+ipstr+"id before insert on "+tablestr+ipstr+" for each row when (new.id is null) begin "
				+" select "+tablestr+"_"+ipstr+"_SEQ.nextval into :new.id from dual; end ;";
	 	try{
	 		conn.executeUpdate(trigerstr);
	 	}catch(Exception e){
	 		e.printStackTrace();
	 		try{		 			
	 			//con.rollback();
	 			//DataGate.freeCon(con);		 			
	 		}catch(Exception ex){
	 			
	 		}		 		
	 	}finally{
	 		try{
	 			if (stmt != null)stmt.close();
	 		}catch(Exception e){
	 			
	 		}
	 	}		 	
  }

}

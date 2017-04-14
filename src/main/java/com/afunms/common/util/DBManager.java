/**
 * <p>Description:DBManager,used to connect database</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.util;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.sql.DataSource;

import com.afunms.initialize.ResourceCenter;
import com.afunms.util.DataGate;
import com.database.DBConnectionManager;

public class DBManager
{
	protected Connection conn;
	protected Statement stmt;
	protected PreparedStatement pstmt;
	protected ResultSet rs;
	protected String preparesql ;
   
   /**
    * ����Ĭ��ϵͳ���ݿ��ǿ����������ӵģ����Բ��׳�����
    */
	
   public Connection getConn (){
	   return conn;
   }
   public DBManager() 
   {
	   try
	   {
           init(ResourceCenter.getInstance().getJndi());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace();
		   SysLogger.error("Can not connect system DB!",e);
	   }
   }

   public DBManager(String jndi) throws Exception
   {
       init(jndi);       
   }
   
   /**
    * ��ʼ��    
    */
   public void init(String jndi)
   {
// 	   Context initCtx = null;
 	  
// 	   DataSource ds = null;
 	   try{
 		  conn=DataGate.getCon();
 		  
         
// 	 	  initCtx = new InitialContext();
// 	 	   if("tomcat".equals(ResourceCenter.getInstance().getAppServer()))
// 	 	      ds = (DataSource)initCtx.lookup("java:comp/env/" + jndi); //tomcat
// 	 	   else
// 	 	      ds = (DataSource)initCtx.lookup(jndi);  //weblogic
// 	       conn = ds.getConnection();      
 		   
 	       conn.setAutoCommit(false);
 	       stmt = conn.createStatement();
 	       //pstmt = conn.prepareStatement(sql);
 	       
 	        
 	   }catch(Exception e){
 		  e.printStackTrace(); 
 	   }finally{
// 		   try{
// 			   initCtx.close();
// 		   }catch(Exception ex){
// 			   ex.printStackTrace();
// 		   }
 	   }

   }
   
 

   /**
    * ��ѯ    
    */
   public ResultSet executeQuery(String sql)
   {
       try
       {
    	   rs = stmt.executeQuery(sql);
       }
       catch(SQLException se)
       {
    	   se.printStackTrace();
           SysLogger.error("Error in DBManager.executeQuery(),SQL=\n" + sql);
       }
       return rs;
   }

   /**
    * ����,���ύ
    */
   public void executeUpdate(String sql)
   {
	   //SysLogger.info("executeUpdate(sql)==="+sql);
       executeUpdate(sql, true);
   }
   
   /**
    * ����
    */
   public void executeUpdate(String sql, boolean bCommit)
   {
	   
	   
	   //SysLogger.info("executeUpdate(sql,boolean)==="+sql);
      try
      {
//    	  if(stmt == null){
//    		   try
//    		   {
//    	           init(ResourceCenter.getInstance().getJndi());
//    		   }
//    		   catch(Exception e)
//    		   {
//    			   e.printStackTrace();
//    			   SysLogger.error("Can not connect system DB!",e);
//    		   }
//    	  }
          try{
        	  stmt.executeUpdate(sql);
          }
		   catch(Exception e)
		   {
			   e.printStackTrace();
			   //SysLogger.error("Can not connect system DB!",e);
		   }
          if(bCommit)
             conn.commit();
      }
      catch(SQLException se)
      {
    	  se.printStackTrace();
         try
         {
            conn.rollback();
         }
         catch(SQLException sqle)
         {}
         SysLogger.error("DBManager.executeUpdate():�������ݳ���:\n" + sql,se);
      }
   }
   
   /**
    * �ύ
    */
   public void commit()
   {
      try
      {
         conn.commit();
      }
      catch(SQLException se)
      {
         SysLogger.error("DBManager.commit():",se);         
      }
   }

  /**
   * �ع� 
   */
   public void rollback()
   {
      try
      {
          conn.rollback();
      }
      catch(SQLException se)
      {
    	  SysLogger.error("DBManager.rollback():",se);	
      }
   }

   /**
    * ����������    
    */
   public void addBatch(String sql)
   {
      try
      {
    	  stmt.addBatch(sql);
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" + sql,se);
      }
    }

   /**
    * ִ��������
    */
   public void executeBatch()
   {      
      try
      {
          stmt.executeBatch();         
          conn.commit();         
      }
      catch(BatchUpdateException bse)
      {
    	  bse.printStackTrace();
          SysLogger.error("-------������һ��SQL������-------",bse); 
      }      
      catch(SQLException se)
      {
    	  se.printStackTrace();
          SysLogger.error("Error in DBManager.executeBatch()!",se);
      }      
      catch(Exception se)
      {
    	  se.printStackTrace();
          SysLogger.error("Error in DBManager.executeBatch()!",se);
      }
      finally
      {
    	  try
    	  {
    	     stmt.clearBatch();
    	  }
    	  catch(SQLException xe)
    	  {
    		  xe.printStackTrace();
    	  }
      }
   }
   
   /**
    * ����������SQL    
    */
   public void setPrepareSql(String sql)
   {
	   preparesql = sql;
	   try{
		   pstmt = conn.prepareStatement(preparesql);
	   }
	   catch(SQLException xe)
 	  {
		   xe.printStackTrace();
 	  }
    }
   
   /**
    * ����������    
    */
   public void addPrepareBatch(List list)
   {
      try
      {
    	  pstmt.setString(1, (String)list.get(0));
    	  pstmt.setString(2, (String)list.get(1));
    	  pstmt.setString(3, (String)list.get(2));
    	  pstmt.setString(4, (String)list.get(3));
    	  pstmt.setString(5, (String)list.get(4));
    	  pstmt.setString(6, (String)list.get(5));
    	  pstmt.setString(7, (String)list.get(6));
    	  pstmt.setString(8, (String)list.get(7));
    	  pstmt.setString(9, (String)list.get(8));
    	  pstmt.setString(10, (String)list.get(9));
    	  pstmt.setString(11, (String)list.get(10));
    	  pstmt.setString(12, (String)list.get(11));
    	  pstmt.setString(13, (String)list.get(12));
    	  pstmt.addBatch();
    	  //SysLogger.info((String)list.get(4)+"====="+(String)list.get(5)+"==="+(String)list.get(6));
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" ,se);
      }
    }
   
   /**
    * ����������    
    */
   public void addPrepareProcBatch(List list)
   {
      try
      {
    	  pstmt.setString(1, (String)list.get(0));
    	  pstmt.setString(2, (String)list.get(1));
    	  pstmt.setString(3, (String)list.get(2));
    	  pstmt.setString(4, (String)list.get(3));
    	  pstmt.setString(5, (String)list.get(4));
    	  pstmt.setString(6, (String)list.get(5));
    	  pstmt.setString(7, (String)list.get(6));
    	  pstmt.setString(8, (String)list.get(7));
    	  pstmt.setString(9, (String)list.get(8));
    	  pstmt.setString(10, (String)list.get(9));
    	  pstmt.setString(11, (String)list.get(10));
    	  pstmt.addBatch();
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" ,se);
      }
    }
   
   /**
    * ����������    
    */
   public void addPrepareProcLongBatch(List list)
   {
      try
      {
    	  pstmt.setString(1, (String)list.get(0));
    	  pstmt.setString(2, (String)list.get(1));
    	  pstmt.setString(3, (String)list.get(2));
    	  pstmt.setString(4, (String)list.get(3));
    	  pstmt.setString(5, (String)list.get(4));
    	  pstmt.setString(6, (String)list.get(5));
    	  pstmt.setString(7, (String)list.get(6));
    	  pstmt.setString(8, (String)list.get(7));
    	  pstmt.setLong(9, (Long)list.get(8));
    	  pstmt.setString(10, (String)list.get(9));
    	  pstmt.setString(11, (String)list.get(10));
    	  pstmt.addBatch();
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" ,se);
      }
    }
   
   /**
    * ����������    
    */
   public void addPrepareSoftwareBatch(List list)
   {
      try
      {
    	  pstmt.setString(1, (String)list.get(0));
    	  pstmt.setString(2, (String)list.get(1));
    	  pstmt.setString(3, (String)list.get(2));
    	  pstmt.setString(4, (String)list.get(3));
    	  pstmt.setString(5, (String)list.get(4));
    	  pstmt.setString(6, (String)list.get(5));
    	  pstmt.setString(7, (String)list.get(6));
    	  pstmt.setString(8, (String)list.get(7));
    	  pstmt.setString(9, (String)list.get(8));
    	  pstmt.addBatch();
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" ,se);
      }
    }
   
   /**
    * ����������    
    */
   public void addPrepareServiceBatch(List list)
   {
      try
      {
    	  pstmt.setString(1, (String)list.get(0));
    	  pstmt.setString(2, (String)list.get(1));
    	  pstmt.setString(3, (String)list.get(2));
    	  pstmt.setString(4, (String)list.get(3));
    	  pstmt.setString(5, (String)list.get(4));
    	  pstmt.setString(6, (String)list.get(5));
    	  pstmt.setString(7, (String)list.get(6));
    	  pstmt.setString(8, (String)list.get(7));
    	  pstmt.setString(9, (String)list.get(8));
    	  pstmt.setString(10, (String)list.get(9));
    	  pstmt.addBatch();
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" ,se);
      }
    }
   
   /**
    * ����������    
    */
   public void addPrepareErrptBatch(List list)
   {
      try
      {
    	  pstmt.setString(1, (String)list.get(0));
    	  pstmt.setString(2, (String)list.get(1));
    	  pstmt.setString(3, (String)list.get(2));
    	  pstmt.setInt(4, (Integer)list.get(3));
    	  pstmt.setString(5, (String)list.get(4));
    	  pstmt.setString(6, (String)list.get(5));
    	  pstmt.setString(7, (String)list.get(6));
    	  pstmt.setString(8, (String)list.get(7));
    	  pstmt.setString(9, (String)list.get(8));
    	  pstmt.setString(10, (String)list.get(9));
    	  pstmt.setString(11, (String)list.get(10));
    	  pstmt.setString(12, (String)list.get(11));
    	  pstmt.setString(13, (String)list.get(12));
    	  pstmt.setString(14, (String)list.get(13));
    	  pstmt.setString(15, (String)list.get(14));
    	  pstmt.addBatch();
      }
      catch(SQLException se)
      {
          SysLogger.error("Error in DBManager.addBatch()!" ,se);
      }
    }
   
   /**
    * ִ��������
    */
   public void executePreparedBatch()
   {      
      try
      {
    	  pstmt.executeBatch();         
          conn.commit();         
      }
      catch(BatchUpdateException bse)
      {
    	  bse.printStackTrace();
          SysLogger.error("-------������һ��SQL������-------",bse); 
      }      
      catch(SQLException se)
      {
    	  se.printStackTrace();
          SysLogger.error("Error in DBManager.executeBatch()!",se);
      }      
      catch(Exception se)
      {
    	  se.printStackTrace();
          SysLogger.error("Error in DBManager.executeBatch()!",se);
      }
      finally
      {
    	  try
    	  {
    		  pstmt.clearBatch();
    	  }
    	  catch(SQLException xe)
    	  {}
      }
   }
   
   /**
    * �ر�����
    */
   public void close(){
      try{
         if(rs!= null)
            rs.close();
         if(stmt!=null)
            stmt.close();
         try{
				conn.commit();
				stmt.close();
				//DataGate.freeCon(conn);
				}catch(Exception ex){
					
				}
         if(conn!=null && !conn.isClosed()){
         //if(conn!=null){
            conn.close();
         }
      }catch(SQLException se){
    	  se.printStackTrace();
         SysLogger.error("Error in DBManager.close()!",se);         
      }finally{
    	  rs = null;
    	  stmt = null;
    	  conn = null;
      }
   }
   
   
   /**
	 * 
	 * ͨ�� onekye��twokey��ϳ�һ��Ψһ��key���������ݿ�Ĳ�ѯ���
	 * �ŵ�hashtable ��;onekey-towkey Ϊ����
	 * @author konglq
	 * @����ʱ�� 2010-8-24 ����12:58:26
	 * @������  executeQueryListHashMap
	 * @param sql
	 * @param onekey ����һ����¼��Ψһkey
	 * @param twokey �ڶ���key
	 * @return
	 * @throws SQLException List
	 * @�������� ִ�����ݿ��ѯ��䷵���������͵�����,������ÿһ��ֵ�ǹ�ϣ�� 
	 * ÿ����ϣ�����һ����¼
	 */
	public Hashtable executeQuerykeytwoListHashMap(String sql, String onekey,
			String twokey) throws SQLException {
		ResultSetMetaData rsmd = null;
		Hashtable list = new Hashtable();
		int columnCount = 0;
		try {
			//logger.info("ִ��sql���£�" + sql);
			rs = executeQuery(sql);
			if (rs == null) {
				return null;
			}
			rsmd = rs.getMetaData();
			if (rsmd == null) {
				return null;
			}
			columnCount = rsmd.getColumnCount(); // �õ��ֶ�����
			if (columnCount == 0) {
				return null;
			}

			// �����ĵ�һ��ֵ����������
			String[] keys = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				keys[i - 1] = rsmd.getColumnName(i); // ����ֶ���
			}
			// vct.add(keys);
			while (rs.next()) {
				Hashtable hm = new Hashtable();
				hm.clear();
				String key1 = null;
				String key2=null;
				for (int i = 1; i <= columnCount; i++) {
					String result = rs.getString(i);
					if ((result == null) || (result.length() == 0)) {
						result = "";
					}

					if (keys[i - 1].equals(onekey)) {
						key1 = result;
					}
					
					if (keys[i - 1].equals(twokey)) {
						key2 = result;
					}

					hm.put(keys[i - 1], result); // ��ÿ����¼���浽һ����ϣ���У�keyΪ�ֶ�����resultΪֵ
				}
				list.put(key1+"-"+key2, hm); // �����ݼ���ÿһ�в�������
			}
		} catch (SQLException e) {
			//logger.error("ִ��sql�������⣺" + sql + e);
			return null;
		}
		
		return list; // ����SQL���ԵĲ�ѯ�������
	}

   
}

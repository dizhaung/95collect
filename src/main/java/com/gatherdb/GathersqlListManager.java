package com.gatherdb;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.database.DBManager;

import java.util.Hashtable;
import java.util.Vector;
/**
 * 
 * �ɼ�����sql�ڴ����ݹ�������б��뷽��
 * @author konglq
 *
 */
public class GathersqlListManager {
	
	public static  Queue<String> queue = new LinkedList<String>();//��ʱ�������
	public static  Queue<String> queue2 = new LinkedList<String>();//��ʱ�������
	public static  Hashtable<String,Vector> datatemplist = new Hashtable();
	public static  Hashtable<String,Vector> datatemplist2 = new Hashtable();
	public static Logger logger = Logger.getLogger(GathersqlListManager.class);
	public static boolean qflg=true; //��ѯ����״��
	public static boolean idbstatus=false;//�Ƿ������״̬
	public static boolean datatempflg=true;//��ѯ����״��
	public static boolean idbdatatempstatus=false;//�Ƿ������״̬
	
	public static  Queue<String> queue_alarm = new LinkedList<String>();//�澯���ݶ�ʱ���
	public static  Queue<String> queue2_alarm= new LinkedList<String>();//�澯���ݶ�ʱ���
	
	public static boolean qflg_alarm=true; //��ѯ����״��
	public static boolean idbstatus_alarm=false;//�Ƿ������״̬
	public static boolean datatempflg_alarm=true;//��ѯ����״��
	public static boolean idbdatatempstatus_alarm=false;//�Ƿ������״̬

	/**
	 * 
	 * ��sql���뵽�ڴ���У�������ݲ���ΪDHCC-DB ��ʾ�������
	 * @param sql �ַ�������2����ʽ��һ����sql��һ���Ǳ�ʾ����ڣ�DHCC-DB��
	 */
	public  static  void Addsql(String sql)
 {
		if (sql.equals("DHCC-DB")) {
			if (qflg == true) {
				if (GathersqlListManager.queue.size() > 1) {
					qflg = !qflg;
					idbstatus = true;
					DBManager pollmg = new DBManager();
					pollmg.excuteBatchSql(GathersqlListManager.queue);
					pollmg.close();
					pollmg = null;
					idbstatus = false;
				}

			} else if (qflg == false) {
				if (GathersqlListManager.queue2.size() > 1) {
					idbstatus = true;
					qflg = !qflg;
					DBManager pollmg = new DBManager();
					pollmg.excuteBatchSql(GathersqlListManager.queue2);

					pollmg.close();
					pollmg = null;

					idbstatus = false;

				}

			}

		} else {
			if (qflg) {
				synchronized (queue) {
					queue.offer(sql);
				}
			} else {
				synchronized (queue2) {
					queue2.offer(sql);
				}
			}
		}
	}
	
	
	/**
	 * 
	 * ��sql���뵽�ڴ���У�������ݲ���ΪDHCC-DB ��ʾ�������
	 * @param sql �ַ�������2����ʽ��һ����sql��һ���Ǳ�ʾ����ڣ�DHCC-DB��
	 */
	public  static  void Addsql_alarm(String sql)
	{
			
		
		System.out.println("==Addsql_alarm=="+sql);
		if(sql.equals("DHCC-DB"))
		{
			
		
		 if(qflg_alarm==true)
		 {
		 //System.out.println("=====������1start=======");
	     //System.out.println("=**=������1=="+GathersqlListManager.queue.size());	
		 if(GathersqlListManager.queue_alarm.size()>1)
		 {
	     qflg_alarm=!qflg_alarm;
	     idbstatus_alarm=true;
		 DBManager pollmg = new DBManager();// ���ݿ�������
		 pollmg.excuteBatchSql(GathersqlListManager.queue_alarm);
		 pollmg.close();
		 pollmg=null;
		 idbstatus_alarm=false;
		 //System.out.println("=====������1end=======");
		 }
		
		 
		 
		 }else if(qflg_alarm==false)
		 {
			// System.out.println("=====������2start=======");
			 if(GathersqlListManager.queue2_alarm.size()>1)
			 {
			 //System.out.println("=**=������2=="+GathersqlListManager.queue2.size());	
			 idbstatus_alarm=true;
			 qflg_alarm=!qflg_alarm;
			 DBManager pollmg = new DBManager();// ���ݿ�������
			 pollmg.excuteBatchSql(GathersqlListManager.queue2_alarm);
			 pollmg.close();
			 pollmg=null;
			 
			 idbstatus_alarm=false;
			 
			 }
			 //System.out.println("=====������2end=======");
			 
		 }
		 
		}else
		 {
			
			if(qflg_alarm)
			{
				
				synchronized (queue_alarm){
			//System.out.println("==�������1");	
			queue_alarm.offer(sql);
				}
			}else
			{
				synchronized (queue2_alarm){
			 //System.out.println("==�������2");
			 queue2_alarm.offer(sql);
			  }
			}
		 }
	}
	
	
	
	
	/**
	 * ���ݷ���ģʽsql���
	 * ��Ϊ�����������keyΪ DHCC-DB ��ʾ�����������ӿڣ���keyֵΪsql��ɾ�����ʱ
	 * ��ʾ�ѽ������sql���뵽�ڴ��б���,��������Ŀ����Ϊ�˱�֤�̵߳İ�ȫ�����ݵ�������
	 * @param sql
	 */
	public  static void AdddateTempsql(String key,Vector sql)
	{
		if(key.equals("DHCC-DB"))
		{
			
			
			 //System.out.println("===��ʱ������="+datatempflg);
			
			 if(datatempflg==true)
			 {
			 if(GathersqlListManager.datatemplist.size()>0)
			 {
				 datatempflg=!datatempflg;
				 idbdatatempstatus=true;
				//System.out.println("=========================99999999====��ʼ��ʱ�������=========="+GathersqlListManager.datatemplist.size());
				
				 if(GathersqlListManager.datatemplist.size()>0)
				 {
					//Vector list=new Vector();
					DBManager dbm=new DBManager();
					
				   Iterator it = GathersqlListManager.datatemplist.keySet().iterator();   
				  while (it.hasNext()){   
				       String keys; 
				       
				       
				       keys=(String)it.next();
				      // System.out.println("======*****==key===="+keys);
				       //list= GathersqlListManager.datatemplist.get(keys);
				       if(null!=GathersqlListManager.datatemplist.get(keys))
				       {
				    	   //logger.info(keys);
				    	   dbm.addBatch(keys);
				    	   for(int i=0;i<GathersqlListManager.datatemplist.get(keys).size();i++)
				    	   {
				    		   //logger.info(GathersqlListManager.datatemplist.get(keys).get(i).toString());
				    		   dbm.addBatch((String)GathersqlListManager.datatemplist.get(keys).get(i).toString());
				    	   }
				    	   
				       }
				       //GathersqlListManager.datatemplist.remove(keys);
				       
				   } 
				  
				  try
				  {
				  dbm.executeBatch();
				  }catch(Exception se)
				  {
					  se.printStackTrace();
					  
				  }finally
				    {
					  dbm.close();
				    }
				 
				  dbm=null;
				  it=null;
				 }
				  GathersqlListManager.datatemplist.clear();
				  //System.out.println("=999999999====��ʼ��ʱ����������=========="+GathersqlListManager.datatemplist.size());
	
				  idbdatatempstatus=false;
			 }
			 
			
			
			 }else if(datatempflg==false)
			 {
				 
				 
				 
				 
				 if(GathersqlListManager.datatemplist2.size()>0)
					{
						//System.out.println("=========================777777====��ʼ��ʱ�������=========="+GathersqlListManager.datatemplist.size());
					     datatempflg=!datatempflg;
					     idbdatatempstatus=true;
						 if(GathersqlListManager.datatemplist2.size()>0)
						 {
							//Vector list=new Vector();
							DBManager dbm=new DBManager();
							
						   Iterator it = GathersqlListManager.datatemplist2.keySet().iterator();   
						  while (it.hasNext()){   
						       String keys; 
						       
						       
						       keys=(String)it.next();
						       //System.out.println("======*****==key===="+keys);
						       //list= GathersqlListManager.datatemplist.get(keys);
						       if(null!=GathersqlListManager.datatemplist2.get(keys))
						       {
						    	   //logger.info(keys);
						    	   dbm.addBatch(keys);
						    	   for(int i=0;i<GathersqlListManager.datatemplist2.get(keys).size();i++)
						    	   {
						    		   //logger.info(GathersqlListManager.datatemplist2.get(keys).get(i).toString());
						    		   dbm.addBatch((String)GathersqlListManager.datatemplist2.get(keys).get(i).toString());
						    	   }
						    	   
						       }
						       //GathersqlListManager.datatemplist.remove(keys);
						       
						   } 
						  try{
						  dbm.executeBatch();
						  }catch(Exception e)
						  {}finally
						  {
							  dbm.close();  
						  }
						  
						  dbm=null;
						  it=null;
						 }
						  GathersqlListManager.datatemplist2.clear();
						  //System.out.println("=777777====��ʼ��ʱ����������=========="+GathersqlListManager.datatemplist.size());
			
						  idbdatatempstatus=false;
				 
				 
				 
			 }
			
			
			
		}
			 
		}else if(key.startsWith("delete") || key.startsWith("DELETE"))
		 {
			
			
			if(datatempflg)
			{
				
			//System.out.println("==������ʱ����1");	
			datatemplist.put(key, sql);	
			}else
			{
			 //System.out.println("==������ʱ����2");
			 datatemplist2.put(key, sql);	
			}
			
		 }
		
	}


}

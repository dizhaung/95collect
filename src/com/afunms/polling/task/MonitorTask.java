/*
 * Created on 2005-3-28
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import net.sf.hibernate.SessionFactory;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class MonitorTask extends TimerTask {
	private int interval = 1000;
	private Context ctx;
	private SessionFactory sessionFactory;
	private Logger logger=Logger.getLogger(MonitorTask.class);

	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	protected Date recentlyStartTime;//最近启动的日期
	
	
	public Date getRecentlyStartTime() {
		return recentlyStartTime;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public void setRecentlyStartTime(Date recentlyStartTime) {
		this.recentlyStartTime = recentlyStartTime;
	}

	public MonitorTask() {
		super();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public abstract void run();
	public void destroy() {

	}
	public boolean isGetSessionFactory(){
		boolean b=false;
		try{
	
			int i=0;

			Context inttex = new InitialContext();
			try {
			//从JNDI中取得SessionFactory的实例，如果出错返回false
				SessionFactory sessionFactory =
				(SessionFactory) inttex.lookup("HibernateSessionFactory");
				b=true;
			} 
			catch (NamingException e) {
				b=false;
				return b;
			}
		}		
		catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	public void setInterval(float d,String t)
	   {
		  if(t.equals("d"))
			 interval =(int) d*24*60*60*1000; //天数
		  else if(t.equals("h"))
			 interval =(int) d*60*60*1000;    //小时
		  else if(t.equals("m"))
			 interval = (int)d*60*1000;       //分钟
		else if(t.equals("s"))
					 interval =(int) d*1000;       //秒
	   }
	

	/**
	 * @return
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @param i
	 */
	

	/**
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}

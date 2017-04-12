package com.afunms.initialize;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.MonitorTask;
import com.afunms.polling.task.MonitorTimer;
import com.afunms.polling.task.TaskFactory;
import com.afunms.polling.task.TaskXml;
import com.afunms.topology.dao.HostNodeDao;
import com.gathertask.TaskManager;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.beanutils.BeanUtils;

public class InitListener
  //implements ServletContextListener
{
	//网络
  private MonitorTimer nettimer = null;
  //PING
  private MonitorTimer pingtimer = null;
  
  private MonitorTask monitorTask = null;
  
  Hashtable task_ht = new Hashtable();

  public InitListener() {
    this.nettimer = null;
    this.pingtimer = null;
  }

  public void contextDestroyed(ServletContextEvent event) {

    if (this.nettimer != null)
      this.nettimer.canclethis(true);
    if (this.pingtimer != null)
      this.pingtimer.canclethis(true);

  }

  public void contextInitialized(ServletContextEvent event)
  {
	//系统初始化
   // SysInitialize sysInit = new SysInitialize();
   // sysInit.setSysPath(event.getServletContext().getRealPath("/"));
    //sysInit.init();
    SysLogger.info("系统 正在启动，请稍候...........");
//    this.nettimer = new MonitorTimer(true);
//    this.pingtimer = new MonitorTimer(true);
//    System.out.println(ResourceCenter.getInstance().getSysPath());
//    System.setProperty("appDir", event.getServletContext().getRealPath("/"));
//    try
//    {
//      long firstTime = 0L;
//
//      this.task_ht = taskNum();
//      int num = this.task_ht.size();
//      TaskFactory taskF = new TaskFactory();
//
//      for (int i = 0; i < num; i++)
//      {
//        String taskinfo = this.task_ht.get(String.valueOf(i)).toString();
//        String[] tmp = taskinfo.split(":");
//        String taskname = tmp[0];
//        float interval = Float.parseFloat(tmp[1]);
//        String unit = tmp[2];
//        SysLogger.info(
//          "interval is -- " + 
//          interval + 
//          "  unit is  -- " + 
//          unit + 
//          "taskname is -- ===" + 
//          taskname + "==================");
//        try
//        {
//          this.monitorTask = taskF.getInstance(taskname);
//          if (this.monitorTask != null)
//          {
//            this.monitorTask.setInterval(interval, unit);
//            if (taskname.equals("netcollecttask")) {
//              this.nettimer.schedule(this.monitorTask, 10000L, this.monitorTask.getInterval());
//            } else if (taskname.equals("pingtask")) {
////              this.pingtimer.schedule(this.monitorTask, 10000L, this.monitorTask.getInterval());
//            }
//            
//            SysLogger.info(taskname + " start success...........");
//          } else {
//            throw new Exception(
//              taskname + "  Task not find ,please check it!");
//          }
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//
//      }
//
//    }
//    catch (Exception e) {
//      SysLogger.info("error in ExecutePing!" + e.getMessage());
//    }

   TaskManager manager = new TaskManager();
   	manager.createAllTask();
    manager.CreateGahterSQLTask();
  }

  public Hashtable taskNum()
  {
    Hashtable ht = new Hashtable();
    int index = 0;
    List list = new ArrayList();
    try {
      TaskXml taskxml = new TaskXml();
      list = taskxml.ListXml();
      for (int i = 0; i < list.size(); i++)
      {
        Task task = new Task();
        BeanUtils.copyProperties(task, list.get(i));
        String sign = task.getStartsign();
        if (("1".equals(sign)) && 
          (!task.getTaskname().equals("linktrust"))) {
          String taskname = task.getTaskname();
          Float interval = task.getPolltime();
          String polltimeunit = task.getPolltimeunit();
          ht.put(String.valueOf(index), taskname + ":" + interval + ":" + polltimeunit);
          index++;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    return ht;
  }
  
  
}
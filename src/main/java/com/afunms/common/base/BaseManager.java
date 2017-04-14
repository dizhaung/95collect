/**
 * <p>Description:father of all Mangager classes</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.common.base;

import java.awt.Color;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.afunms.common.util.SysLogger;
import com.itextpdf.text.Phrase;


public class BaseManager
{
   protected HttpServletRequest request;  
   protected HttpServletResponse response;
   protected HttpSession session;   
   protected int errorCode;  //������Ϣ��;
   private String target;     //Ŀ��jsp
   
   public BaseManager()
   {
   }

   public void setRequest(HttpServletRequest req)
   {
      request = req;
      //response.setContentType("text/html;charset=GB2312");
      //response.setContentType("text/html;charset=UTF-8");
      session = request.getSession();
   }
   
   public void setRequest(HttpServletRequest req,HttpServletResponse res)
   {
      request = req;
      response = res;
      response.setContentType("text/html;charset=GB2312");
      //response.setContentType("text/html;charset=UTF-8");
      session = request.getSession();
   }
   
   /**
    * ��������
    */
   public void setErrorCode(int ec)
   {
      errorCode = ec;
      if(ec!=-1)
         SysLogger.error(ErrorMessage.getErrorMessage(ec));
   }

   public int getErrorCode()
   {
      return errorCode;
   }

   //=================�������õ�����==================
   protected String getParaValue(String para)
   {
      return request.getParameter(para);
   }

   protected String[] getParaArrayValue(String para)
   {
      return request.getParameterValues(para);
   }

   /**
    * �Ѵ�ҳ���ϴ����Ĳ���ת������ 
    */
   protected int getParaIntValue(String para)
   {
      int result = -1;
      String temp = request.getParameter(para);
      if(temp!=null)
         result = Integer.parseInt(temp);
//      else
//    	 SysLogger.error("BaseManager.getParaIntValue(),����[" + para + "]��������!"); 
      return result;
   }

   /**
    * �õ���ǰҳ��
    */
   protected int getCurrentPage()
   {
      int curPage = 1;
      try
      {
         String jp = getParaValue("jp");
         if(jp==null||"".equals(jp))
            jp = (String)session.getAttribute("current_page");
         else
            session.setAttribute("current_page",jp);

         if(jp==null||"".equals(jp))
            curPage = 1;
         else
            curPage = Integer.parseInt(jp);
      }
      catch(NumberFormatException e)
      {
         curPage = 1;
      }
      return curPage;
   }
   
   protected int getPerPagenum()
   {
      int perPage = 15;
      try
      {
         String perpagenum = getParaValue("perpagenum");
         if(perpagenum==null||"".equals(perpagenum)){
        	 perPage = 15;
         }else{
        	 perPage = Integer.parseInt(perpagenum);
         }
      }
      catch(NumberFormatException e)
      {
    	  perPage = 15;
      }
      return perPage;
   }

   protected void setTarget(String target) 
   {
	   this.target = target;
   }

   protected String getTarget() 
   {
	   return target;
   }
   
   /**
    * ��ҳ��ʾ��¼
    * targetJsp:Ŀ¼jsp
    */
   protected String list(DaoInterface dao)
   {
	   String targetJsp = null;
	   	int perpage = getPerPagenum();
       List list = dao.listByPage(getCurrentPage(),perpage);
       if(list==null) return null;
       
       request.setAttribute("page",dao.getPage());
       request.setAttribute("list",list);
       targetJsp = getTarget(); 
	   return targetJsp;
   }
   
   /**
    * ��ҳ��ʾ��¼
    * targetJsp:Ŀ¼jsp
    */
   protected String list(DaoInterface dao,String where)
   {
	   String targetJsp = null;
	   int perpage = getPerPagenum();
       List list = dao.listByPage(getCurrentPage(),where,perpage);
       if(list==null) return null;
       
       request.setAttribute("page",dao.getPage());
       request.setAttribute("list",list);
       targetJsp = getTarget(); 
       
	   return targetJsp;
   }

   protected String delete(DaoInterface dao)
   {
	   String targetJsp = null; 
       String[] id = getParaArrayValue("checkbox");
       if(dao.delete(id))        	 
          targetJsp = getTarget();
       else
      	  targetJsp = null; 
       return targetJsp;
   }
   
   protected String readyEdit(DaoInterface dao)
   {
	   String targetJsp = null; 
       BaseVo vo = null;
       try{
    	   String ii=getParaValue("id");
    	   vo = dao.findByID(getParaValue("id"));       
       }catch(Exception e){
    	   e.printStackTrace();
       }finally{
    	   
       }
       if(vo!=null)
       {	   
          request.setAttribute("vo",vo);
          targetJsp = getTarget();
       }
       return targetJsp;
   }
   
   protected String save(DaoInterface dao,BaseVo vo)
   {
	   String targetJsp = null; 
       if(dao.save(vo))
          targetJsp = getTarget();
       else
      	  targetJsp = null; 
       return targetJsp;
   }
   
   protected String update(DaoInterface dao,BaseVo vo)
   {
	   String targetJsp = null; 
       if(dao.update(vo))
          targetJsp = getTarget();
       else
      	  targetJsp = null; 
       return targetJsp;
   }
}

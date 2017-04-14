package com.afunms.common.util;

/*
 * 此程序是为了方便日期调用
 * 
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

public class DateUtil {

	private static Logger log = Logger.getLogger(DateUtil.class);

	private static String datePattern = "yyyy-MM-dd";

	private static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

	private static String timePattern = "HH:mm";

	/*
	 * Return default datePattern (MM/dd/yyyy)
	 * @return a string representing the date pattern on the UI
	 */

	public static String getDatePattern() {
		return datePattern;
	}
	/*
	 * This method attempts to convert an Oracle-formatted date
	 * in the form dd-MMM-yyyy to mm/dd/yyyy.
	 * @param aDate date from database as a string
	 * @return formatted string for the ui
	 */

	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate != null) {
			df = new SimpleDateFormat(datePattern);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/*
	 * This method generates a string representation of a date/time
	 * in the format you specify on input
	 *
	 * @param aMask the date pattern the string is in
	 * @param strDate a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */

	public static final Date convertStringToDate(String aMask, String strDate)
	throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
			+ aMask + "'");
		}
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			//log.error("ParseException: " + pe);
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return (date);
	}

	/*
	 * This method returns the current date time in the format:
	 * MM/dd/yyyy HH:MM a
	 *
	 * @param theTime the current time
	 * @return the current date/time
	 */

	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/*
	 * This method returns the current date in the format: yyyy/MM/dd
	 *
	 * @return the current date
	 * @throws ParseException
	 */

	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(datePattern);
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));
		return cal;
	}

	/*
	 * This method generates a string representation of a date's date/time
	 * in the format you specify on input
	 *
	 * @param aMask the date pattern the string is in
	 * @param aDate a date object
	 * @return a formatted string representation of the date
	 *
	 * @see java.text.SimpleDateFormat
	 */

	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate == null) {
			log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}
		return (returnValue);
	}

	/*
	 * This method generates a string representation of a date based
	 * on the System Property 'dateFormat'
	 * in the format you specify on input
	 *
	 * @param aDate A date to convert
	 * @return a string representation of the date
	 */

	public static final String convertDateToString(Date aDate) {
		return getDateTime(datePattern, aDate);
	}

	//***************************************************

	//名称：strToDate

	//功能：将指定的字符串转换成日�?

	//输入：aStrValue: 要转换的字符�?

	//aFmtDate: 转换日期的格�? 默认�?"yyyy/MM/dd"

	//      aDteRtn: 转换后的日期

	//输出�?

	//返回：TRUE: 是正确的日期格式; FALSE: 是错误的日期格式

	//***************************************************

	public static boolean strToDate(

	String aStrValue,

	String aFmtDate,

	java.util.Date aDteRtn)

	{

		if (aFmtDate.length() == 0)

		{

			aFmtDate = "yyyy/MM/dd";

		}

		SimpleDateFormat fmtDate = new SimpleDateFormat(aFmtDate);

		try

		{

			aDteRtn.setTime(fmtDate.parse(aStrValue).getTime());

		}

		catch (Exception e)

		{

			return (false);

		}

		return (true);

	}

	//  得到昨天具体每个小时的日期，i代表小时�?

	public static String getLastDateHour(int i) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(rightNow.DATE, -1);
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int date = rightNow.get(Calendar.DATE);
		rightNow.set(year, month, date, i, 0, 0);
		//       rightNow.add(Calendar.MONTH, 1);

		rightNow.getTime();
		String getDate = df.format(rightNow.getTime());
		return getDate;
	}

	public static Date getDayByAdd(String strDate) throws Exception {

		String datePattern = "yyyy-mm-dd";

		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(sdf.parse(strDate));

		int year = rightNow.get(Calendar.YEAR);

		int month = Integer.parseInt(strDate.substring(5, 7)) - 1;

		int day = rightNow.get(Calendar.DATE) + 1;

		rightNow.set(year, month, day, 0, 0, 0);

		return rightNow.getTime();

	}

	public static java.util.Date getCheckDateTmp(String strdate)

	{
		try {

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			java.util.Date checkDateTmp = sdf.parse(strdate);

			return checkDateTmp;

		} catch (Exception e) {

			return new java.sql.Date(System.currentTimeMillis());

		}

	}

	public static Date getBeginDate() throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new Date());

		rightNow.add(Calendar.MONTH, -2);

		int year = rightNow.get(Calendar.YEAR);

		int month = rightNow.get(Calendar.MONTH);

		rightNow.set(year, month, 1);

		//       rightNow.add(Calendar.MONTH, 1);

		return rightNow.getTime();

	}

	//  得到系统当前时间，比�? 2006-01-02 10:22:15

	public static String getRightNow() {

		SimpleDateFormat df = null;

		String returnValue = "";

		Date aDate = new Date();

		if (aDate == null) {

			log.error("aDate is null!");

		} else {

			df = new SimpleDateFormat(dateTimePattern);

			returnValue = df.format(aDate);

		}

		return (returnValue);

	}

	public static synchronized java.util.Date getNextMonth(java.util.Date date) {

		/**

		 * 详细设计�?

		 * 1.指定日期的月份加1

		 */

		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();

		gc.setTime(date);

		gc.add(Calendar.MONTH, 1);

		return gc.getTime();

	}

	//  得到昨天具体每个小时的日期，i代表小时�?返回格式yyyymmddhhmmss

	public static String getUpSendDateTime(int i) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new Date());

		rightNow.add(rightNow.DATE, -1);

		int year = rightNow.get(Calendar.YEAR);

		int month = rightNow.get(Calendar.MONTH);

		int date = rightNow.get(Calendar.DATE);

		//       24点单独处理，取出23点的日期，再替换23�?4

		if (i == 24) {

			rightNow.set(year, month, date, i - 1, 0, 0);

			rightNow.getTime();

			String strDate = df.format(rightNow.getTime());

			strDate = strDate.replaceAll("-", "");

			strDate = strDate.replaceAll(":", "");

			strDate = strDate.replaceAll(" ", "");

			strDate = strDate.substring(0, 9) + "40000";

			return strDate;

		}

		rightNow.set(year, month, date, i, 0, 0);

		// rightNow.add(Calendar.MONTH, 1);

		rightNow.getTime();

		String strDate = df.format(rightNow.getTime());

		strDate = strDate.replaceAll("-", "");

		strDate = strDate.replaceAll(":", "");

		strDate = strDate.replaceAll(" ", "");

		return strDate;

	}

	//  得到昨天具体每个小时的日期，i代表小时�?返回格式yyyymmddhhmmss
	//按时间生成临时文件名

	public static String getGenFileName() {

		java.util.Date date = new java.util.Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");

		String sf = sdf.format(date);

		return sf;

	}

	public static String getUploadDateTime() throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new Date());

		// rightNow.add(Calendar.MONTH, 1);

		rightNow.getTime();

		String strDate = df.format(rightNow.getTime());

		strDate = strDate.replaceAll("-", "");

		strDate = strDate.replaceAll(":", "");

		strDate = strDate.replaceAll(" ", "");

		return strDate;

	}

	//  得到上一个月的年月时�?

	public static String getUpMonth() {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new java.util.Date());

		rightNow.add(Calendar.MONTH, -1);

		int year = rightNow.get(Calendar.YEAR);

		int month = rightNow.get(Calendar.MONTH);

		rightNow.set(year, month, 1);

		return (DateUtil.getDate(rightNow.getTime()).substring(0, 7));

	}

	//  得到上一个月的最后一�?

	public static String getUpMonthLastDay() {

		SimpleDateFormat df = null;

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new java.util.Date());

		int year = rightNow.get(Calendar.YEAR);

		int month = rightNow.get(Calendar.MONTH);

		int date = rightNow.get(Calendar.DATE);

		rightNow.set(year, month, 1, 0, 0, 0);

		rightNow.add(Calendar.DATE, -1);

		int yearLast = rightNow.get(Calendar.YEAR);

		int monthLast = rightNow.get(Calendar.MONTH);

		int dateLast = rightNow.get(Calendar.DATE);

		rightNow.set(yearLast, monthLast, dateLast, 0, 0, 0);

		String returnValue = null;

		df = new SimpleDateFormat(datePattern);

		returnValue = df.format(rightNow.getTime());

		return returnValue;

	}

	//   增加以小�?

	public static String getDateAddByHour(String date, int hour)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(getCheckDateTmp(date));

		rightNow.add(Calendar.HOUR, hour);

		return getDateTime(dateTimePattern, rightNow.getTime());

	}

	public static Date getEndDate() throws Exception {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new Date());

		rightNow.add(Calendar.MONTH, 1);

		int year = rightNow.get(Calendar.YEAR);

		int month = rightNow.get(Calendar.MONTH);

		rightNow.set(year, month, 1);

		//    rightNow.add(Calendar.MONTH, -1);

		return rightNow.getTime();

	}

	public static Date getEndDate(String yearStr, String monthStr)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();

		//      rightNow.setTime(new Date());

		int year = Integer.parseInt(yearStr);

		int month = Integer.parseInt(monthStr);

		rightNow.set(year, month, 1, 0, 0, 0);

		return rightNow.getTime();

	}

	/**

	 * This method converts a String to a date using the datePattern

	 *

	 * @param strDate the date to convert (in format yyyy/MM/dd)

	 * @return a date object

	 *

	 * @throws ParseException

	 */

	public static Date convertStringToDate(String strDate)

	throws ParseException {

		Date aDate = null;

		try {

			if (log.isDebugEnabled()) {

				log.debug("converting date with pattern: " + datePattern);

			}

			aDate = convertStringToDate(datePattern, strDate);

		} catch (ParseException pe) {

			log.error("Could not convert '" + strDate

			+ "' to a date, throwing exception");

			pe.printStackTrace();

			throw new ParseException(pe.getMessage(),

			pe.getErrorOffset());

		}

		return aDate;

	}

	/**

	 * This method converts a String to a date using the datePattern

	 *

	 * @param strDate the date to convert (in format yyyy/MM/dd)

	 * @return a date object

	 *

	 * @throws ParseException

	 */

	public static Date convertStringToDateTime(String strDate)

	throws ParseException {

		Date aDate = null;

		try {

			if (log.isDebugEnabled()) {

				log.debug("converting date with pattern: " + dateTimePattern);

			}

			aDate = convertStringToDate(dateTimePattern, strDate);

		} catch (ParseException pe) {

			log.error("Could not convert '" + strDate

			+ "' to a date, throwing exception");

			pe.printStackTrace();

			throw new ParseException(pe.getMessage(),

			pe.getErrorOffset());

		}

		return aDate;

	}

	public static String getLastDate() {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(new Date());

		rightNow.add(rightNow.DATE, -1);

		int year = rightNow.get(Calendar.YEAR);

		int month = rightNow.get(Calendar.MONTH) + 1;

		int date = rightNow.get(Calendar.DATE);

		String buf;

		buf = (new Integer(year).toString());

		if (month < 10) {

			buf = buf + "-" + "0" + (new Integer(month).toString());

		} else

		{

			buf = buf + "-" + (new Integer(month).toString());

		}

		if (date < 10) {

			buf = buf + "-" + "0" + (new Integer(date).toString());

		} else

		{

			buf = buf + "-" + (new Integer(date).toString());

		}

		return buf;

	}

	// 得到�?��时间�?4�?0的LastDateHour

	public static String getLastDateHourFor24(int i) throws Exception {

		String getDate = getLastDate();

		if (i < 10) {

			getDate = getDate + " " + "0" + i + ":00:00";

		} else

		{

			getDate = getDate + " " + i + ":00:00";

		}

		return getDate;

	}

	public static String getLastDateByInt(int i) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date());
		rightNow.add(rightNow.DATE, -i);
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int date = rightNow.get(Calendar.DATE);
		rightNow.set(year, month, date, 23, 59, 59);
		//       rightNow.add(Calendar.MONTH, 1);

		rightNow.getTime();
		String getDate = df.format(rightNow.getTime());
		return getDate;
	}

	public static String getNextMonth(String str) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));

		rightNow.add(rightNow.MONTH, 1);
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int date = rightNow.get(Calendar.DATE);
		rightNow.set(year, month, date, 23, 59, 59);
		//       rightNow.add(Calendar.MONTH, 1);

		rightNow.getTime();
		String getDate = df.format(rightNow.getTime());
		return getDate;
	}

	public static String getLastHour(String str, int i) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));

		rightNow.add(rightNow.HOUR, i);
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int date = rightNow.get(Calendar.DATE);
		int hour = rightNow.get(Calendar.HOUR);
		int minute = rightNow.get(Calendar.MINUTE);
		rightNow.set(year, month, date, hour, minute, 00);
		rightNow.getTime();
		String getDate = df.format(rightNow.getTime());
		return getDate;

	}

	public static String getMonthLastDay(String str) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));

		rightNow.add(rightNow.MONTH, 1);

		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		rightNow.set(year, month, 01, 00, 00, 00);
		rightNow.add(rightNow.DATE, -1);
		int year1 = rightNow.get(Calendar.YEAR);
		int month1 = rightNow.get(Calendar.MONTH);
		int date1 = rightNow.get(Calendar.DATE);
		rightNow.set(year1, month1, date1, 00, 00, 00);
		rightNow.getTime();
		String getDate = df.format(rightNow.getTime());

		return getDate;

	}

	public static String getLastMinute(String str, int i) throws Exception {

		SimpleDateFormat df = new SimpleDateFormat(dateTimePattern);
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));

		rightNow.add(rightNow.MINUTE, i);
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int date = rightNow.get(Calendar.DATE);
		int hour = rightNow.get(Calendar.HOUR);
		int minute = rightNow.get(Calendar.MINUTE);
		int second = rightNow.get(Calendar.SECOND);
		rightNow.set(year, month, date, hour, minute, second);
		rightNow.getTime();
		String getDate = df.format(rightNow.getTime());
		return getDate;

	}

	public static String getWeekFirstDay(String str) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));
		int week = rightNow.get(Calendar.DAY_OF_WEEK);
		if (week == 1) {
			rightNow.add(rightNow.DATE, -6);
		} else if (week == 2) {
		} else if (week == 3) {
			rightNow.add(rightNow.DATE, -1);
		} else if (week == 4) {
			rightNow.add(rightNow.DATE, -2);
		} else if (week == 5) {
			rightNow.add(rightNow.DATE, -3);
		} else if (week == 6) {
			rightNow.add(rightNow.DATE, -4);
		} else if (week == 7) {
			rightNow.add(rightNow.DATE, -5);
		}

		String getDate = df.format(rightNow.getTime());

		return getDate;
	}

	public static String getDateAddByMinute(String date, int min)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(getCheckDateTmp(date));

		rightNow.add(Calendar.MINUTE, min);

		return getDateTime(dateTimePattern, rightNow.getTime());

	}

	public static String getDateAddByDate(String date, int day)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(getCheckDateTmp(date));

		rightNow.add(Calendar.DATE, day);

		return getDateTime(dateTimePattern, rightNow.getTime());

	}

	public static String getDateAddByMonth(String date, int month)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(getCheckDateTmp(date));

		rightNow.add(Calendar.MONTH, month);

		return getDateTime(dateTimePattern, rightNow.getTime());

	}

	public static String getDateAddByYear(String date, int year)
			throws Exception {

		Calendar rightNow = Calendar.getInstance();

		rightNow.setTime(getCheckDateTmp(date));

		rightNow.add(Calendar.YEAR, year);

		return getDateTime(dateTimePattern, rightNow.getTime());

	}

	/****
	 * @param time,指定时间
	 * @param n,指定时间�?
	 * 功能：指定时间前N天的具体时间
	 * eg time为当前时间，n�?，返回�?为昨天的这个时刻
	 * ***/
	public static String getTimeOfBeforeN(String time, int n) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar rightNow = Calendar.getInstance();
		try {
			rightNow.setTime(new Date(df.parse(time).getTime()));
		} catch (ParseException ex) {
		}
		rightNow.add(Calendar.DATE, n);

		String getDate = df.format(rightNow.getTime());

		return getDate;
	}

	/*判断指定日期的星期数*/
	public static int getDayOfWeek(String str) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));
		int num = rightNow.get(Calendar.DAY_OF_WEEK) - 1;
		return num;
	}

	/*判断指定日期�?��周的周日*/
	public static String getWeekEndDay(String str) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(new Date(df.parse(str).getTime()));
		int week = rightNow.get(Calendar.DAY_OF_WEEK);
		if (week == 1) {
		} else if (week == 2) {
			rightNow.add(rightNow.DATE, 6);
		} else if (week == 3) {
			rightNow.add(rightNow.DATE, 5);
		} else if (week == 4) {
			rightNow.add(rightNow.DATE, 4);
		} else if (week == 5) {
			rightNow.add(rightNow.DATE, 3);
		} else if (week == 6) {
			rightNow.add(rightNow.DATE, 2);
		} else if (week == 7) {
			rightNow.add(rightNow.DATE, 1);
		}

		String getDate = df.format(rightNow.getTime());

		return getDate;
	}
	public static void main(String[] args) throws Exception{
		DateUtil du = new DateUtil();
		String str="2016-11-28 17:52:13";
		System.out.println(du.getDateAddByDate(str, 1));
	}
}

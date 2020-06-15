package com.greathammer.usmj.intensity.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间助手. <br>
 * 时间计算、格式化显示.
 * 
 * @version 0.2.2 2016-07-06
 * @since Java 8.0
 * @author Zheng Chao
 */
public class DateHelper {

	private DateHelper() {
		throw new AssertionError();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 显示当前时间, 格式为: yyyy-MM-dd HH:mm:ss.SSS. <blockquote>
	 * 
	 * <pre>
	 * 例子: 2016-07-07 08:37:54.149
	 * </pre>
	 * 
	 * </blockquote>
	 * 使用了{@code Calendar.getInstance().getTime()}调用当前时间,详见{@link Calendar}
	 * <p>
	 * <i>Tag:Method 1-1</i>
	 */
	public static String nowDate_StringFormate() {
		String DateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * 生成基于当前时间的ID, 格式为: yyyyMMddHHmmss. <blockquote>
	 * 
	 * <pre>
	 * 例子: 20160707084753
	 * </pre>
	 * 
	 * </blockquote>
	 * 使用了{@code Calendar.getInstance().getTime()}调用当前时间,详见{@link Calendar}
	 * <p>
	 * <i>Tag:Method 1-2</i>
	 */
	public static String nowDate_StringFormateToEventID() {
		String DateFormat = "yyyyMMddHHmmss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * 生成基于目前时间, 用于系统日志使用的日期标识, 格式: yyyyMM. <blockquote>
	 * 
	 * <pre>
	 * 例子: 201607
	 * </pre>
	 * 
	 * </blockquote>
	 * 使用了{@code Calendar.getInstance().getTime()}调用当前时间,详见{@link Calendar}
	 * <p>
	 * <i>Tag:Method 1-3</i>
	 */
	public static String nowDate_StringFormateForSystemLog() {
		String DateFormat = "yyyyMM";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * 生成基于目前时间的, 用于预警MQ消息(服务器发送时间)的日期格式, 格式: HH:mm:ss. <blockquote>
	 * 
	 * <pre>
	 * 例子: 09:59:35
	 * </pre>
	 * 
	 * </blockquote>
	 * 使用了{@code Calendar.getInstance().getTime()}调用当前时间,详见{@link Calendar}
	 * <p>
	 * <i>Tag:Method 1-4</i>
	 */
	public static String nowDate_StringFormateForEEWMQ() {
		String DateFormat = "HH:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(Calendar.getInstance().getTime());
	}

	/**
	 * 将指定时间转换成格式字符, 格式: yyyy-MM-dd HH:mm:ss.SSS. <blockquote>
	 * 
	 * <pre>
	 * 例子: 2016-07-07 10:18:46.813
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * <i>Tag:Method 1-5</i>
	 * 
	 * @param date
	 */
	public static String dateToStringFormate(Date date) {
		String DateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(date);
	}

	/**
	 * 将指定时间转换成格式字符, 格式: HH:mm:ss.SSS. <blockquote>
	 * 
	 * <pre>
	 * 例子: 10:18:46.813
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * <i>Tag:Method 1-6</i>
	 * 
	 * @param date
	 */
	public static String dateToStringFormateMilliTime(Date date) {
		String DateFormat = "HH:mm:ss.SSS";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(date);
	}

	/**
	 * 将指定时间转换成格式字符, 格式: yyyy-MM-dd HH:mm:ss. <blockquote>
	 * 
	 * <pre>
	 * 例子: 2016-07-07 10:24:47
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * <i>Tag:Method 1-7</i>
	 * 
	 * @param date
	 */
	public static String dateToStringFormateNormal(Date date) {
		String DateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(date);
	}

	/**
	 * 将指定时间转换成格式字符(为MQ定制), 格式: HH:mm:ss. <blockquote>
	 * 
	 * <pre>
	 * 例子: 10:26:43
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * <i>Tag:Method 1-8</i>
	 * 
	 * @param date
	 */
	public static String dateToStringFormateForEEWMQ(Date date) {
		String DateFormat = "HH:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(date);
	}

	/**
	 * 将指定时间转换成格式字符(为Weibo定制), 格式: MM月dd日HH时mm分. <blockquote>
	 * 
	 * <pre>
	 * 例子: 07月07日10时29分
	 * </pre>
	 * 
	 * </blockquote>
	 * <p>
	 * <i>Tag:Method 1-9</i>
	 * 
	 * @param date
	 */
	public static String dateToStringFormateForWeibo(Date date) {
		String DateFormat = "MM月dd日HH时mm分";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.format(date);
	}

	/**
	 * 将格式字符转换成 {@code Date} 时间格式.
	 * <p>
	 * <i>Tag:Method 1-10</i>
	 * 
	 * @param string
	 *            格式为: yyyy-MM-dd HH:mm:ss
	 * @throws ParseException:
	 *             非上述格式的字符将抛出错误
	 */
	public static Date StringFormateNormalToDate(String string) throws ParseException {
		String DateFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.parse(string);
	}

	/**
	 * 将格式字符转换成 {@code Date} 时间格式.
	 * <p>
	 * <i>Tag:Method 1-11</i>
	 * 
	 * @param string
	 *            格式为: yyyy-MM-dd HH:mm:ss.SSS
	 * @throws ParseException:
	 *             非上述格式的字符将抛出错误
	 */
	public static Date StringFormateToDate(String string) throws ParseException {
		String DateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		return dateFormat.parse(string);
	}

	/**
	 * 返回当前时间 {@code Date} 时间格式. <br>
	 * 使用了{@code Calendar.getInstance().getTime()}调用当前时间,详见{@link Calendar}
	 * <p>
	 * <i>Tag:Method 1-12</i>
	 */
	public static Date nowDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 将时间转成时间戳. <br>
	 * 时间戳为: 与格林威治标准时间1970.01.01 00:00:00的毫秒时间差. <br>
	 * 时间戳计算方法: {@code Date(Obj).getTime()}
	 * <p>
	 * <i>Tag:Method 1-13</i>
	 * 
	 * @param date
	 */
	public static long dateToTimeStamp(Date date) {
		return date.getTime();
	}

	/**
	 * 将时间戳转成时间. <br>
	 * 时间戳为: 与格林威治标准时间1970.01.01 00:00:00的毫秒时间差.
	 * <p>
	 * <i>Tag:Method 1-14</i>
	 * 
	 * @param long
	 *            timeStamp
	 */
	public static Date dateFromTimeStamp(long timeStamp) {
		Date date = new Date(timeStamp);
		return date;
	}

	/**
	 * 计算时间差, 返回单位: 秒(int).
	 * <p>
	 * <i>Tag:Method 1-15</i>
	 * 
	 * @param Date
	 *            dateBefore 较早时间
	 * @param Date
	 *            dateAfter 较晚时间
	 * @return int 时间差,秒
	 */
	public static int dateInterval(Date dateBefore, Date dateAfter) {
		return (int) ((dateAfter.getTime() - dateBefore.getTime()) / 1000);
	}

	/**
	 * 计算时间差, 返回单位: 毫秒(int).
	 * <p>
	 * <i>Tag:Method 1-16</i>
	 * 
	 * @param Date
	 *            dateBefore 较早时间
	 * @param Date
	 *            dateAfter 较晚时间
	 * @return int 时间差,毫秒
	 */
	public static int dataIntervalMilliseconds(Date dateBefore, Date dateAfter) {
		return (int) (dateAfter.getTime() - dateBefore.getTime());
	}

	/**
	 * 将yyyy,MM,dd,HH:mm:ss.SSSS(DTime 监测中心预警时间类)转成yyyy-MM-dd HH:mm:ss表达形式
	 * <p>
	 * <i>Tag:Method 1-17</i>
	 */
	public static String dTimeToFormatedString(String dTime) throws ParseException {
		String DateFormat = "yyyy,MM,dd,HH:mm:ss.SSS";
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormat);
		Date date = dateFormat.parse(dTime.substring(0, dTime.length() - 2));
		return DateHelper.dateToStringFormateNormal(date);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 演示.
	 * 
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		// Method 2 Test
		// Method 2-1 nowStringTypeOne
		// old method (Method 1) Test...
		// Method 1-1 nowDate_StringFormate
		System.out.println("Method 1-1 nowDate_StringFormate method test: " + nowDate_StringFormate());
		// Method 1-2 nowDate_StringFormateToEventID
		System.out.println("Method 1-2 nowDate_StringFormateToEventID test: " + nowDate_StringFormateToEventID());
		// Method 1-3 nowDate_StringFormateForSystemLog
		System.out.println("Method 1-3 nowDate_StringFormateForSystemLog test: " + nowDate_StringFormateForSystemLog());
		// Method 1-4 nowDate_StringFormateForEEWMQ
		System.out.println("Method 1-4 nowDate_StringFormateForEEWMQ test: " + nowDate_StringFormateForEEWMQ());
		// Method 1-5 dateToStringFormate
		System.out.println("Method 1-5 dateToStringFormate test: " + dateToStringFormate(new Date()));
		// Method 1-6 dateToStringFormateMilliTime
		System.out.println("Method 1-6 dateToStringFormateMilliTime test: " + dateToStringFormateMilliTime(new Date()));
		// Method 1-7 dateToStringFormateNormal
		System.out.println("Method 1-7 dateToStringFormateNormal test: " + dateToStringFormateNormal(new Date()));
		// Method 1-8 dateToStringFormateForEEWMQ
		System.out.println("Method 1-8 dateToStringFormateForEEWMQ test: " + dateToStringFormateForEEWMQ(new Date()));
		// Method 1-9 dateToStringFormateForWeibo
		System.out.println("Method 1-9 dateToStringFormateForWeibo test: " + dateToStringFormateForWeibo(new Date()));
		// Method 1-10 StringFormateNormalToDate
		String method1_10S = "2016-07-07 10:29:25";
		Date method1_10 = StringFormateNormalToDate(method1_10S);
		System.out.println("Method 1-10 StringFormateNormalToDate \n\t转换之前字符为: " + method1_10S + "\n\t转换后Date内容: "
				+ dateToStringFormateNormal(method1_10));
		// Method 1-11 StringFormateToDate
		String method1_11S = "2016-07-07 10:43:57.532";
		Date method1_11 = StringFormateToDate(method1_11S);
		System.out.println("Method 1-11 StringFormateNormalToDate \n\t转换之前字符为: " + method1_11S + "\n\t转换后Date内容: "
				+ dateToStringFormate(method1_11));
		// Method 1-13 与 Method 1-14 时间戳互转
		Date method1_13 = new Date();
		System.out.println("Method 1-13 与 1-14 演示时间为: " + dateToStringFormate(method1_13));
		System.out.println("\tMethod 1-13 转换时间戳为: " + dateToTimeStamp(method1_13));
		Date method1_14 = new Date(dateToTimeStamp(method1_13));
		System.out.println("\tMethod 1-14 时间戳转时间为: " + dateToStringFormate(method1_14));
		// Method 1-15 dateInterval
		Date method1_15Before = new Date();
		Date method1_15After = new Date(method1_15Before.getTime() + 1000);
		System.out.println("Method 1-15 dateInterval Test " + "\n\t 前面时间: " + dateToStringFormate(method1_15Before)
				+ "\n\t 后面时间: " + dateToStringFormate(method1_15After) + "\n\t 时间差: "
				+ dateInterval(method1_15Before, method1_15After));
		// Method 1-16 dataIntervalMilliseconds
		Date method1_16Before = new Date();
		Date method1_16After = new Date(method1_15Before.getTime() + 1200);
		System.out.println("Method 1-16 dateInterval Test " + "\n\t 前面时间: " + dateToStringFormate(method1_16Before)
				+ "\n\t 后面时间: " + dateToStringFormate(method1_16After) + "\n\t 时间差: "
				+ dataIntervalMilliseconds(method1_16Before, method1_16After));
		// Method 1-17 dTimeToFormatedString
		String method1_17S = "2016,07,07,11:34:59.2900";
		System.out.println("Method 1-17 dTimeToFormatedString Test " + "\n\t 转换之前字符串为: " + method1_17S
				+ "\n\t 转换之后字符串为: " + dTimeToFormatedString(method1_17S));
	}
}

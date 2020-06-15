package com.greathammer.usmj.eewp.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

	/**
	 * 生成基于当前时间的ID格式为: yyyyMMddHHmmss. <blockquote>
	 * 
	 * <pre>
	 * 例子: 20160707081840
	 * </pre>
	 * 
	 * </blockquote> 使用了{@link LocalDateTime}调用当前时间
	 * <p>
	 * <i>Tag:Method 2-2</i>
	 */
	public static String idGeneratorNowTimeFormatTypeOne() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
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

}

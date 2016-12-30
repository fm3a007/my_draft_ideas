/**
 *
 *  
 * These source files are released under the GPLv3 license.
 */
package my.frmwk.util;

import java.util.Calendar;
import java.util.Date;

public class StartAndEndDate {
	/**
	 * 获取给定时间所在周的第一天(Sunday)的日期和最后一天(Saturday)的日期
	 * 
	 * @param calendar
	 * @return Date数组，[0]为第一天的日期，[1]最后一天的日期
	 */
	public Date[] getWeekStartAndEndDate(Calendar calendar) {
		Date[] dates = new Date[2];
		Date firstDateOfWeek, lastDateOfWeek;
		// 得到当天是这周的第几天
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		// 减去dayOfWeek,得到第一天的日期，因为Calendar用０－６代表一周七天，所以要减一
		calendar.add(Calendar.DAY_OF_WEEK, -(dayOfWeek - 1));
		firstDateOfWeek = calendar.getTime();
		// 每周7天，加６，得到最后一天的日子
		calendar.add(Calendar.DAY_OF_WEEK, 6);
		lastDateOfWeek = calendar.getTime();
		
		dates[0] = firstDateOfWeek;
		dates[1] = lastDateOfWeek;
		return dates;
	}
	/**
	 * 获取date所在当月的第一天和最后一天的日期
	 * @param date
	 * @return
	 */
	public  Date[] getMonthStartAndEndDate(Date date) {
		Date[] dates = new Date[2];
		int m = date.getMonth();
		int y = date.getYear();
		Date firstDay = new Date(y, m + 1, 1);
		int min = 24 * 60 * 60 * 1000;
		dates[0] = new Date(y, m, 1);
		dates[1] = new Date(firstDay.getTime() - min);
		return dates;
	}
	/**
	 * 获取date所在当年的第一天和最后一天的日期
	 * @param date
	 * @return
	 */
	public Date[] getYearStartAndEndDate(Date date) {
		Date[] dates = new Date[2];
		int m = date.getMonth();
		int y = date.getYear();
		Date firstDay = new Date(y + 1, 0, 1);
		int min = 24 * 60 * 60 * 1000;
		dates[0] = new Date(y, 0, 1);
		dates[1] = new Date(firstDay.getTime() - min);
		return dates;
	}

}

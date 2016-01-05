package com.github.ffpojo.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date convertToDate(Calendar dtCalendar) {
		return new Date(dtCalendar.getTimeInMillis());
	}

	public static Calendar convertToCalendar(Date date) {
		Calendar dtCalendar = Calendar.getInstance();
		dtCalendar.setTime(date);
		return dtCalendar;
	}

	public static Date floorDate(Date date) {
		Calendar dateAsCalendar = convertToCalendar(date);
		dateAsCalendar.set(Calendar.HOUR_OF_DAY, 0);
		dateAsCalendar.set(Calendar.MINUTE, 0);
		dateAsCalendar.set(Calendar.SECOND, 0);
		dateAsCalendar.set(Calendar.MILLISECOND, 0);
		return convertToDate(dateAsCalendar);
	}

}

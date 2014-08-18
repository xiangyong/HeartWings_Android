package com.nwpu.heartwings.util;

import java.util.Calendar;

public class TimeUtil {

	public static String GetHourMinute(long date){
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		
		StringBuilder sb = new StringBuilder();
		sb.append(calendar.get(Calendar.HOUR_OF_DAY) +"ʱ" + calendar.get(Calendar.MINUTE) + "��");
		
		return sb.toString();
	}
}

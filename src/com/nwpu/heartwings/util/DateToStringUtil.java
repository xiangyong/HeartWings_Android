package com.nwpu.heartwings.util;

import java.util.Calendar;
import java.util.Date;


public class DateToStringUtil {

	public static String ConvertToString(Date date){
		
		StringBuilder sb = new StringBuilder();
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		sb.append(calendar.get(Calendar.YEAR) + "��");
		
		int month = calendar.get(Calendar.MONTH) + 1;
		
		sb.append(month+"��"+calendar.get(Calendar.DAY_OF_MONTH)+"��     ");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY) + "ʱ" + calendar.get(Calendar.MINUTE) + "��");
		
		return sb.toString();
		
	}
}

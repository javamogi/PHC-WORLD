package com.phcworld.web;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtils {
	
	public static final int MIN = 60;
	public static final int HOUR = 24;

	public static String getTime(LocalDateTime inputDate) {
		if(inputDate == null) {
			return "";
		}
		if(LocalDateTime.now().getYear() - inputDate.getYear() > 0) {
			return inputDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		}
		long minutesGap = Duration.between(inputDate, LocalDateTime.now()).toMinutes();
		
		if (minutesGap / MIN < HOUR) {
			if (minutesGap / MIN < 1) {
				if (minutesGap == 0) {
					return "방금전";
				}
				return minutesGap + "분 전";
			}
			return (minutesGap / MIN) + "시간 전";
		}
		
		/*long duration = Duration.between(inputDate, LocalDateTime.now()).toDays();
		if (duration < 1) {
			return inputDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		}*/
		return inputDate.format(DateTimeFormatter.ofPattern("MM.dd"));
	}

}

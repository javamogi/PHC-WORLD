package com.phcworld.web;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtils {
	
	public static final int MINUTES = 60;
	public static final int HOUR = 24;

	public static String getTime(LocalDateTime inputDate) {
		if(inputDate == null) {
			return "";
		}
		if(LocalDateTime.now().getYear() - inputDate.getYear() > 0) {
			return inputDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
		}
		long inputDateAndNowDifferenceMinutes = Duration.between(inputDate, LocalDateTime.now()).toMinutes();
		
		if (inputDateAndNowDifferenceMinutes / MINUTES < HOUR) {
			if (inputDateAndNowDifferenceMinutes / MINUTES < 1) {
				if (inputDateAndNowDifferenceMinutes == 0) {
					return "방금전";
				}
				return inputDateAndNowDifferenceMinutes + "분 전";
			}
			return (inputDateAndNowDifferenceMinutes / MINUTES) + "시간 전";
		}
		
		/*long duration = Duration.between(inputDate, LocalDateTime.now()).toDays();
		if (duration < 1) {
			return inputDate.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
		}*/
		return inputDate.format(DateTimeFormatter.ofPattern("MM.dd"));
	}

}

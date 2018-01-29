package io.mithrilcoin.front.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	/**
	 * 특정날짜 패턴을 다른 패턴으로 변경하는 함수
	 * @param strDate 날짜 형식 문자열
	 * @param sourcePattern  예시 yyyyMMddHHmmss
	 * @param targetPattern 예시 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String dateStringReplace(String strDate, String sourcePattern, String targetPattern) {
		// yyyy-mm-dd hh:mm:ss
		String date1 = null;
		try {

			// input date format
			SimpleDateFormat df_in = new SimpleDateFormat(sourcePattern);

			// output date format
			SimpleDateFormat df_output = new SimpleDateFormat(targetPattern);
			Date date = df_in.parse(strDate);
			date1 = df_output.format(date);
		} catch (Exception e) {
			System.out.println("Invalid Date: " + e.getMessage());
		}
		if (date1 != null) {
			return date1;
		}

		return strDate;
	}
	
	/**
	 * Date 형식을 특정 패턴을 가지는 날짜 패턴 문자열로 변경.
	 * @param sourceDate
	 * @param pattern
	 * @return
	 */
	public String date2String(Date sourceDate, String pattern)
	{
		
		SimpleDateFormat df_in = new SimpleDateFormat(pattern);
		String formatedString = df_in.format(sourceDate);
		
		return formatedString;
	}
	
	
	/***
	 * 현재 시간 스트링 반환 UTC 기준 
	 * @return
	 */
	public String getUTCNow() {
		ZonedDateTime date = ZonedDateTime.now(ZoneOffset.UTC);
		return String.valueOf(	date.toInstant().toEpochMilli());
	}
//	/***
//	 * 현재 시간 스트링 반환 UTC 기준 
//	 * @return
//	 */
//	public String getLocaleNow() {
//		LocalDateTime date = LocalDateTime.now();
//		ZoneOffset offset = ZoneOffset.
//		return String.valueOf(date.toEpochSecond(ZoneOffset.systemDefault()));
//	}
	public boolean isToday(Date date) {
		return isSameDay(date, Calendar.getInstance().getTime());
	}
	
	/**
	 * <p>
	 * Checks if two dates are on the same day ignoring time.
	 * </p>
	 * 
	 * @param date1
	 *            the first date, not altered, not null
	 * @param date2
	 *            the second date, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 */
	public boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}/**
	 * <p>
	 * Checks if two calendars represent the same day ignoring time.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 */
	public boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	
	/**
	 * UTC 시간을 현재 locale 시간으로 변환 
	 * @param utcdateString
	 * @return
	 */
	public String string2LocaleDateString(String utcdateString)
	{
		long t = Long.parseLong(utcdateString);
		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
		
		return simpleDate.format(t);
	}
	/**
	 * locale 시간 문자열을 UTC 시간 문자열로 변경
	 * @param localeString yyyy-MM-dd hh:mm:ss
	 * @return
	 * @throws ParseException 
	 */
	public String localeString2UTCString(String localeString) throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss",Locale.getDefault());
		Date date = dateFormat.parse(localeString);
		long unixTime = (long)date.getTime();
		
		return String.valueOf(unixTime);
	}

	public boolean isToday(String utcTime)
	{
		String dateString = string2LocaleDateString(utcTime);
		return isToday(string2Date(dateString, "yyyy-MM-dd hh:mm:ss"));
	}
	
	public Date string2Date(String playdate, String pattern) {

		try {
			return new SimpleDateFormat(pattern).parse(playdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}

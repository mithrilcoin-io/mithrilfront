package io.mithrilcoin.front.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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

}

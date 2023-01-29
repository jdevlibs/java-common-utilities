/*  ---------------------------------------------------------------------------
 *  * Copyright 2020-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  ---------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils;

import java.util.*;

/**
 * Utility class for manage date
 * @author Supot Saelao
 * @version 1.0
 */
public final class DateUtils {
	public static final Locale US = Locale.US;
	public static final Locale TH = new Locale("th", "TH");
	public static final int THAI_DIFF_YEAR 		= 543;
	public static final int TOTAL_WEEK_IN_MONTH = 7;
	
	public static final String FM_DATE 			= "dd/MM/yyyy";
	public static final String FM_DATE_TIME 	= "dd/MM/yyyy HH:mm:ss";
	public static final String FM_TIME 			= "HH:mm:ss";
	public static final String FM_NAME_SHORT 	= "dd MMM yyyy";
    public static final String FM_NAME_FULL		= "dd MMMM yyyy";
    
	private static final Map<String, Locale> LOCALES;
	static {
		LOCALES = new HashMap<>(5);
		LOCALES.put("en_US", US);
		LOCALES.put("en_GB", Locale.UK);
		LOCALES.put("ja_JP", Locale.JAPAN);
		LOCALES.put("zh_CN", Locale.CHINA);
		LOCALES.put("th_TH", TH);
	}
	
	private DateUtils() {
	}

	public static Locale getLocale(String key) {
		if (Validators.isEmpty(key)) {
			return US;
		}
		
		return LOCALES.get(key);
	}

	public static java.sql.Date getSqlDate() {
		return new java.sql.Date(new Date().getTime());
	}

	public static java.sql.Timestamp getSqlTimestamp() {
		return new java.sql.Timestamp(new Date().getTime());
	}

	public static java.sql.Date sqlDate(Object value) {
		return sqlDate(value, FM_DATE, US);
	}

	public static java.sql.Date sqlDate(Object value, Locale locale) {
		return sqlDate(value, FM_DATE, locale);
	}

	public static java.sql.Date sqlDate(Object value, String pattern) {
		return sqlDate(value, pattern, US);
	}

	public static java.sql.Date sqlDate(Object value, String pattern, Locale locale) {
		Date date = DateFormats.date(value, pattern, locale);
		if (date != null) {
			return new java.sql.Date(date.getTime());
		}
		
		return null;
	}

	public static java.sql.Timestamp sqlTimestamp(Object value) {
		return sqlTimestamp(value, FM_DATE_TIME, US);
	}

	public static java.sql.Timestamp sqlTimestamp(Object value, Locale locale) {
		return sqlTimestamp(value, FM_DATE_TIME, locale);
	}

	public static java.sql.Timestamp sqlTimestamp(Object value, String pattern) {
		return sqlTimestamp(value, pattern, US);
	}

	public static java.sql.Timestamp sqlTimestamp(Object value, String pattern, Locale locale) {
		Date date = DateFormats.date(value, pattern, locale);
		if (date != null) {
			return new java.sql.Timestamp(date.getTime());
		}
		return null;
	}

	public static Date getDate() {
		return new Date();
	}

	public static int getYear() {
		return Calendar.getInstance(US).get(Calendar.YEAR);
	}

	public static int getYearThai() {
		return getYearThai(getDate());
	}

	public static int getYear(Date date) {
		if (Validators.isNull(date)) {
			return 0;
		}

		Calendar cal = Calendar.getInstance(US);
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static int getYearThai(Date date) {
		if (Validators.isNull(date)) {
			return 0;
		}

		Calendar cal = Calendar.getInstance(TH);
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}

	public static int getMonth(Date date) {
		if (date == null) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.MONTH) + 1;
	}
	
	public static int getDayOfMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static int getTotalDayOfMonth() {
		return getTotalDayOfMonth(getDate());
	}
	
	public static int getTotalDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	public static int getWeekOfMonth() {
		return getWeekOfMonth(getDate());
	}
	
	public static int getWeekOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		if (dayOfMonth <= TOTAL_WEEK_IN_MONTH) {
			return 1;
		} else if (dayOfMonth <= 14) {
			return 2;
		} else if (dayOfMonth <= 21) {
			return 3;
		} else {
			return 4;
		}
	}
	
	public static String getDayOfMonthName() {
		return getDayOfMonthName(getDate());
	}

	public static String getDayOfMonthName(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);
	}

	public static Date addSecond(Date date, int second) {
		return addDate(date, Calendar.SECOND, second);
	}
	
	public static Date addMinute(Date date, int minute) {
		return addDate(date, Calendar.MINUTE, minute);
	}

	public static Date addHour(Date date, int hour) {
		return addDate(date, Calendar.HOUR, hour);
	}

	public static Date addDay(Date date, int day) {
		return addDate(date, Calendar.DATE, day);
	}

	public static Date addWeek(Date date, int week) {
		return addDate(date, Calendar.DATE, (week * 7));
	}
	
	public static Date addMonth(Date date, int month) {
		return addDate(date, Calendar.MONTH, month);
	}

	public static Date addYear(Date date, int year) {
		return addDate(date, Calendar.YEAR, year);
	}
	
	public static Date trunc(Date date) {
		if (Validators.isNull(date)) {
			return null;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date max(Date date1, Date date2) {
		if (date1 == null) {
			return date2;
		}
		if (date2 == null) {
			return date1;
		}
		return (date1.after(date2)) ? date1 : date2;
	}

	public static Date min(Date date1, Date date2) {
		if (date1 == null) {
			return null;
		}
		if (date2 == null) {
			return null;
		}
		return (date1.before(date2)) ? date1 : date2;
	}
	
	private static Date addDate(Date date, int type, int value) {
		if (date == null || value == 0) {
			return date;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(type, value);
		return cal.getTime();
	}
}

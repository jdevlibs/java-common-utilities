/*  ---------------------------------------------------------------------------
 *  * Copyright 2019-2020 the original author or authors.
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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * @author supot
 * @version 1.0
 */
public final class DateFormats {
	private static final String EMPTY 		= "";
	private static final String FM_DATE 	= "dd/MM/yyyy";
	private static final String FM_DT_TIME 	= "dd/MM/yyyy HH:mm:ss";
	private static final Locale TH 			= new Locale("th", "TH");
	private static final DateTimeFormatter TIME_FM = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	private static final DateTimeFormatter[] FMT_DATES = {
			DateTimeFormatter.ofPattern(FM_DATE, Locale.US),
			DateTimeFormatter.ofPattern(FM_DT_TIME, Locale.US),
			DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss", Locale.US),
			DateTimeFormatter.ofPattern("dd/MM/yyyy'T'HH:mm:ss.SSS'Z'", Locale.US),
			DateTimeFormatter.ISO_LOCAL_DATE,
			DateTimeFormatter.ISO_DATE,
			DateTimeFormatter.ISO_OFFSET_DATE,  
			DateTimeFormatter.BASIC_ISO_DATE,
			DateTimeFormatter.ISO_LOCAL_DATE_TIME, 
			DateTimeFormatter.ISO_OFFSET_DATE_TIME,
			DateTimeFormatter.ISO_INSTANT, 
			DateTimeFormatter.ISO_ZONED_DATE_TIME, 
			DateTimeFormatter.ISO_DATE_TIME
	};

	private static final DateTimeFormatter[] FMT_TIMES = {DateTimeFormatter.ISO_LOCAL_TIME,
			DateTimeFormatter.ISO_OFFSET_TIME,
			DateTimeFormatter.ISO_TIME, TIME_FM};

	private DateFormats() {
	}
	
	/**
	 * Converts this input object to a LocalTime.
	 * @param value The input value to convert.
	 * @return LocalTime, If cannot convert return null
	 */
	public static LocalTime time(Object value) {
		if (value instanceof Timestamp) {
			return ((Timestamp) value).toLocalDateTime().toLocalTime();
		} else if (value instanceof Date) {
			return toTime((Date) value);
		} else if (value instanceof LocalTime) {
			return (LocalTime) value;
		} else if (value instanceof LocalDateTime) {
			return ((LocalDateTime) value).toLocalTime();
		} else if (value instanceof String) {
			return toTime((String) value);
		}

		return null;
	}
	
	/**
	 * Converts this input object to a LocalDate.
	 * @param value The input value to convert.
	 * @return LocalDate, If cannot convert return null
	 */
	public static LocalDate localDate(Object value) {
		return localDate(value, null);
	}
	
	/**
	 * Converts this input object to a LocalDate.
	 * @param value The input value to convert.
	 * @param pattern LocalDate format pattern.
	 * @return LocalDate, If cannot convert return null
	 */
	public static LocalDate localDate(Object value, String pattern) {
		if (value instanceof Timestamp) {
			return ((Timestamp) value).toLocalDateTime().toLocalDate();
		} else if (value instanceof java.sql.Date) {
			return ((java.sql.Date) value).toLocalDate();
		} else if (value instanceof Date) {
			return toLocalDate((Date) value);
		} else if (value instanceof LocalDate) {
			return (LocalDate) value;
		} else if (value instanceof LocalDateTime) {
			return ((LocalDateTime) value).toLocalDate();
		} else if (value instanceof String) {
			if (Validators.isNotEmpty(pattern)) {
				return toLocalDate((String) value, pattern);
			}
			return toLocalDate((String) value);
		}

		return null;
	}
	
	/**
	 * Converts this input object to a LocalDateTime.
	 * @param value The input value to convert.
	 * @return LocalDateTime, If cannot convert return null
	 */
	public static LocalDateTime localDateTime(Object value) {
		return localDateTime(value, null);
	}
	
	/**
	 * Converts this input object to a LocalDateTime.
	 * @param value The input value to convert.
	 * @param pattern LocalDateTime format pattern.
	 * @return  LocalDateTime, If cannot convert return null
	 */
	public static LocalDateTime localDateTime(Object value, String pattern) {
		if (value instanceof Timestamp) {
			return ((Timestamp) value).toLocalDateTime();
		} else if (value instanceof java.sql.Date) {
			return ((java.sql.Date) value).toLocalDate().atStartOfDay();
		} else if (value instanceof Date) {
			return toLocalDateTime((Date) value);
		} else if (value instanceof LocalDateTime) {
			return (LocalDateTime) value;
		} else if (value instanceof LocalDate) {
			return ((LocalDate) value).atStartOfDay();
		} else if (value instanceof String) {
			if (Validators.isNotEmpty(pattern)) {
				return toLocalDateTime((String) value, pattern);
			} else {
				return toLocalDateTime((String) value);
			}
		}
		
		return null;
	}

	/**
	 * Converts input object to a Date.(using pattern dd/MM/yyyy, Locale.US)
	 * @param value The input value to convert.
	 * @return Date, If program cannot convert return null
	 */
	public static Date date(Object value) {
		return date(value, FM_DATE, Locale.US);
	}
	
	/**
	 * Converts this input object to a Date. (Using Locale.US)
	 * @param value The input value to convert.
	 * @param pattern Date format pattern.
	 * @return Date, If program cannot convert return null
	 */
	public static Date date(Object value, String pattern) {
		return date(value, pattern, Locale.US);
	}

	/**
	 * Converts this input object to a Date. (using pattern dd/MM/yyyy)
	 * @param value The input value to convert.
	 * @param locale The locale whose date format symbols should be used
	 * @return Date, If program cannot convert return null
	 */
	public static Date date(Object value, Locale locale) {
		return date(value, FM_DATE, locale);
	}

	/**
	 * Converts this input object to a Date.
	 * @param value The input value to convert.
	 * @param pattern Date format pattern.
	 * @param locale The locale whose date format symbols should be used
	 * @return Date, If program cannot convert return null
	 */
	public static Date date(Object value, String pattern, Locale locale) {
		if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof LocalDateTime) {
			return toDate((LocalDateTime) value);
		} else if (value instanceof LocalDate) {
			return toDate((LocalDate) value);
		} else if (!(value instanceof String)) {
			return null;
		}
		
		try {
			SimpleDateFormat dfm = new SimpleDateFormat(pattern, locale);
			dfm.setLenient(false);
			return dfm.parse(value.toString());
		} catch (ParseException ex) {
			return null;
		}
	}
	
	/**
	 * Formats a Date into a date/time string. (Using pattern dd/MM/yyyy, Locale.US)
	 * @param date The Date value to be formatted into string.
	 * @return The formatted time string., If program cannot convert return empty
	 */
	public static String format(Date date) {
		return format(date, FM_DATE, Locale.US);
	}
	
	/**
	 * Formats a Date into a date/time string. (Using pattern dd/MM/yyyy, THAI locale)
	 * @param date The Date value to be formatted into string.
	 * @return The formatted time string., If program cannot convert return empty
	 */
	public static String formatThai(Date date) {
		return format(date, FM_DATE, DateUtils.TH);
	}
	
	/**
	 * Formats a Date into a date/time string. (Using Locale.US)
	 * @param date The Date value to be formatted into string.
	 * @param pattern The pattern describing the date and time format
	 * @return The formatted time string., If program cannot convert return empty
	 */
	public static String format(Date date, String pattern) {
		return format(date, pattern, Locale.US);
	}
	
	/**
	 * Formats a Date into a date/time string.
	 * @param date The Date value to be formatted into string.
	 * @param pattern The pattern describing the date and time format
	 * @param locale The locale whose date format symbols should be used
	 * @return The formatted time string., If program cannot convert return empty
	 */
	public static String format(Date date, String pattern, Locale locale) {
		try {
			if (Validators.isNull(date)) {
				return EMPTY;
			}
			
			DateFormat fmt = new SimpleDateFormat(pattern, locale);
			fmt.setLenient(false);
			return fmt.format(date);
		} catch (Exception ex) {
			return EMPTY;
		}
	}

	/**
	 * Formats a LocalDate into a date string. (Using Locale.US, pattern dd/MM/yyyy)
	 * @param date The LocalDate value to be formatted into string.
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDate date) {
		return format(date, FM_DATE, Locale.US);
	}
	
	/**
	 * Formats a LocalDate into a date/time string. (Using pattern dd/MM/yyyy, THAI locale)
	 * @param date The LocalDate value to be formatted into string.
	 * @return The formatted time string., If program cannot convert return empty
	 */
	public static String formatThai(LocalDate date) {
		return format(date, FM_DATE, DateUtils.TH);
	}
	
	/**
	 * Formats a LocalDate into a date string. (Using Locale.US)
	 * @param date The LocalDate value to be formatted into string.
	 * @param pattern The pattern describing the date format
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDate date, String pattern) {
		return format(date, pattern, Locale.US);
	}
	
	/**
	 * Formats a LocalDate into a date string. (Using pattern dd/MM/yyyy)
	 * @param date The LocalDate value to be formatted into string.
	 * @param locale The locale whose date format symbols should be used
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDate date, Locale locale) {
		return format(date, FM_DATE, locale);
	}
	
	/**
	 * Formats a LocalDate into a date string.
	 * @param date The LocalDate value to be formatted into string.
	 * @param pattern The pattern describing the date format
	 * @param locale The locale whose date format symbols should be used
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDate date, String pattern, Locale locale) {
		try {
			if (Validators.isNull(date)) {
				return EMPTY;
			}
			if (TH.equals(locale)) {
				Date dt = toDate(date);
				return format(dt, pattern, locale);
			}
			return DateTimeFormatter.ofPattern(pattern, locale).format(date);
		} catch (IllegalArgumentException ex) {
			return EMPTY;
		}
	}
	
	/**
	 * Formats a LocalDateTime into a date string. (Using Locale.US, pattern dd/MM/yyyy HH:mm:ss)
	 * @param date The LocalDateTime value to be formatted into string.
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDateTime date) {
		return format(date, FM_DT_TIME, Locale.US);
	}
	
	/**
	 * Formats a LocalDateTime into a date/time string. (Using pattern dd/MM/yyyy, THAI locale)
	 * @param date The LocalDate value to be formatted into string.
	 * @return The formatted time string., If program cannot convert return empty
	 */
	public static String formatThai(LocalDateTime date) {
		return format(date, FM_DT_TIME, DateUtils.TH);
	}
	
	/**
	 * Formats a LocalDateTime into a date string. (Using Locale.US)
	 * @param date The LocalDateTime value to be formatted into string.
	 * @param pattern The pattern describing the date format
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDateTime date, String pattern) {
		return format(date, pattern, Locale.US);
	}
	
	/**
	 * Formats a LocalDateTime into a date string. (Using pattern dd/MM/yyyy HH:mm:ss)
	 * @param date The LocalDateTime value to be formatted into string.
	 * @param locale The locale whose date format symbols should be used
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDateTime date, Locale locale) {
		return format(date, FM_DT_TIME, locale);
	}
	
	/**
	 * Formats a LocalDateTime into a date string.
	 * @param dateTime The LocalDateTime value to be formatted into string.
	 * @param pattern The pattern describing the date format
	 * @param locale The locale whose date format symbols should be used
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalDateTime dateTime, String pattern, Locale locale) {
		try {
			if (Validators.isNull(dateTime)) {
				return EMPTY;
			}

			if (TH.equals(locale)) {
				Date date = toDate(dateTime);
				return format(date, pattern, locale);
			}
			return DateTimeFormatter.ofPattern(pattern, locale).format(dateTime);
		} catch (Exception ex) {
			return EMPTY;
		}
	}
	
	/**
	 * Formats a LocalTime into a time string.
	 * @param time The LocalTime value to be formatted into string.
	 * @return The formatted string., If program cannot convert return empty
	 */
	public static String format(LocalTime time) {
		if (Validators.isNull(time)) {
			return EMPTY;
		}
		return TIME_FM.format(time);
	}
	
	public static String formatTime(LocalTime time, String pattern) {
		try {
			if (Validators.isNull(time)) {
				return EMPTY;
			}
			return DateTimeFormatter.ofPattern(pattern).format(time);
		} catch (Exception ex) {
			return EMPTY;
		}
	}

	private static LocalDateTime toLocalDateTime(Date value) {
		return Instant.ofEpochMilli(value.getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
	
	private static LocalDateTime toLocalDateTime(String value) {
		if (Validators.isEmpty(value)) {
			return null;
		}
		
		LocalDateTime dateTime = null;
		for (DateTimeFormatter fmt : FMT_DATES) {
			try {
				dateTime = LocalDateTime.parse(value, fmt);
			} catch (DateTimeParseException ex) {
				//Ignore
			}
			if (dateTime != null) {
				return dateTime;
			}
		}

		return null;
	}
	
	private static LocalDateTime toLocalDateTime(String value, String pattern) {
        try {
            return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException ex) {
        	return null;
        }
	}
	
	private static LocalDate toLocalDate(Date value) {
		return Instant.ofEpochMilli(value.getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}
	
	private static LocalDate toLocalDate(String value) {
		if (Validators.isEmpty(value)) {
			return null;
		}

		LocalDate date = null;
		for (DateTimeFormatter fmt : FMT_DATES) {
			try {
				date = LocalDate.parse(value, fmt);
			} catch (DateTimeParseException ex) {
				// Ignore
			}

			if (date != null) {
				return date;
			}
		}

		return null;
	}
	
	private static LocalDate toLocalDate(String value, String pattern) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
        } catch (DateTimeParseException ex) {
        	return null;
        }
	}
	
	private static LocalTime toTime(Date value) {
		return Instant.ofEpochMilli(value.getTime())
				.atZone(ZoneId.systemDefault()).toLocalTime();
	}
	
	private static LocalTime toTime(String value) {
		if (Validators.isEmpty(value)) {
			return null;
		}
		
		LocalTime time = null;
		for (DateTimeFormatter fmt : FMT_TIMES) {
			try {
				time = LocalTime.parse(value, fmt);
			} catch (DateTimeParseException ex) {
				//Ignore
			}

			if (time != null) {
				return time;
			}
		}
		return null;
	}
	
	private static Date toDate(LocalDateTime value) {
		return Date.from(value.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	private static Date toDate(LocalDate value) {
		return Date.from(value.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static final class CustomDateFormat extends SimpleDateFormat {
		private static final long serialVersionUID = 1L;
		
		private final String pattern;

		public CustomDateFormat(String pattern) {
			super(pattern);
			this.pattern = pattern;
		}

		public String getPattern() {
			return pattern;
		}

		public int lengthPattern() {
			return pattern.length();
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return super.equals(obj);
		}
		
	}
}

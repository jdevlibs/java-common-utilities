/*
 * ----------------------------------------------------------------------------
 * Copyright © 2020 by Cana enterprise co,.Ltd. All rights reserved.
 * ----------------------------------------------------------------------------
 */
package io.github.jdevlibs.utils.fotmat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 * The class for a parse/format Date object
 * @author Supot Saelao
 * @version 1.0
 */
public class FormatDate {
	private static final String DF = "dd/MM/yyyy";
	
    private DateTimeFormatter format;
    private DateFormat fmt;
    
    public FormatDate() {
        this(DF, Locale.US);
    }
    
    public FormatDate(String pattern) {
        this(pattern, Locale.US);
    }
    
    public FormatDate(Locale locale) {
        this(DF, locale);
    }
    
    public FormatDate(String pattern, Locale locale) {
    	this.createFormat(pattern, locale);
    }
    
    /**
     * Convert String value to java.util.Date
     * @param value The string value of date
     * @return Date object, if program cannot convert return null
     */
	public Date parse(String value) {
		try {
			if (value == null || value.isEmpty()) {
				return null;
			}
			return fmt.parse(value);
		} catch (ParseException ex) {
			return null;
		}
	}
    
	/**
	 * Convert String value to java.time.LocalDate
	 * @param value The string value of date
	 * @return LocalDate, if cannot convert return null
	 */
    public LocalDate localDate(String value) {
        try {
            return LocalDate.parse(value, format);
        } catch (DateTimeParseException ex) {
        	return null;
        }
    }
    
	/**
	 * Convert String value to java.time.LocalDateTime
	 * @param value The string value of date
	 * @return LocalDate, if cannot convert return null
	 */
    public LocalDateTime localDateTime(String value) {
        try {
            return LocalDateTime.parse(value, format);
        } catch (DateTimeParseException ex) {
        	return null;
        }
    }
    
    /**
     * Format Date object to String with format
     * @param date Input java.util.Date object
     * @return Date as String, If program cannot format return empty
     */
	public String format(Date date) {
		try {
			if (date == null) {
				return "";
			}
			return fmt.format(date);
		} catch (Exception ex) {
			return "";
		}		
	}
    
    /**
     * Format Date object to String with format
     * @param date Input java.time.LocalDate object
     * @return Date as String, If program cannot format return empty
     */
    public String format(LocalDate date) {
        try {
            if (date == null) {
                return "";
            }
            return format.format(date);
        } catch (DateTimeException ex) {
            return "";
        }
    }
    
    /**
     * Format Date object to String with format
     * @param date Input java.time.LocalDateTime object
     * @return Date as String, If program cannot format return empty
     */
    public String format(LocalDateTime date) {
        try {
            if (date == null) {
                return "";
            }
            return format.format(date);
        } catch (DateTimeException ex) {
            return "";
        }
    }
    
    private void createFormat(String pattern, Locale locale) {
        if (pattern == null || pattern.isEmpty()) {
            pattern = DF;
        }
        
        if (locale == null) {
            locale = Locale.US;
        }
        
        format = DateTimeFormatter.ofPattern(pattern).withLocale(locale);
        fmt = new SimpleDateFormat(pattern, locale);
        fmt.setLenient(false);
    }
}

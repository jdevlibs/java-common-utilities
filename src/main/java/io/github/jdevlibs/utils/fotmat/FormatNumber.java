/*
* ----------------------------------------------------------------------------
* Copyright © 2020 by Cana enterprise co,.Ltd. All rights reserved.
* ----------------------------------------------------------------------------
*/
package io.github.jdevlibs.utils.fotmat;

import java.text.DecimalFormat;
import java.text.ParseException;

import io.github.jdevlibs.utils.Validators;

/**
 * The class for a parse/format Number object
 * @author Supot Saelao
 * @version 1.0
 */
public class FormatNumber {
	private final DecimalFormat format;
	
	public FormatNumber() {
		this("#,##0.00");
	}
	
	public FormatNumber(String pattern) {
		if (Validators.isEmpty(pattern)) {
			format = new DecimalFormat("#,##0.00");
		} else {
			format = new DecimalFormat(pattern);
		}
	}
	
    /**
     * Format number value to String object
     * @param value The abstract class Number 
     * @return String object, if program cannot convert return empty
     */
	public String format(Number value) {
		if (Validators.isNull(value)) {
			return "";
		}
		
		try {
			return format.format(value);
		} catch (IllegalArgumentException ex) {
			return "";
		}
	}
	
    /**
     * Convert String to Number
     * @param value The value of Number in String format
     * @return Number, If program cannot convert return null
     */
    public Number parse(String value) {
		if (Validators.isEmpty(value)) {
    		return null;
    	}
    	
        try {
        	return format.parse(value);
        } catch (ParseException ex) {
        	return null;
        }
    }
}

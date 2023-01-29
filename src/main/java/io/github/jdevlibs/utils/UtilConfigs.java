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

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Utilities class for manage configuration on common-utils.properties
 * @author Supot Saelao
 * @version 1.0
 */
public class UtilConfigs {
	private static final String FILE		= "common-utils";
	private static final String SPLIT		= ",";
	private static final PropertyResourceBundle prop;
	
	static {
		prop = (PropertyResourceBundle) ResourceBundle.getBundle(FILE, Locale.US);
	}

	private UtilConfigs() {}
	
	public static String[] getArray(PropertyKeys key){
		return getArray(key, SPLIT);
	}
	
	public static String[] getArray(PropertyKeys key, String split) {
		if (Validators.isNull(key)) {
			return new String[] {};
		}
		
		String value = getValue(key);
		if(Validators.isEmpty(value)){
			return new String[] {};
		}else{
			return value.split("["+split+"]");
		}
	}

	public static boolean getBoolValue(PropertyKeys key) {
		return getBoolValue(key, false);
	}

	public static boolean getBoolValue(PropertyKeys key, Boolean defVal) {
		String val = getValue(key);
		if (Validators.isEmpty(val)) {
			return defVal;
		}
		
		try {
			return Boolean.parseBoolean(val);
		} catch (Exception ex) {
			//Ignore error
		}
		
		return defVal;
	}
	
	public static Integer getInteger(PropertyKeys key) {
		return getInteger(key, 0);
	}
	
	public static Integer getInteger(PropertyKeys key, Integer defVal) {
		String val = getValue(key);
		if (Validators.isEmpty(val)) {
			return defVal;
		}
		
		try {
			return Integer.valueOf(val);
		} catch (Exception ex){
			//Ignore error
		}
		
		return defVal;
	}
	
	public static String getValue(PropertyKeys key) {
		return getValue(key, "");
	}

	public static String getValue(PropertyKeys key, String defVal) {
		if (Validators.isNull(key) || Validators.isNull(prop)) {
			return defVal;
		}

		return prop.getString(key.getKey());
	}
	
	public enum PropertyKeys {
	    NUMBER_SCALE("thai.baht.number.scale"),
	    DIGIT_WORD("thai.baht.digit.word"),
	    ZERO("thai.baht.zero.word"),
	    NEGATIVE("thai.baht.negative"),
	    BAHT("thai.baht.baht"),
	    ALL("thai.baht.all"),
	    STANG("thai.baht.stang"),
	    YEE("thai.baht.yee"),
	    ONE_AED("thai.baht.one"),
	    THAI_CHAR("thai.character");
	    
		final String key;
	    PropertyKeys(String key) {
	        this.key = key;
	    }
	    
	    public String getKey() {
	        return this.key;
	    }
	}
}

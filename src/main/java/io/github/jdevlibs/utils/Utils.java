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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Utilities for general
 * 
 * @author Supot Saelao
 * @version 1.0
 */
public final class Utils {
	private static final Map<Character, String> TH_MAPS;

	static {
		String thChar = UtilConfigs.getValue(UtilConfigs.PropertyKeys.THAI_CHAR);
		TH_MAPS = new HashMap<>(thChar.length());
		char[] chs = thChar.toCharArray();
		for (Character ch : chs) {
			TH_MAPS.put(ch, null);
		}
	}

	private Utils() {
	}

	public static String padLeft(int val, int len, String pad) {
		return padLeft(String.valueOf(val), len, pad);
	}

	public static String padLeft(String val, int len, String pad) {
		if (Validators.isEmpty(val)) {
			return val;
		}
		if (val.length() >= len) {
			return val;
		}

		StringBuilder sb = new StringBuilder(len);
		int size = val.length();
		for (int i = 0; i < len - size; i++) {
			sb.append(pad);
		}
		sb.append(val);

		return sb.toString();
	}

	public static String padRight(int val, int len, String pad) {
		return padRight(String.valueOf(val), len, pad);
	}

	public static String padRight(String val, int len, String pad) {
		if (Validators.isEmpty(val)) {
			return val;
		}
		if (val.length() >= len) {
			return val;
		}

		StringBuilder sb = new StringBuilder(len);
		sb.append(val);

		int size = val.length();
		for (int i = 0; i < len - size; i++) {
			sb.append(pad);
		}

		return sb.toString();
	}

	public static int length(String value) {
		if (value == null) {
			return 0;
		}

		return value.length();
	}

	public static int lengthWithThai(String value) {
		if (value == null || value.isEmpty()) {
			return 0;
		}

		char[] chs = value.toCharArray();
		int count = 0;
		for (Character ch : chs) {
			if (TH_MAPS.containsKey(ch)) {
				continue;
			}
			count++;
		}

		return count;
	}

	public static String trim(String value) {
		if (Validators.isNull(value)) {
			return Values.EMPTY;
		}

		return value.trim();
	}

	public static String trimWhiteSpace(String value) {
		if (Validators.isNull(value)) {
			return Values.EMPTY;
		}
		return value.replaceAll("\\s","");
	}
	
	public static String removeComma(String value) {
		if (Validators.isEmpty(value)) {
			return value;
		}

		return value.replace(",", "");
	}

	public static String[] splitByLine(String value) {
		if (Validators.isEmpty(value)) {
			return new String[] {};
		}

		return value.split(Values.REG_NEW_LINE);
	}

	public static String[] splitByHtmlLine(String value) {
		if (Validators.isEmpty(value)) {
			return new String[] {};
		}

		return value.split(Values.NEW_LINE_HTML);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().toUpperCase();
	}

	public static String toTitleCase(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return Character.toTitleCase(str.charAt(0)) +
				str.substring(1);
	}

	public static String cleanupSpecialChar(String value) {
		return cleanupSpecialChar(value, "_");
	}

	public static String cleanupSpecialChar(String value, String replacement) {
		if (Validators.isEmpty(value)) {
			return value;
		}
		replacement = Validators.isNull(replacement) ? "" : replacement;
		return value.replaceAll("[^a-zA-Z0-9]", replacement);
	}
}

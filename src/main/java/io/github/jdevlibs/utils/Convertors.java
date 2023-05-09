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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Generic object converter
 * @author supot
 * @version 1.0
 */
public final class Convertors {
	private static final String BYTE 	= " bytes";
	private static final String KB 		= " KB";
	private static final String MB 		= " MB";
	private static final String GB 		= " GB";
	private static final String TB 		= " TB";

	private static final String FILE_ZERO_SIZE = "0 Bytes";
	private static final DecimalFormat DFM = new DecimalFormat("#,##0");

	private Convertors() {}

	/**
	 * Convert input value to boolean [0=true, 1=false], ['true', 'false'], If cannot convert return false.
	 * @param value The value for a convert.
	 * @return The convert value
	 */
	public static Boolean toBoolean(Object value) {
		return toBoolean(value, Boolean.FALSE);
	}

	/**
	 * Convert input value to boolean [1=true, 0=false], ['true', 'false'], If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Boolean toBoolean(Object value, Boolean defValue) {
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof String) {
			return Boolean.valueOf((String) value);
		}

		Number number = toNumber(value);
		if (number == null) {
			return defValue;
		}
		return (number.intValue() == 1 ? Boolean.TRUE : Boolean.FALSE);
	}

	/**
	 * Convert input value to Byte, If value cannot convert return 0.
	 * @param value The value
	 * @return The convert value
	 */
	public static Byte toByte(Object value) {
		return toByte(value, (byte) 0);
	}

	/**
	 * Convert input value to Byte, If cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Byte toByte(Object value, Byte defValue) {
		try {
			if (value instanceof Byte) {
				return (Byte) value;
			}
			if (value instanceof String) {
				return Byte.valueOf((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}

			return number.byteValue();
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to Short, If value cannot convert return 0.
	 * @param value The value
	 * @return The convert value
	 */
	public static Short toShort(Object value) {
		return toShort(value, (short) 0);
	}

	/**
	 * Convert input value to Short, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Short toShort(Object value, Short defValue) {
		try {
			if (value instanceof Short) {
				return (Short) value;
			}
			if (value instanceof String) {
				return Short.valueOf((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}

			return number.shortValue();
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to Integer, If value cannot convert return 0.
	 * @param value The value
	 * @return The convert value
	 */
	public static Integer toInteger(Object value) {
		return toInteger(value, 0);
	}

	/**
	 * Convert input value to Integer, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Integer toInteger(Object value, Integer defValue) {
		try {
			if (value instanceof Integer) {
				return (Integer) value;
			}
			if (value instanceof String) {
				return Integer.valueOf((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}

			return number.intValue();
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to Long, If value cannot convert return 0.
	 * @param value the value
	 * @return The convert value
	 */
	public static Long toLong(Object value) {
		return toLong(value, 0L);
	}

	/**
	 * Convert input value to Long, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Long toLong(Object value, Long defValue) {
		try {
			if (value instanceof Long) {
				return (Long) value;
			}
			if (value instanceof String) {
				return Long.valueOf((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}

			return number.longValue();
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to Double, If value cannot convert return 0.
	 * @param value The value
	 * @return The convert value
	 */
	public static Double toDouble(Object value) {
		return toDouble(value, 0D);
	}

	/**
	 * Convert input value to Double, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Double toDouble(Object value, Double defValue) {
		try {
			if (value instanceof Double) {
				return (Double) value;
			}
			if (value instanceof String) {
				return Double.valueOf((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}

			return number.doubleValue();
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to Float, If value cannot convert return 0.0
	 * @param value The value
	 * @return The convert value
	 */
	public static Float toFloat(Object value) {
		return toFloat(value, 0F);
	}

	/**
	 * Convert input value to Float, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static Float toFloat(Object value, Float defValue) {
		try {
			if (value instanceof Float) {
				return (Float) value;
			}
			if (value instanceof String) {
				return Float.valueOf((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}

			return number.floatValue();
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to BigInteger, If value cannot convert return 0.
	 * @param value The value
	 * @return The convert value
	 */
	public static BigInteger toBigInteger(Object value) {
		return toBigInteger(value, BigInteger.ZERO);
	}

	/**
	 * Convert input value to BigInteger, If value cannot convert return default value.
	 * @param value the value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static BigInteger toBigInteger(Object value, BigInteger defValue) {
		try {
			if (value instanceof BigInteger) {
				return (BigInteger) value;
			}
			if (value instanceof String) {
				return new BigInteger((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}
			return BigInteger.valueOf(number.longValue());
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to BigDecimal, If value cannot convert return 0.
	 * @param value The value
	 * @return The convert value
	 */
	public static BigDecimal toBigDecimal(Object value) {
		return toBigDecimal(value, BigDecimal.ZERO);
	}

	/**
	 * Convert input value to BigDecimal, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static BigDecimal toBigDecimal(Object value, BigDecimal defValue) {
		try {
			if (value instanceof BigDecimal) {
				return (BigDecimal) value;
			}
			if (value instanceof String) {
				return new BigDecimal((String) value);
			}

			Number number = toNumber(value);
			if (number == null) {
				return defValue;
			}
			return BigDecimal.valueOf(number.doubleValue());
		} catch (NumberFormatException ex) {
			return defValue;
		}
	}

	/**
	 * Convert input value to String
	 * @param value The value
	 * @return The convert value
	 */
	public static String toString(Object value) {
		return toString(value, null);
	}

	/**
	 * Convert input value to String, If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static String toString(Object value, String defValue) {
		if (value instanceof String) {
			return (String) value;
		}
		if (value instanceof byte[]) {
			byte[] data = (byte[]) value;
			return new String(data, StandardCharsets.UTF_8);
		}

		return value != null ? value.toString() : defValue;
	}

	/**
	 * Convert input value to byte[], If value cannot convert return zero byte[].
	 * @param value the value
	 * @return The convert value
	 */
	public static byte[] toBytes(Object value) {
		return toBytes(value, new byte[0]);
	}

	/**
	 * Convert input value to byte[], If value cannot convert return default value.
	 * @param value The value
	 * @param defValue Default value when cannot convert.
	 * @return The convert value
	 */
	public static byte[] toBytes(Object value, byte[] defValue) {
		if (Validators.isEmpty(value)) {
			return defValue;
		}
		try {
			if (value instanceof byte[]) {
				return (byte[]) value;
			}

			String strValue = toString(value);
			if (strValue == null) {
				return defValue;
			}
			return strValue.getBytes(StandardCharsets.UTF_8);
		} catch (Exception ex) {
			return defValue;
		}
	}

	/**
	 * Format file size to user readable. (e.g. 1024 to 1 KB)
	 * @param size File size
	 * @return The format of file size.
	 */
	public static String toFileSize(Number size) {
		if (Validators.isNull(size)) {
			return FILE_ZERO_SIZE;
		}

		long value = size.longValue();

		if (value < Values.ONE_KB) {
			return DFM.format(value) + BYTE;
		} else if(value >= Values.ONE_KB && value < Values.ONE_MB) {
			return DFM.format((value / Values.ONE_KB)) + KB;
		} else if(value >= Values.ONE_MB && value < Values.ONE_GB) {
			return DFM.format((value / Values.ONE_MB)) + MB;
		} else if(value >= Values.ONE_GB && value < Values.ONE_TB) {
			return DFM.format((value / Values.ONE_GB)) + GB;
		} else {
			return DFM.format((value / Values.ONE_TB)) + TB;
		}
	}

	/**
	 * Convert source value to Assignable target value
	 * @param clazzType Target class type
	 * @param value The source value
	 * @return The value
	 */
	public static Object convertWithType(Class<?> clazzType, Object value) {
		if (Validators.isNull(value)) {
			return null;
		}

		if (clazzType.equals(String.class)) {
			if (value instanceof Blob) {
				return toString(JdbcUtils.toByte((Blob) value));
			}
			return toString(value);
		} else if (clazzType.equals(BigDecimal.class)) {
			return toBigDecimal(value, null);
		} else if (clazzType.equals(BigInteger.class)) {
			return toBigInteger(value);
		} else if (clazzType.equals(Long.class) || clazzType.equals(long.class)) {
			return toLong(value);
		} else if (clazzType.equals(Integer.class) || clazzType.equals(int.class)) {
			return toInteger(value);
		} else if (clazzType.equals(Double.class) || clazzType.equals(double.class)) {
			return toDouble(value);
		} else if (clazzType.equals(Float.class) || clazzType.equals(float.class)) {
			return toFloat(value);
		} else if (clazzType.equals(Short.class) || clazzType.equals(short.class)) {
			return toShort(value);
		} else if (clazzType.equals(Byte.class) || clazzType.equals(byte.class)) {
			return toByte(value);
		} else {
			return convertWithOther(clazzType, value);
		}
	}

	private static Object convertWithOther(Class<?> clazzType, Object value) {
		if (clazzType.equals(LocalDateTime.class)) {
			return DateFormats.localDateTime(value);
		} else if (clazzType.equals(LocalDate.class)) {
			return DateFormats.localDate(value);
		} else if (clazzType.equals(LocalTime.class)) {
			return DateFormats.time(value);
		} else if (clazzType.equals(Byte[].class) || clazzType.equals(byte[].class)) {
			if (value instanceof Blob) {
				return JdbcUtils.toByte((Blob) value);
			}
			return toBytes(value);
		} else if (clazzType.equals(Boolean.class) || clazzType.equals(boolean.class)) {
			return toBoolean(value);
		} else {
			if (value instanceof Clob) {
				return JdbcUtils.readClob((Clob) value);
			} else if (value instanceof Blob) {
				return JdbcUtils.toByte((Blob) value);
			}
			return value;
		}
	}

	private static Number toNumber(Object value) {
		if (value instanceof Number) {
			return (Number) value;
		}

		return null;
	}
}

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
import java.util.List;

/**
 *
 * @author Supot Saelao
 * @version 1.0
 */
public final class CsvUtils {
    private static final String DEF_STR_VAL = "";

    private CsvUtils() {
    }

    public static String getValue(final String[] values, int inx) {
        return getValue(values, inx, 0, DEF_STR_VAL);
    }

    public static String getValue(final String[] values, int inx, int maxLen) {
        return getValue(values, inx, maxLen, DEF_STR_VAL);
    }

    public static String getValue(final String[] values, int inx, int maxLen, String defVal) {
        if (isEmpty(values) || inx < 0) {
            return defVal;
        }

        if (values.length <= inx) {
            return defVal;
        }

        String value = values[inx];
		if (maxLen <= 0 || value == null || value.length() < maxLen) {
            return value;
        } else {
            return value.substring(0, maxLen);
        }
    }

	public static int getInt(final String[] values, int inx) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return 0;
		}

		try {
			return Integer.parseInt(val);
		} catch (Exception ex) {
			return 0;
		}
	}

    public static Integer getInteger(final String[] values, int inx) {
        return getInteger(values, inx, null);
    }

	public static Integer getInteger(final String[] values, int inx, Integer defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return Integer.valueOf(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

    public static BigDecimal getBigDecimal(final String[] values, int inx) {
        return getBigDecimal(values, inx, null);
    }

    public static BigDecimal getBigDecimal(final String[] values, int inx, BigDecimal defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return new BigDecimal(val);
		} catch (Exception ex) {
			return defVal;
		}
    }

    public static BigInteger getBigInteger(final String[] values, int inx) {
        return getBigInteger(values, inx, null);
    }

	public static BigInteger getBigInteger(final String[] values, int inx, BigInteger defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return new BigInteger(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

    public static Double getDouble(final String[] values, int inx) {
        return getDouble(values, inx, null);
    }

	public static Double getDouble(final String[] values, int inx, Double defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return Double.valueOf(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

	public static String getValue(final List<String> values, int inx) {
		return getValue(values, inx, 0, DEF_STR_VAL);
	}

	public static String getValue(final List<String> values, int inx, int maxLen, String defVal) {
		if (isEmpty(values) || inx < 0) {
			return defVal;
		}

		if (values.size() <= inx) {
			return defVal;
		}

		String value = values.get(inx);
		if (maxLen <= 0 || value == null || value.length() < maxLen) {
			return value;
		} else {
			return value.substring(0, maxLen);
		}
	}

	public static int getInt(final List<String> values, int inx) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return 0;
		}

		try {
			return Integer.parseInt(val);
		} catch (Exception ex) {
			return 0;
		}
	}

	public static Integer getInteger(final List<String> values, int inx) {
		return getInteger(values, inx, null);
	}

	public static Integer getInteger(final List<String> values, int inx, Integer defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return Integer.valueOf(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

	public static BigDecimal getBigDecimal(final List<String> values, int inx) {
		return getBigDecimal(values, inx, null);
	}

	public static BigDecimal getBigDecimal(final List<String> values, int inx, BigDecimal defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return new BigDecimal(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

	public static BigInteger getBigInteger(final List<String> values, int inx) {
		return getBigInteger(values, inx, null);
	}

	public static BigInteger getBigInteger(final List<String> values, int inx, BigInteger defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return new BigInteger(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

	public static Double getDouble(final List<String> values, int inx) {
		return getDouble(values, inx, null);
	}

	public static Double getDouble(final List<String> values, int inx, Double defVal) {
		String val = getValue(values, inx, 0, DEF_STR_VAL);
		if (isEmpty(val)) {
			return defVal;
		}

		try {
			return Double.valueOf(val);
		} catch (Exception ex) {
			return defVal;
		}
	}

	private static boolean isEmpty(Object value) {
		return (value == null);
	}
	
	private static boolean isEmpty(final String[] values) {
		return (values == null || values.length == 0);
	}
}

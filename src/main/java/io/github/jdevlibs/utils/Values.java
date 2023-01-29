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

/**
 * Constant class for holding value
 * @author Supot Saelao
 * @version 1.0
 */
public final class Values {
	
	public static final BigDecimal ZERO    = BigDecimal.ZERO;
	public static final BigDecimal ONE     = BigDecimal.ONE;
	public static final BigDecimal TWO     = BigDecimal.valueOf(2D);
	public static final BigDecimal TREE    = BigDecimal.valueOf(3D);
	public static final BigDecimal FOUR    = BigDecimal.valueOf(4D);
	public static final BigDecimal FIVE    = BigDecimal.valueOf(5D);
	public static final BigDecimal SIX     = BigDecimal.valueOf(6D);
	public static final BigDecimal SEVEN   = BigDecimal.valueOf(7D);
	public static final BigDecimal EIGHT   = BigDecimal.valueOf(8D);
	public static final BigDecimal NINE    = BigDecimal.valueOf(9D);
	public static final BigDecimal TEN 	   = BigDecimal.TEN;	
	public static final BigDecimal ONE_HUNDRED     = BigDecimal.valueOf(100D);
	public static final BigDecimal ONE_THOUSAND    = BigDecimal.valueOf(1000D);
	public static final BigDecimal ONE_MILLION     = BigDecimal.valueOf(1000000D);
	
	public static final double ONE_KB 	= 1024D;
	public static final double ONE_MB 	= ONE_KB * ONE_KB;
	public static final double ONE_GB 	= ONE_KB * ONE_MB;
	public static final double ONE_TB 	= ONE_KB * ONE_GB;
	public static final double ONE_PB 	= ONE_KB * ONE_TB;
	public static final double ONE_EB 	= ONE_KB * ONE_PB;
	public static final double ONE_ZB 	= ONE_KB * ONE_EB;
	public static final double ONE_YB 	= ONE_KB * ONE_ZB;
	
	public static final byte ZERO_BYTE     = 0;
	public static final short ZERO_SHORT   = 0;
	public static final int ZERO_INT       = 0;
	public static final long ZERO_LONG     = 0L;
	public static final double ZERO_DOUBLE = 0D;
	public static final float ZERO_FLOAT   = 0F;
	
	public static final String EMPTY   = "";
	public static final String NEW_LINE_HTML = "<br/>";
	public static final String REG_NEW_LINE = "\\r?\\n";
	
	private Values(){}

    public static String defaultOf(String value) {
        if (value == null) {
            return EMPTY;
        }
        return value;
    }

	public static Integer defaultOf(Integer value) {
		if (value == null) {
			return ZERO_INT;
		}
		return value;
	}

	public static Long defaultOf(Long value) {
		if (value == null) {
			return ZERO_LONG;
		}
		return value;
	}

	public static Double defaultOf(Double value) {
		if (value == null) {
			return ZERO_DOUBLE;
		}
		return value;
	}

	public static Float defaultOf(Float value) {
		if (value == null) {
			return ZERO_FLOAT;
		}
		return value;
	}

	public static Short defaultOf(Short value) {
		if (value == null) {
			return ZERO_SHORT;
		}
		return value;
	}

	public static BigDecimal defaultOf(BigDecimal value) {
		if (value == null) {
			return ZERO;
		}
		return value;
	}

	public static BigInteger defaultOf(BigInteger value) {
		if (value == null) {
			return BigInteger.ZERO;
		}
		return value;
	}
}

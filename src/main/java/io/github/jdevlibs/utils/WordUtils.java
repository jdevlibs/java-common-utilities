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
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Utility class for convert currency to human reading format
 * @author Supot Saelao
 * @version 1.0
 */
public final class WordUtils {
	private WordUtils() {}
	
	private static final String[] NUMBER_SCALES = UtilConfigs.getArray(UtilConfigs.PropertyKeys.NUMBER_SCALE);
	private static final String[] DIGIT_WORDS = UtilConfigs.getArray(UtilConfigs.PropertyKeys.DIGIT_WORD);
	private static final String TXT_YEE = UtilConfigs.getValue(UtilConfigs.PropertyKeys.YEE);
	private static final String TXT_ZERO = UtilConfigs.getValue(UtilConfigs.PropertyKeys.ZERO);
	private static final String TXT_NEGATIVE = UtilConfigs.getValue(UtilConfigs.PropertyKeys.NEGATIVE);
	private static final String TXT_BAHT = UtilConfigs.getValue(UtilConfigs.PropertyKeys.BAHT);
	private static final String TXT_ALL = UtilConfigs.getValue(UtilConfigs.PropertyKeys.ALL);
	private static final String TXT_STANG = UtilConfigs.getValue(UtilConfigs.PropertyKeys.STANG);
	private static final String ONE_AED = UtilConfigs.getValue(UtilConfigs.PropertyKeys.ONE_AED);
	
	private static final String[] TEN_NAMES = { "", " ten", " twenty", " thirty", " forty", " fifty", " sixty",
			" seventy", " eighty", " ninety" };
	private static final String[] NUMBERS = { "", " one", " two", " three", " four", " five", " six", " seven",
			" eight", " nine", " ten", " eleven", " twelve", " thirteen", " fourteen", " fifteen", " sixteen",
			" seventeen", " eighteen", " nineteen" };
	private static final String MASK 	= "000000000000";
	
	/**
	 * Convert currency amount to Thai readable format
	 * @param value The currency amount (Set total scale with 2 position : 100.356 will be to 100.36)
	 * @return The value after format to thai baht
	 */
	public static String toThaiBaht(Number value) {
		if (value == null) {
			return TXT_ZERO;
		}
		
		return toThaiBaht(BigDecimal.valueOf(value.doubleValue()));
	}
	
	private static String toThaiBaht(BigDecimal amount) {
		if (amount == null || BigDecimal.ZERO.compareTo(amount) == 0) {
			return TXT_ZERO;
		}
		
		StringBuilder sb = new StringBuilder(128);
		BigDecimal absolute = amount.abs();
		int precision = absolute.precision();
		int scale = absolute.scale();
		int roundedPrecision = ((precision - scale) + 2);
		MathContext mc = new MathContext(roundedPrecision, RoundingMode.HALF_UP);
		BigDecimal rounded = absolute.round(mc);
		BigDecimal[] compound = rounded.divideAndRemainder(BigDecimal.ONE);
		boolean negativeAmount = (amount.compareTo(BigDecimal.ZERO) < 0);

		compound[0] = compound[0].setScale(0, RoundingMode.UNNECESSARY);
		compound[1] = compound[1].movePointRight(2);

		if (negativeAmount) {
			sb.append(TXT_NEGATIVE);
		}

		if (compound[0].toBigIntegerExact().compareTo(BigInteger.ZERO) > 0) {
			sb.append(getNumberText(compound[0].toBigIntegerExact()));
			sb.append(TXT_BAHT);
		}

		if (0 == compound[1].compareTo(BigDecimal.ZERO)) {
			sb.append(TXT_ALL);
		} else {
			sb.append(getNumberText(compound[1].toBigIntegerExact()));
			sb.append(TXT_STANG);
		}

		return sb.toString();
	}

	private static String getNumberText(BigInteger number) {
		StringBuilder sb = new StringBuilder();
		char[] digits = number.toString().toCharArray();

		for (int index = digits.length; index > 0; --index) {
			int digit = Integer.parseInt(String.valueOf(digits[digits.length- index]));
			String digitText = DIGIT_WORDS[digit];
			int x = ((index - 1) % 6);
			int scaleIdx = ((index > 1) ?  x : 6);

			if ((1 == scaleIdx) && (2 == digit)) {
				digitText = TXT_YEE;
			}

			if (1 == digit) {
				checkScale(sb, digits, scaleIdx, index, digitText);
			} else if (0 == digit) {
				if (0 == scaleIdx) {
					sb.append(getValue(scaleIdx));
				}
				continue;
			} else {
				sb.append(digitText);
			}

			sb.append(getValue(scaleIdx));
		}

		return sb.toString();
	}
	
	private static void checkScale(StringBuilder sb, char[] digits, int scaleIdx, int index, String digitText) {
		switch (scaleIdx) {
		case 0:
		case 6:
			if ((index < digits.length)) {
				int preInx = digits.length - (index + 1);
				if('0' == digits[preInx]) {
					sb.append(digitText);
				} else {
					sb.append(ONE_AED);
				}
			} else {
				sb.append(digitText);	
			}
			break;
		case 1:
			break;
		default:
			sb.append(digitText);
			break;
		}
	}
	
    private static String getValue(int inx) {
        if (NUMBER_SCALES.length == 0 || inx < 0) {
            return Values.EMPTY;
        }

        if (NUMBER_SCALES.length <= inx) {
            return Values.EMPTY;
        }
        
        return NUMBER_SCALES[inx];
    }
    
    public static String toEnglish(Number number) {
		if (Validators.isNull(number)) {
    		return "";
    	}
		
		BigDecimal amount = Calculators.round(BigDecimal.valueOf(number.doubleValue()), 2);
		long dollars = (long) Math.floor(amount.doubleValue());
		
		String value = convertToEnglish(dollars);
		double cent = amount.subtract(BigDecimal.valueOf(dollars)).multiply(Values.ONE_HUNDRED).doubleValue();
		if (cent > 0) {
			value += " baht and " + convertToEnglish(cent) + " satang";
		} else {
			value += " baht";
		}
		
		return value;
    }
    
	private static String convertToEnglish(double number) {
		if (number == 0) {
			return "zero";
		}

		DecimalFormat df = new DecimalFormat(MASK);
		String numberStr = df.format(number);

		int billions = Integer.parseInt(numberStr.substring(0, 3));
		int millions = Integer.parseInt(numberStr.substring(3, 6));
		int hundredThousands = Integer.parseInt(numberStr.substring(6, 9));
		int thousands = Integer.parseInt(numberStr.substring(9, 12));

		String tradBillions;
		if (billions == 0) {
			tradBillions = "";
		} else {
			tradBillions = convertLessThanOneThousand(billions) + " billion ";
		}
		
		StringBuilder result = new StringBuilder(tradBillions);
		if (millions != 0) {
			result.append(convertLessThanOneThousand(millions));
			result.append(" million ");
		}

		switch (hundredThousands) {
		case 0:
			break;
		case 1:
			result.append("one thousand ");
			break;
		default:
			result.append(convertLessThanOneThousand(hundredThousands));
			result.append(" thousand ");
		}

		result.append(convertLessThanOneThousand(thousands));

		// remove extra spaces!
		return result.toString().replaceAll("^\\s+", "").replaceAll("\\b\\s{2,}\\b", " ");
	}

	private static String convertLessThanOneThousand(int number) {
		String soFar;
		if (number % 100 < 20) {
			soFar = NUMBERS[number % 100];
			number /= 100;
		} else {
			soFar = NUMBERS[number % 10];
			number /= 10;
			soFar = TEN_NAMES[number % 10] + soFar;
			number /= 10;
		}
		if (number == 0) {
			return soFar;
		}
		return NUMBERS[number] + " hundred" + soFar;
	}
}

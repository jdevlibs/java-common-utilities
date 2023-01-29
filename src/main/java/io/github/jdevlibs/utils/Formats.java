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

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author supot
 * @version 1.0
 */
public final class Formats {
	private static final DecimalFormat DFM = new DecimalFormat("#,##0.00");
	private static final DecimalFormat DF_NO_RD = new DecimalFormat("#,##0.00");
	private static final DecimalFormat NFM = new DecimalFormat("###0");
	
	static {
		DF_NO_RD.setRoundingMode(RoundingMode.FLOOR);
	}
	
	private Formats() {}

	public static String withoutRound(Number value) {
		try {
			if (value == null) {
				return "";
			}
			
			double data = value.doubleValue();
			if (Math.abs(data) == data) {
				return DF_NO_RD.format(data);
			} else {
				data *= -1;
				return "-" + DF_NO_RD.format(data);
			}
		} catch (Exception ex) {
			return "";
		}
	}
	
	public static String decimal(Number value) {
		try {
			if (value == null) {
				return null;
			}			
			return DFM.format(value.doubleValue());
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static String number(Number value) {
		try {
			if (value == null) {
				return null;
			}
			return NFM.format(value);
		} catch (Exception ex) {
			return null;
		}
	}
}

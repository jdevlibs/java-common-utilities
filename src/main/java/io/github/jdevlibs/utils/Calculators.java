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
import java.math.RoundingMode;


/**
 * Utility class for calculation data
 * @author Supot Saelao
 * @version 1.0
 */
public final class Calculators {
    private static final int SCALE_2  = 2;

    private Calculators() {}

    public static BigDecimal roundDown(BigDecimal value) {
        return divide(value, Values.ONE, RoundingMode.DOWN);
    }

    public static BigDecimal roundDown(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.DOWN);
    }

    public static BigDecimal roundUp(BigDecimal value) {
    	return divide(value, Values.ONE, RoundingMode.UP);
    }

    public static BigDecimal roundUp(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.UP);
    }

    public static BigDecimal round(BigDecimal value) {
    	return divide(value, Values.ONE, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(BigDecimal value, int scale) {
        return round(value, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal round(BigDecimal value, int scale, RoundingMode mode) {
        if (Validators.isNull(value)) {
            return null;
        }

		if (scale < 0) {
        	return value;
        }

        return value.setScale(scale, mode);
    }

    /**
     * Calculate an amount of percent from total amount
     * @param value The total of amount
     * @param percent The percent value
     * @return An amount of percent
     */
    public static BigDecimal amountOfPercent(BigDecimal value, BigDecimal percent) {
        return amountOfPercent(value, percent, SCALE_2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate an amount of percent from total amount
     * @param value The total of amount
     * @param percent The percent value
     * @param scale Rounding Scale
     * @return An amount of percent
     */
    public static BigDecimal amountOfPercent(BigDecimal value, BigDecimal percent, int scale) {
        return amountOfPercent(value, percent, scale, RoundingMode.HALF_UP);
    }

    /**
     * Calculate an amount of percent from total amount
     * @param value The total of amount
     * @param percent The percent value
     * @param scale Rounding Scale
     * @param mode Rounding mode {@link RoundingMode}
     * @return An amount of percent
     */
    public static BigDecimal amountOfPercent(BigDecimal value, BigDecimal percent, int scale,
            RoundingMode mode) {

        if (Validators.isNull(value)) {
            value = BigDecimal.ZERO;
        }
        if (Validators.isNull(percent)) {
            percent = BigDecimal.ZERO;
        }
        value = value.multiply(percent);

        return divide(value, Values.ONE_HUNDRED, scale, mode);
    }

    /**
     * Calculate percent value from total amount
     * @param totalAmt The total amount
     * @param amt The amount for calculate percent
     * @return An percent value
     */
    public static BigDecimal percent(BigDecimal totalAmt, BigDecimal amt) {
    	return percent(totalAmt, amt, SCALE_2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate percent value from total amount
     * @param totalAmt The total amount
     * @param amt The amount for calculate percent
     * @param scale Rounding scale
     * @return An percent value
     */
    public static BigDecimal percent(BigDecimal totalAmt, BigDecimal amt, int scale) {
    	return percent(totalAmt, amt, scale, RoundingMode.HALF_UP);
    }

    /**
     * Calculate percent value from total amount
     * @param totalAmt The total amount
     * @param amt The amount for calculate percent
     * @param scale Rounding scale
     * @param mode mode Rounding mode {@link RoundingMode}
     * @return An percent value
     */
    public static BigDecimal percent(BigDecimal totalAmt, BigDecimal amt, int scale,
            RoundingMode mode) {
        if (Validators.isNullOne(totalAmt, amt) || Validators.isZero(totalAmt)) {
            return BigDecimal.ZERO;
        }
		if (Validators.isNull(amt)) {
			amt = BigDecimal.ZERO;
		}
		amt = amt.multiply(Values.ONE_HUNDRED);
		return divide(amt, totalAmt, scale, mode);
	}

    public static BigDecimal add(BigDecimal value, Number add) {
        if (Validators.isNullAll(value, add)) {
            return BigDecimal.ZERO;
		}

		value = Validators.isNull(value) ? BigDecimal.ZERO : value;
        return value.add(Convertors.toBigDecimal(add, BigDecimal.ZERO));
    }

    public static BigDecimal subtract(BigDecimal value, Number sub) {
        if (Validators.isNullAll(value, sub)) {
            return BigDecimal.ZERO;
        }

        value = (Validators.isNull(value) ? BigDecimal.ZERO : value);
        return value.subtract(Convertors.toBigDecimal(sub, BigDecimal.ZERO));
    }

    public static BigDecimal divide(BigDecimal value, Number divisor) {
        return divide(value, divisor, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal value, Number divisor, RoundingMode mode) {
		if (Validators.isNullOne(value, divisor)) {
			return BigDecimal.ZERO;
		}

		try {
			return value.divide(Convertors.toBigDecimal(divisor, BigDecimal.ZERO), mode);
		} catch (ArithmeticException ex) {
			return BigDecimal.ZERO;
		}
    }

    public static BigDecimal divide(BigDecimal value, Number divisor, int scale) {
        return divide(value, divisor, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal value, Number divisor, int scale, RoundingMode mode) {
		if (Validators.isNullOne(value, divisor)) {
			return BigDecimal.ZERO;
		}

		try {
			return value.divide(Convertors.toBigDecimal(divisor, BigDecimal.ZERO), scale, mode);
		} catch (ArithmeticException ex) {
			return BigDecimal.ZERO;
		}
	}

    public static BigDecimal multiply(BigDecimal value, Number multiply) {
        if (Validators.isNullAll(value, multiply)) {
            return BigDecimal.ZERO;
        }

        value = (Validators.isNull(value) ? BigDecimal.ZERO : value);
        return value.multiply(Convertors.toBigDecimal(multiply, BigDecimal.ZERO));
    }

	public static BigDecimal negative(BigDecimal value) {
		if (Validators.isNull(value)) {
			return null;
		}
		return value.multiply(BigDecimal.valueOf(-1D));
	}

	public static BigDecimal negative(Double value) {
		if (Validators.isNull(value)) {
			return null;
		}
		return BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(-1D));
	}

	public static BigDecimal abs(BigDecimal value) {
		if (Validators.isNull(value)) {
			return BigDecimal.ZERO;
		}
		return BigDecimal.valueOf(Math.abs(value.doubleValue()));
	}

	/**
	 * Calculate an amount with no VAT
	 * @param amount The value for calculate
	 * @param vatRate The VAT rate
	 * @return Vat amount information
	 */
	public static VatAmount calculateNoVat(BigDecimal amount, BigDecimal vatRate) {
		VatAmount valAmount = new VatAmount();
		valAmount.setAmount(amount);
		valAmount.setVatRate(vatRate);
		if (Validators.isNull(amount)) {
			return valAmount;
		}

		valAmount.setVatExcludeAmt(amount);
		valAmount.setVatIncludeAmt(amount);
		return valAmount;
	}

	/**
	 * Calculate an amount with including VAT
	 * @param amount The value for calculate
	 * @param vatRate The VAT rate
	 * @return Vat amount information
	 */
	public static VatAmount calculateIncludeVat(BigDecimal amount, BigDecimal vatRate) {
		VatAmount valAmount = new VatAmount();
		valAmount.setAmount(amount);
		valAmount.setVatRate(vatRate);

		if (Validators.isNull(amount)) {
			return valAmount;
		}

		// Vat = (amount * vatRate)/(100 + vatRate)
		BigDecimal vatAmt = multiply(amount, vatRate);
		vatAmt = divide(vatAmt, Values.ONE_HUNDRED.add(vatRate));

		// Exclude vatAmt = (Include vatAmt-vateAmt)
		BigDecimal excVatAmt = subtract(amount, vatAmt);

		valAmount.setVatAmt(vatAmt);
		valAmount.setVatIncludeAmt(amount);
		valAmount.setVatExcludeAmt(excVatAmt);

		return valAmount;
	}

	/**
	 * Calculate an amount with excluding VAT
	 * @param amount The value for calculate
	 * @param vatRate The VAT rate
	 * @return Vat amount information
	 */
	public static VatAmount calculateExcludeVat(BigDecimal amount, BigDecimal vatRate) {
		VatAmount valAmount = new VatAmount();
		valAmount.setAmount(amount);
		valAmount.setVatRate(vatRate);
		if (Validators.isNull(amount)) {
			return valAmount;
		}

		// Vat = (payAmt * vatRate)/100
		BigDecimal vatAmt = multiply(amount, vatRate);
		vatAmt = divide(vatAmt, Values.ONE_HUNDRED);

		// Include vatAmt = (payAmt + vatAmt)
		BigDecimal incVatAmt = add(amount, vatAmt);

		// Exclude vatAmt = Amount
		valAmount.setVatAmt(vatAmt);
		valAmount.setVatIncludeAmt(incVatAmt);
		valAmount.setVatExcludeAmt(amount);

		return valAmount;
	}

	/**
	 * The class-holding calculation of VAT
	 * @author Supot Sealao
	 */
	public static class VatAmount {
		private BigDecimal amount;
		private BigDecimal vatRate;
		private BigDecimal vatAmt;
		private BigDecimal vatIncludeAmt;
		private BigDecimal vatExcludeAmt;

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public BigDecimal getVatRate() {
			return vatRate;
		}

		public void setVatRate(BigDecimal vatRate) {
			this.vatRate = vatRate;
		}

		public BigDecimal getVatAmt() {
			if (vatAmt == null) {
				vatAmt = BigDecimal.ZERO;
			}
			return vatAmt;
		}

		public void setVatAmt(BigDecimal vatAmt) {
			this.vatAmt = vatAmt;
		}

		public BigDecimal getVatIncludeAmt() {
			if (vatIncludeAmt == null) {
				vatIncludeAmt = BigDecimal.ZERO;
			}
			return vatIncludeAmt;
		}

		public void setVatIncludeAmt(BigDecimal vatIncludeAmt) {
			this.vatIncludeAmt = vatIncludeAmt;
		}

		public BigDecimal getVatExcludeAmt() {
			if (vatExcludeAmt == null) {
				vatExcludeAmt = BigDecimal.ZERO;
			}
			return vatExcludeAmt;
		}

		public void setVatExcludeAmt(BigDecimal vatExcludeAmt) {
			this.vatExcludeAmt = vatExcludeAmt;
		}

		@Override
		public String toString() {
			return "VatAmount [amount=" + amount + ", vatRate=" + vatRate + ", vatAmt=" + vatAmt + ", vatIncludeAmt="
					+ vatIncludeAmt + ", vatExcludeAmt=" + vatExcludeAmt + "]";
		}

	}
}

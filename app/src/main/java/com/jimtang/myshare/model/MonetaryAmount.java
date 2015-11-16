package com.jimtang.myshare.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * A Parcelable convienience wrapper around BigDecimal for monetary numeric values.
 *
 * BigDecimal values represented retain their entire precision (notably, in mathematical operations)
 * except when:
 *
 * (a) In division, we retain precision up to a certain multiple of the currency default precision;
 *
 * (b) When outputting monetary values, the representation maintains the decimal places of the
 * currency precision;
 *
 * (c) Instantiation of a MonetaryAmount can involve either keeping input precision or rounding to
 * currency precision. When creating a MonetaryAmount out of the box, we should round to the
 * currency precision.
 *
 * Supported operations: addition/subtraction by another monetary amount; scaling by a factor;
 * dividing by another MonetaryAmount to produce a dimensionless ratio.
 *
 * Created by tangz on 10/18/2015.
 */
public class MonetaryAmount implements Parcelable, Comparable<MonetaryAmount> {

    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
    private static final int DECIMALS = 2;
    private static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(DECIMALS, ROUNDING_MODE);

    // keep 4x as many digits as we'll need to output.
    public static final int DIVISION_SCALE = DECIMALS * 4;

    public static final MonetaryAmount ZERO = new MonetaryAmount("0.0");

    private final BigDecimal amount;
    private final MathContext mathContext;

    public MonetaryAmount(BigDecimal bigDecimalValue) {
        this(bigDecimalValue, true);
    }

    public MonetaryAmount(BigDecimal bigDecimalValue, boolean round) {
        this.mathContext = DEFAULT_MATH_CONTEXT;
        this.amount = round ? bigDecimalValue.setScale(this.mathContext.getPrecision(),
                this.mathContext.getRoundingMode()) : bigDecimalValue;
    }

    public MonetaryAmount(String str) {
        this(new BigDecimal(str));
    }

    public MonetaryAmount add(MonetaryAmount other) {
        return new MonetaryAmount(this.amount.add(other.amount), false);
    }

    public MonetaryAmount subtract(MonetaryAmount other) {
        return new MonetaryAmount(this.amount.subtract(other.amount), false);
    }

    // It wouldn't make sense to multiple two monetary amounts (what would be the units?).
    // In that sense, this method scales this MonetaryAmount by a dimensionless number.
    public MonetaryAmount multiply(BigDecimal scalar) {
        return new MonetaryAmount(this.amount.multiply(scalar), false);
    }

    // Does the same thing as multiply(1/scalar)
    public MonetaryAmount divide(BigDecimal scalar) {
        return new MonetaryAmount(this.amount.divide(scalar, DIVISION_SCALE, RoundingMode.CEILING), false);
    }

    // currency units cancel out in the division operation
    public BigDecimal divide(MonetaryAmount other) {
        return this.amount.divide(other.amount, DIVISION_SCALE, RoundingMode.CEILING);
    }

    public BigDecimal toBigDecimal() {
        return amount.setScale(mathContext.getPrecision(), mathContext.getRoundingMode());
    }

    public double toDouble() {
        return toBigDecimal().doubleValue();
    }

    public String toFormattedString() {
        return NumberFormat.getCurrencyInstance().format(toDouble());
    }

    public String toNumericString() {
        return toBigDecimal().toPlainString();
    }

    public int hashCode() {
        return toBigDecimal().hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof MonetaryAmount)) {
            return false;
        }
        return this.toBigDecimal().compareTo(((MonetaryAmount)obj).toBigDecimal()) == 0;
    }

    //////////////////////////////////////
    // following methods for Parcelable //
    //////////////////////////////////////

    static final String AMOUNT_PARCELABLE_FLAG = "amount";
    static final String MATHCONTEXT_PARCELABLE_FLAG = "mathContext";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AMOUNT_PARCELABLE_FLAG, amount);
        bundle.putSerializable(MATHCONTEXT_PARCELABLE_FLAG, mathContext);
        dest.writeBundle(bundle);
    }

    protected MonetaryAmount(Parcel in) {
        Bundle bundle = in.readBundle();
        this.amount = (BigDecimal) bundle.getSerializable(AMOUNT_PARCELABLE_FLAG);
        this.mathContext = (MathContext) bundle.getSerializable(MATHCONTEXT_PARCELABLE_FLAG);
    }

    public static final Creator<MonetaryAmount> CREATOR = new Creator<MonetaryAmount>() {
        @Override
        public MonetaryAmount createFromParcel(Parcel in) {
            return new MonetaryAmount(in);
        }

        @Override
        public MonetaryAmount[] newArray(int size) {
            return new MonetaryAmount[size];
        }
    };

    @Override
    public int compareTo(MonetaryAmount another) {
        return this.toBigDecimal().compareTo(another.toBigDecimal());
    }
}

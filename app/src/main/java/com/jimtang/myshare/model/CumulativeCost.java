package com.jimtang.myshare.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tangz on 10/18/2015.
 */
public class CumulativeCost implements Parcelable {

    public static final CumulativeCost FREE = new CumulativeCost(MonetaryAmount.ZERO, MonetaryAmount.ZERO, MonetaryAmount.ZERO);

    private final MonetaryAmount subtotalPortion;
    private final MonetaryAmount taxPortion;
    private final MonetaryAmount tipPortion;

    public CumulativeCost(MonetaryAmount subtotalPortion, MonetaryAmount taxPortion, MonetaryAmount tipPortion) {
        this.subtotalPortion = subtotalPortion;
        this.taxPortion = taxPortion;
        this.tipPortion = tipPortion;
    }

    public MonetaryAmount getSubtotal() {
        return subtotalPortion;
    }

    public MonetaryAmount getTax() {
        return taxPortion;
    }

    public MonetaryAmount getTip() {
        return tipPortion;
    }

    public MonetaryAmount getTotal() {
        return subtotalPortion.add(taxPortion).add(tipPortion);
    }

    static final String SUBTOTAL_KEY = "subtotal";
    static final String TIP_KEY = "tip";
    static final String TAX_KEY = "tax";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SUBTOTAL_KEY, subtotalPortion);
        bundle.putParcelable(TIP_KEY, tipPortion);
        bundle.putParcelable(TAX_KEY, taxPortion);
        dest.writeBundle(bundle);
    }

    protected CumulativeCost(Parcel in) {
        Bundle bundle = in.readBundle(MonetaryAmount.class.getClassLoader());
        this.subtotalPortion = bundle.getParcelable(SUBTOTAL_KEY);
        this.taxPortion = bundle.getParcelable(TAX_KEY);
        this.tipPortion = bundle.getParcelable(TIP_KEY);
    }

    public static final Creator<CumulativeCost> CREATOR = new Creator<CumulativeCost>() {
        @Override
        public CumulativeCost createFromParcel(Parcel in) {
            return new CumulativeCost(in);
        }

        @Override
        public CumulativeCost[] newArray(int size) {
            return new CumulativeCost[size];
        }
    };
}

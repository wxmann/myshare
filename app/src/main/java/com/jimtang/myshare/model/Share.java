package com.jimtang.myshare.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tangz on 11/8/2015.
 */
public class Share /* implements Parcelable */ {

    private final String personName;
    private final Map<String, MonetaryAmount> expensePortions;
    private final CumulativeCost indivCumulativeCost;

    public static Share getFreeCostInstance(String personName) {
        return new Share(personName, new HashMap<String, MonetaryAmount>(), CumulativeCost.FREE);
    }

    public Share(String personName, Map<String, MonetaryAmount> expensePortions, CumulativeCost indivCumulativeCost) {
        this.personName = personName;
        this.expensePortions = expensePortions;
        this.indivCumulativeCost = indivCumulativeCost;
    }

    public String getPersonName() {
        return personName;
    }

    public Map<String, MonetaryAmount> getExpensePortions() {
        return Collections.unmodifiableMap(expensePortions);
    }

    public CumulativeCost getIndivCumulativeCost() {
        return indivCumulativeCost;
    }

    ////// Parcelable methods ///////////////////


//    protected Share(Parcel in) {
//    }
//
//    public static final Creator<Share> CREATOR = new Creator<Share>() {
//        @Override
//        public Share createFromParcel(Parcel in) {
//            return new Share(in);
//        }
//
//        @Override
//        public Share[] newArray(int size) {
//            return new Share[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//    }
}

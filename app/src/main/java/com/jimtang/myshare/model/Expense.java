package com.jimtang.myshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by tangz on 10/16/2015.
 */
public class Expense implements Parcelable {

    private final String[] people;
    private final BigDecimal amount;
    private final String expenseName;

    public static Expense getInstance(String[] people, String expenseName, BigDecimal amount) {
        return new Expense(people, expenseName, amount);
    }

    public static Expense getSharedByAllInstance(String expenseName, BigDecimal amount) {
        return new Expense(new String[]{"EVERYONE"}, expenseName, amount) {
            @Override
            public boolean sharedByAll() {
                return true;
            }
        };
    }

    Expense(String[] people, String expenseName, BigDecimal amount) {
        this.people = people;
        this.amount = amount;
        this.expenseName = expenseName;
    }

    public boolean sharedByAll() {
        return false;
    }

    public String[] getPeople() {
        return people;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    ///////////////////////////////////
    // following methods for Parcelable
    ///////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(people);
        dest.writeString(expenseName);
        dest.writeSerializable(amount);
    }

    protected Expense(Parcel in) {
        people = in.createStringArray();
        expenseName = in.readString();
        // for now, expect that only one Serializable instance, our amount BigDecimal, will be written to this parcel.
        amount = (BigDecimal) in.readSerializable();
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}

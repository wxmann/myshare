package com.jimtang.myshare.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tangz on 10/16/2015.
 */
public class Expense implements Parcelable {

    private final String[] people;
    private final MonetaryAmount amount;
    private final String expenseName;
    private boolean sharedByAll;

    public static Expense getInstance(String[] people, String expenseName, MonetaryAmount amount) {
        return new Expense(people, expenseName, amount, false);
    }

    public static Expense getSharedByAllInstance(String expenseName, MonetaryAmount amount) {
        return new Expense(new String[]{"EVERYONE"}, expenseName, amount, true);
    }

    Expense(String[] people, String expenseName, MonetaryAmount amount, boolean sharedByAll) {
        this.people = people;
        this.amount = amount;
        this.expenseName = expenseName;
        this.sharedByAll = sharedByAll;
    }

    public boolean isSplitByAll() {
        return sharedByAll;
    }

    public String[] getPeople() {
        return people;
    }

    public MonetaryAmount getAmount() {
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
        dest.writeParcelable(amount, 0);
        dest.writeInt(sharedByAll ? 1 : 0);
    }

    protected Expense(Parcel in) {
        people = in.createStringArray();
        expenseName = in.readString();
        amount = in.readParcelable(null);
        sharedByAll = in.readInt() == 1;
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

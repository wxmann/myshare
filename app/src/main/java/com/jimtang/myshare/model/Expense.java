package com.jimtang.myshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by tangz on 10/16/2015.
 */
public class Expense implements Parcelable {

    private final String[] people;
    private final MonetaryAmount amount;
    private final String expenseName;

    // this flag means that the people[] array include all participants
    private boolean sharedByAll;

    public static Expense getInstance(String[] people, String expenseName, MonetaryAmount amount) {
        return new Expense(people, expenseName, amount, false);
    }

    public static Expense getSharedByAllInstance(String[] allPeople, String expenseName, MonetaryAmount amount) {
        return new Expense(allPeople, expenseName, amount, true);
    }

    Expense(String[] people, String expenseName, MonetaryAmount amount, boolean sharedByAll) {
        this.people = Arrays.copyOf(people, people.length);
        Arrays.sort(this.people);
        this.amount = amount;
        this.expenseName = expenseName;
        this.sharedByAll = sharedByAll;
    }

    public boolean involves(String personName) {
        if (isSplitByAll()) {
            return true;
        } else {
            for (String person : people) {
                if (personName.equals(person)) {
                    return true;
                }
            }
            return false;
        }
    }

    public int numberOfPeople() {
        return people.length;
    }

    public boolean isSplitByAll() {
        return sharedByAll;
    }

    public String[] getPeople() {
        String[] pplCopy = new String[people.length];
        System.arraycopy(people, 0, pplCopy, 0, people.length);
        return pplCopy;
    }

    public MonetaryAmount getAmount() {
        return amount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Expense expense = (Expense) o;

        if (!Arrays.equals(people, expense.people)) {
            return false;
        }
        if (!amount.equals(expense.amount)) {
            return false;
        }
        return expenseName.equals(expense.expenseName);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(people);
        result = 31 * result + amount.hashCode();
        result = 31 * result + expenseName.hashCode();
        return result;
    }

    //// following methods for Parcelable //////////////

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
        amount = in.readParcelable(MonetaryAmount.class.getClassLoader());
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

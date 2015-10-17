package com.jimtang.myshare.model;

import java.math.BigDecimal;

/**
 * Created by tangz on 10/16/2015.
 */
public class Expense {

    private String[] people;
    private BigDecimal amount;
    private String expenseName;

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
}

package com.jimtang.myshare.model;

import java.math.BigDecimal;

/**
 * Created by tangz on 10/16/2015.
 */
public class Expense {

    private String[] people;
    private BigDecimal amount;
    private String expenseName;

    public Expense(String[] people, String expenseName, BigDecimal amount) {
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

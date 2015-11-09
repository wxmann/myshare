package com.jimtang.myshare.calc;

import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.math.BigDecimal;

/**
 * Created by tangz on 11/8/2015.
 */
public class ExpensePortionCalculator {

    static final ExpensePortionCalculator INSTANCE = new ExpensePortionCalculator();

    public static ExpensePortionCalculator getInstance() {
        return INSTANCE;
    }

    public MonetaryAmount expensePortionFor(String personName, Expense expense) {
        if (!expense.involves(personName)) {
            return MonetaryAmount.ZERO;
        } else {
            int numPpl = expense.numberOfPeople();
            MonetaryAmount total = expense.getAmount();
            return total.divide(new BigDecimal(numPpl));
        }
    }
}

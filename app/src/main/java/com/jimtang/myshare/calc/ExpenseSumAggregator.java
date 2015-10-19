package com.jimtang.myshare.calc;

import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * Created by tangz on 10/18/2015.
 */
public class ExpenseSumAggregator {

    private static final ExpenseSumAggregator INSTANCE = new ExpenseSumAggregator();

    public static ExpenseSumAggregator getInstance() {
        return INSTANCE;
    }

    private ExpenseSumAggregator() {
    }

    public MonetaryAmount aggregate(Collection<Expense> expenses) {
        MonetaryAmount sum = MonetaryAmount.ZERO;
        for (Expense expense : expenses) {
            sum = sum.add(expense.getAmount());
        }
        return sum;
    }
}

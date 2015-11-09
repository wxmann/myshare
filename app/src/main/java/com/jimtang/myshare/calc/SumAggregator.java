package com.jimtang.myshare.calc;

import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.util.Collection;

/**
 * Created by tangz on 10/18/2015.
 */
public class SumAggregator {

    private static final SumAggregator INSTANCE = new SumAggregator();

    public static SumAggregator getInstance() {
        return INSTANCE;
    }

    private SumAggregator() {
    }

    public MonetaryAmount aggregateExpenses(Collection<Expense> expenses) {
        MonetaryAmount sum = MonetaryAmount.ZERO;
        for (Expense expense : expenses) {
            sum = sum.add(expense.getAmount());
        }
        return sum;
    }

    public MonetaryAmount aggregateAmounts(Collection<MonetaryAmount> amounts) {
        MonetaryAmount sum = MonetaryAmount.ZERO;
        for (MonetaryAmount amount : amounts) {
            sum = sum.add(amount);
        }
        return sum;
    }
}

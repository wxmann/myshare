package com.jimtang.myshare.calc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jimtang.myshare.exception.CalculationException;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;
import com.jimtang.myshare.model.Share;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tangz on 10/18/2015.
 */
public class ShareCostsCalculator {

    private List<Expense> expenses;
    private CumulativeCost totalCost;

    private ExpensePortionCalculator portionCalculator = ExpensePortionCalculator.getInstance();
    private SumAggregator aggregator = SumAggregator.getInstance();

    private Set<String> allParticipants;
    private List<Share> cachedAllShares;

    public ShareCostsCalculator(CumulativeCost totalCost, List<Expense> expenses) {
        this.totalCost = totalCost;
        this.expenses = expenses;
    }

    public Set<String> getParticipants() {
        if (allParticipants == null) {
            allParticipants = Sets.newHashSet();
            for (Expense expense : expenses) {
                String[] people = expense.getPeople();
                for (String person : people) {
                    allParticipants.add(person);
                }
                if (expense.isSplitByAll()) {
                    // we're done; we've already added everyone that we can add.
                    break;
                }
            }
        }
        return allParticipants;
    }

    // bad algorithm, but won't matter for small data volumes.
    // for manual inputs, won't expect participants >~ 10.
    public List<Share> allShares() {
        if (cachedAllShares == null) {
            cachedAllShares = Lists.newArrayList();
            for (String name: getParticipants()) {
                cachedAllShares.add(shareFor(name));
            }
        }
        return cachedAllShares;
    }

    public Share shareFor(String name) {
        if (totalCost.getTotal().equals(MonetaryAmount.ZERO)) {
            return Share.getFreeCostInstance(name);
        }

        // calculate portions of expenses for me
        final Map<String, MonetaryAmount> expenseAmts = Maps.newHashMap();
        for (Expense expense: expenses) {
            MonetaryAmount portion = portionCalculator.expensePortionFor(name, expense);
            if (!portion.equals(MonetaryAmount.ZERO)) {
                expenseAmts.put(expense.getExpenseName(), portion);
            }
        }
        if (expenseAmts.isEmpty()) {
            // when we build the expenses up through the UI, we add the names to the expenses as we go.
            // hence not having a name being present in -ANY- expense would clearly indicate an
            // error in internal application logic.
            throw new CalculationException(
                    String.format("Internal error: Can't calculate share for: %s; (s)he wasn't added to any expenses", name));
        }

        // now use the cumulative total to individual total ratios to calculate individual tax/tip
        final MonetaryAmount totalSubtotal = totalCost.getSubtotal();
        final MonetaryAmount totalTax = totalCost.getTax();
        final MonetaryAmount totalTip = totalCost.getTip();

        MonetaryAmount resultSubtotal = aggregator.aggregateAmounts(expenseAmts.values());
        // do not worry about the divide by zero; taken care of earlier check whether expenses are empty.
        BigDecimal singleToTotalRatio = resultSubtotal.divide(totalSubtotal);
        MonetaryAmount resultTax = totalTax.multiply(singleToTotalRatio);
        MonetaryAmount resultTip = totalTip.multiply(singleToTotalRatio);

        CumulativeCost totalCost = new CumulativeCost(resultSubtotal, resultTax, resultTip);
        return new Share(name, expenseAmts, totalCost);
    }
}

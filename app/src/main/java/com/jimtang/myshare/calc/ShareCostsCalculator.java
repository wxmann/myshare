package com.jimtang.myshare.calc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jimtang.myshare.model.Cost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tangz on 10/18/2015.
 */
public class ShareCostsCalculator {

    private List<Expense> expenses;
    private Cost totalCost;

    private Set<String> allParticipants;
    private Map<String, Cost> cachedAllShares;

    public ShareCostsCalculator(Cost totalCost, List<Expense> expenses) {
        this.totalCost = totalCost;
        this.expenses = expenses;
    }

    List<Expense> filterForPerson(String name) {
        List<Expense> expensesForPerson = Lists.newArrayList();
        for (Expense expense: expenses) {
            if (expense.involves(name)) {
                expensesForPerson.add(expense);
            }
        }
        return expensesForPerson;
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
    public Map<String, Cost> allShares() {
        if (cachedAllShares == null) {
            cachedAllShares = Maps.newHashMap();
            for (String name: getParticipants()) {
                cachedAllShares.put(name, shareFor(name));
            }
        }
        return cachedAllShares;
    }

    public Cost shareFor(String name) {
        if (totalCost.getTotal().equals(MonetaryAmount.ZERO)) {
            return Cost.FREE;
        }

        final MonetaryAmount totalSubtotal = totalCost.getSubtotal();
        final MonetaryAmount totalTax = totalCost.getTax();
        final MonetaryAmount totalTip = totalCost.getTip();

        List<Expense> filteredForPerson = filterForPerson(name);
        if (filteredForPerson.isEmpty()) {
            // can't imagine this scenario but we should be careful nonetheless.
            throw new CalculationException(
                    String.format("Can't calculate share for: %s; (s)he wasn't added to any expenses", name));
        }

        MonetaryAmount resultSubtotal = MonetaryAmount.ZERO;
        MonetaryAmount resultTax = MonetaryAmount.ZERO;
        MonetaryAmount resultTip = MonetaryAmount.ZERO;

        for (Expense involvedExpense: filteredForPerson) {
            MonetaryAmount subtotalForExpense = involvedExpense.getAmount();

            // this divide uses MonetaryAmount which takes care of repeating decimals.
            BigDecimal singleToTotalRatio = subtotalForExpense.divide(totalSubtotal);
            BigDecimal numberOfPeople = new BigDecimal(involvedExpense.numberOfPeople());

            // this divide uses BigDecimal which does NOT take care of repeating decimals.
            // hence have to provide explicit rounding parameters
            BigDecimal scalingFactor = singleToTotalRatio.divide(numberOfPeople,
                    MonetaryAmount.DIVISION_SCALE, RoundingMode.CEILING);

            resultSubtotal = resultSubtotal.add(totalSubtotal.multiply(scalingFactor));
            resultTax = resultTax.add(totalTax.multiply(scalingFactor));
            resultTip = resultTip.add(totalTip.multiply(scalingFactor));
        }
        return new Cost(resultSubtotal, resultTax, resultTip);
    }
}

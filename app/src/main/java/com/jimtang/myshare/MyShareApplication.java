package com.jimtang.myshare;

import android.app.Application;

import com.jimtang.myshare.calc.ShareCalculator;
import com.jimtang.myshare.calc.ShareResults;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by tangz on 9/9/2015.
 */
public class MyShareApplication extends Application {

    private ShareCalculator calculator;
    private Map<String, ShareResults> cachedShares;
    private ShareResults cachedCombined;

    @Override
    public void onCreate() {
        super.onCreate();
        resetAll();
    }

    private void resetAll() {
        resetCalculator();
        resetCache();
    }

    private void resetCalculator() {
        calculator = new ShareCalculator();
    }

    private void resetCache() {
        if (cachedShares == null || !cachedShares.isEmpty()) {
            cachedShares = new HashMap<>();
        }
        cachedCombined = null;
    }

    private ShareResults shareResultsForCombined() {
        if (cachedCombined != null) {
            return cachedCombined;
        } else {
            ShareResults all = calculator.shareForAll();
            cachedCombined = all;
            return all;
        }
    }

    private ShareResults shareResultsFor(String name) {
        if (cachedShares.containsKey(name)) {
            return cachedShares.get(name);
        } else {
            ShareResults results = calculator.shareFor(name);
            cachedShares.put(name, results);
            return results;
        }
    }

    public BigDecimal subtotalCombined() {
        return shareResultsForCombined().getSubtotal();
    }

    public BigDecimal taxCombined() {
        return shareResultsForCombined().getTax();
    }

    public BigDecimal tipCombined() {
        return shareResultsForCombined().getTip();
    }

    public BigDecimal totalCombined() {
        return shareResultsForCombined().getTotal();
    }

    public BigDecimal subtotalFor(String name) {
        return shareResultsFor(name).getSubtotal();
    }

    public BigDecimal taxFor(String name) {
        return shareResultsFor(name).getTax();
    }

    public BigDecimal tipFor(String name) {
        return shareResultsFor(name).getTip();
    }

    public BigDecimal totalFor(String name) {
        return shareResultsFor(name).getTotal();
    }

    public void addPerson(String name, BigDecimal cost) {
        calculator.addPerson(name, cost);
        resetCache();
    }

    public void removePerson(String name) {
        calculator.removePerson(name);
        resetCache();
    }

    // This method will need updating if we can add multiple amounts
    public void updateAmount(String name, BigDecimal amt) {
        calculator.updateAmount(name, amt);
        resetCache();
    }

    public Set<String> participants() {
        return calculator.getPeople();
    }

    public void setTotals(BigDecimal subtotal, BigDecimal tax, BigDecimal tipPercentage) {
        calculator.setSubtotal(subtotal);
        calculator.setTax(tax);
        calculator.setTipPercentage(tipPercentage);
        resetCache();
    }
}

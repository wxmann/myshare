package com.jimtang.myshare;

import android.app.Application;

import com.jimtang.myshare.calc.ShareCalculator;
import com.jimtang.myshare.calc.ShareResults;

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

    public Double subtotalCombined() {
        return shareResultsForCombined().getSubtotal();
    }

    public Double taxCombined() {
        return shareResultsForCombined().getTax();
    }

    public Double tipCombined() {
        return shareResultsForCombined().getTip();
    }

    public Double totalCombined() {
        return shareResultsForCombined().getTotal();
    }

    public Double subtotalFor(String name) {
        return shareResultsFor(name).getSubtotal();
    }

    public Double taxFor(String name) {
        return shareResultsFor(name).getTax();
    }

    public Double tipFor(String name) {
        return shareResultsFor(name).getTip();
    }

    public Double totalFor(String name) {
        return shareResultsFor(name).getTotal();
    }

    public void addPerson(String name, Double cost) {
        calculator.addPerson(name, cost);
        resetCache();
    }

    public void removePerson(String name) {
        calculator.removePerson(name);
        resetCache();
    }

    // This method will need updating if we can add multiple amounts
    public void updateAmount(String name, Double amt) {
        calculator.updateAmount(name, amt);
        resetCache();
    }

    public Set<String> participants() {
        return calculator.getPeople();
    }

    public void setTotals(Double subtotal, Double tax, Double tipPercentage) {
        calculator.setSubtotal(subtotal);
        calculator.setTax(tax);
        calculator.setTipPercentage(tipPercentage);
        resetCache();
    }
}

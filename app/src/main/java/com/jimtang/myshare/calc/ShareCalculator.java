package com.jimtang.myshare.calc;

//import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * TODO: shared things.
 * Created by tangz on 9/3/2015.
 */
public class ShareCalculator {

    private Map<String, Double> amounts;

    private Double subtotal;
    private Double tax;
    private Double tip;

    public ShareCalculator() {
        this.amounts = new HashMap<>();
        this.subtotal = 0.0;
        this.tax = 0.0;
        this.tip = 0.0;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public void setTipPercentage(Double tipPercentage) {
        setTip(calculateTipFromPercentage(this.subtotal + this.tax, tipPercentage));
    }

    public ShareResults shareForAll() {
        return new CumulativeShareResults(subtotal, tax, tip);
    }

    private void checkPersonExists(String name) {
        if (!amounts.containsKey(name)) {
            throw new RuntimeException("Person does not exist: " + name);
        }
    }

    public void addPerson(String name, Double amount) {
        // TODO: use multimap
        if (amounts.containsKey(name)) {
            throw new RuntimeException("Duplicate person: " + name);
        }
        amounts.put(name, amount);
    }

    public void removePerson(String name) {
        checkPersonExists(name);
        amounts.remove(name);
    }

    public void updateAmount(String name, Double newAmount) {
        checkPersonExists(name);
        amounts.put(name, newAmount);
    }

    public Set<String> getPeople() {
        return amounts.keySet();
    }

    static double calculateTipFromPercentage(double totalWithTax, double percentage) {
        return totalWithTax * percentage / 100;
    }

    ShareResults calculateIndividualShare(String name, double amt) {
        if (amt == 0 || subtotal == 0) {
            return new ShareResults(name, 0.0, 0.0, 0.0);
        }
        double amtPercentage = amt / subtotal;

        double mySubtotal = subtotal * amtPercentage;
        double myTax = tax * amtPercentage;
        double myTip = tip * amtPercentage;

        return new ShareResults(name, mySubtotal, myTax, myTip);
    }

    public ShareResults shareFor(String name) {
        checkPersonExists(name);
        Double indivAmt = amounts.get(name);
        return calculateIndividualShare(name, indivAmt);
    }
}

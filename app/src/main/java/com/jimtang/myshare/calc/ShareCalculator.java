package com.jimtang.myshare.calc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.math.BigDecimal.ZERO;

/**
 * TODO: shared things.
 * Created by tangz on 9/3/2015.
 */
public class ShareCalculator {

    private Map<String, BigDecimal> amounts;

    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal tip;

    public ShareCalculator() {
        this.amounts = new HashMap<>();
        this.subtotal = ZERO;
        this.tax = ZERO;
        this.tip = ZERO;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public void setTip(BigDecimal tip) {
        this.tip = tip;
    }

    public void setTipPercentage(BigDecimal tipPercentage) {
        setTip(calculateTipFromPercentage(subtotal.add(tax), tipPercentage));
    }

    public ShareResults shareForAll() {
        return new CumulativeShareResults(subtotal, tax, tip);
    }

    private void checkPersonExists(String name) {
        if (!amounts.containsKey(name)) {
            throw new RuntimeException("Person does not exist: " + name);
        }
    }

    public void addPerson(String name, BigDecimal amount) {
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

    public void updateAmount(String name, BigDecimal newAmount) {
        checkPersonExists(name);
        amounts.put(name, newAmount);
    }

    public Set<String> getPeople() {
        return amounts.keySet();
    }

    static BigDecimal percentageToDecimal(BigDecimal percentage) {
        return percentage.divide(new BigDecimal(100));
    }

    static BigDecimal calculateTipFromPercentage(BigDecimal totalWithTax, BigDecimal percentage) {
        return totalWithTax.multiply(percentageToDecimal(percentage));
    }

    static boolean isZero(BigDecimal num) {
        return num == null || num == ZERO || num.equals(ZERO);
    }

    ShareResults calculateIndividualShare(String name, BigDecimal amt) {
        if (isZero(amt) || isZero(subtotal)) {
            return new ShareResults(name, ZERO, ZERO, ZERO);
        }
        BigDecimal amtPercentage = amt.divide(subtotal);

        BigDecimal mySubtotal = subtotal.multiply(amtPercentage);
        BigDecimal myTax = tax.multiply(amtPercentage);
        BigDecimal myTip = tip.multiply(amtPercentage);

        return new ShareResults(name, mySubtotal, myTax, myTip);
    }

    public ShareResults shareFor(String name) {
        checkPersonExists(name);
        BigDecimal indivAmt = amounts.get(name);
        return calculateIndividualShare(name, indivAmt);
    }
}

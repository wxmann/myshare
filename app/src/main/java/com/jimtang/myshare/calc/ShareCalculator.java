package com.jimtang.myshare.calc;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.math.BigDecimal.ZERO;

/**
 * TODO: shared things.
 * Created by tangz on 9/3/2015.
 */
public class ShareCalculator {

    /* Map of purchase to cost. */
    private Map<String, BigDecimal> individualPurchases;

    /* Map of purchase to cost. */
    private Map<String, BigDecimal> groupPurchases;

    /* Map of person purchasing to purchases. */
    private Multimap<String, String> participants;

    /* Cumulative amounts */
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal tip;

    public ShareCalculator() {
        this.individualPurchases = new HashMap<>();
        this.groupPurchases = new HashMap<>();
        this.participants = ArrayListMultimap.create();
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

    public ShareResults cumulativeAmounts() {
        return new CumulativeShareResults(subtotal, tax, tip);
    }

    public boolean participantExists(String person) {
        return participants.containsKey(person);
    }

    public boolean purchaseExists(String purchase) {
        return individualPurchases.containsKey(purchase) || groupPurchases.containsKey(purchase);
    }

    public void addIndividualPurchase(String person, String purchase, BigDecimal amount) {
        assertPurchaseDoesNotExist(purchase);
        individualPurchases.put(purchase, amount);
        participants.put(person, purchase);
    }

    public void addGroupPurchase(String purchase, BigDecimal amount) {
        assertPurchaseDoesNotExist(purchase);
        groupPurchases.put(purchase, amount);
    }

    public void removePurchase(String purchase) {
        assertPurchaseExists(purchase);
        individualPurchases.remove(purchase);
        groupPurchases.remove(purchase);
    }

    public void updateAmount(String purchase, BigDecimal newAmount) {
        assertPurchaseExists(purchase);
        if (individualPurchases.containsKey(purchase)) {
            individualPurchases.put(purchase, newAmount);
        }
        if (groupPurchases.containsKey(purchase)) {
            groupPurchases.put(purchase, newAmount);
        }
    }

    public Set<String> getParticipants() {
        return participants.keySet();
    }

    public BigDecimal getPurchaseAmount(String purchase) {
        assertPurchaseExists(purchase);
        if (individualPurchases.containsKey(purchase)) {
            return individualPurchases.get(purchase);
        }
        if (groupPurchases.containsKey(purchase)) {
            return groupPurchases.get(purchase);
        }
        // will never reach here as we have asserted that purchase exists.
        throw new RuntimeException("Should never reach here");
    }

    public BigDecimal getTotalPurchaseSubtotal() {
        BigDecimal sumIndivPurchases = ZERO;
        for (String person : getParticipants()) {
            sumIndivPurchases = sumIndivPurchases.add(getIndividualPurchaseSubtotal(person));
        }
        return sumIndivPurchases.add(getGroupPurchaseSubtotal());
    }

    public BigDecimal getGroupPurchaseSubtotal() {
        BigDecimal sum = ZERO;
        for (BigDecimal groupPurchase : groupPurchases.values()) {
            sum = sum.add(groupPurchase);
        }
        return sum;
    }

    public BigDecimal getIndividualPurchaseSubtotal(String person) {
        Collection<String> purchases = participants.get(person);
        BigDecimal sum = ZERO;
        for (String purchase : purchases) {
            sum = sum.add(getPurchaseAmount(purchase));
        }
        return sum;
    }

    // TODO: may move above into a different class
    /////////////////////////
    // CALCULATION METHODS //
    /////////////////////////

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

    BigDecimal amountFromSharedPurchases() {
        int participantSize = getParticipants().size();
        BigDecimal aggregated = getGroupPurchaseSubtotal();
        return aggregated.divide(new BigDecimal(participantSize));
    }

    public ShareResults shareFor(String name) {
        assertParticipantExists(name);
        BigDecimal indivAmt = getIndividualPurchaseSubtotal(name);
        BigDecimal sharedAmt = amountFromSharedPurchases();

        return calculateIndividualShare(name, indivAmt.add(sharedAmt));
    }

    ///////////////////////
    // ASSERTION METHODS //
    ///////////////////////

    private void assertParticipantDoesNotExist(String name) {
        if (participantExists(name)) {
            throw new RuntimeException("Person already exists: " + name);
        }
    }

    private void assertParticipantExists(String name) {
        if (!participantExists(name)) {
            throw new RuntimeException("Person does not exist: " + name);
        }
    }

    private void assertPurchaseDoesNotExist(String purchase) {
        if (purchaseExists(purchase)) {
            throw new RuntimeException("Purchase already exists: " + purchase);
        }
    }

    private void assertPurchaseExists(String purchase) {
        if (!purchaseExists(purchase)) {
            throw new RuntimeException("Purchase does not exist: " + purchase);
        }
    }
}

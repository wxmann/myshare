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
 * Created by tangz on 9/13/2015.
 */
public class PurchaseData {

    /* Map of purchase to cost. */
    private Map<String, BigDecimal> individualPurchases;

    /* Map of purchase to cost. */
    private Map<String, BigDecimal> groupPurchases;

    /* Map of person purchasing to purchases. */
    private Multimap<String, String> participants;

    public PurchaseData() {
        this.individualPurchases = new HashMap<>();
        this.groupPurchases = new HashMap<>();
        this.participants = ArrayListMultimap.create();
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

        //remove purchase in participant-purchase mapping
        String participantForRemovedPurchase = null;
        for (String person : participants.keySet()) {
            Collection<String> purchases = participants.get(person);
            if (purchases.contains(purchase)) {
                participants.remove(person, purchase);
                participantForRemovedPurchase = person;
                break;
            }
        }

        // make sure we don't have orphan participants whose purchases have been removed
        if (participantForRemovedPurchase != null) {
            if (participants.get(participantForRemovedPurchase).isEmpty()) {
                participants.removeAll(participantForRemovedPurchase);
            }
        }
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

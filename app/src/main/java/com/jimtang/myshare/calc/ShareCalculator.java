package com.jimtang.myshare.calc;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

/**
 * Created by tangz on 9/3/2015.
 */
public class ShareCalculator {

    private PurchaseData purchaseData;

    /* Cumulative amounts */
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal tip;

    public ShareCalculator(PurchaseData purchaseData) {
        this.purchaseData = purchaseData;
        this.subtotal = purchaseData.getTotalPurchaseSubtotal();
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
        int participantSize = purchaseData.getParticipants().size();
        BigDecimal aggregated = purchaseData.getGroupPurchaseSubtotal();
        return aggregated.divide(new BigDecimal(participantSize));
    }

    public ShareResults shareFor(String name) {
        if (!purchaseData.participantExists(name)) {
            throw new RuntimeException(String.format("Purchase data for person: %s does not exist"));
        }
        BigDecimal indivAmt = purchaseData.getIndividualPurchaseSubtotal(name);
        BigDecimal sharedAmt = amountFromSharedPurchases();

        return calculateIndividualShare(name, indivAmt.add(sharedAmt));
    }

}

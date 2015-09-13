package com.jimtang.myshare.calc;

/**
 * Created by tangz on 9/9/2015.
 */
public class ShareResults {

    private String name;
    private Double subtotal;
    private Double tax;
    private Double tip;

    public ShareResults(String name, Double subtotal, Double tax, Double tip) {
        this.name = name;
        this.subtotal = subtotal;
        this.tax = tax;
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public Double getTax() {
        return tax;
    }

    public Double getTip() {
        return tip;
    }

    public Double getTotal() {
        return subtotal + tax + tip;
    }

}

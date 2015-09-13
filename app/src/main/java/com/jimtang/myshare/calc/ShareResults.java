package com.jimtang.myshare.calc;

import java.math.BigDecimal;

/**
 * Created by tangz on 9/9/2015.
 */
public class ShareResults {

    private String name;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal tip;

    public ShareResults(String name, BigDecimal subtotal, BigDecimal tax, BigDecimal tip) {
        this.name = name;
        this.subtotal = subtotal;
        this.tax = tax;
        this.tip = tip;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTip() {
        return tip;
    }

    public BigDecimal getTotal() {
        return subtotal.add(tax).add(tip);
    }

}

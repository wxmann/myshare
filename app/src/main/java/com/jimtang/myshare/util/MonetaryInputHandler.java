package com.jimtang.myshare.util;

import android.app.AlertDialog;
import android.content.Context;

import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;

/**
 * Created by tangz on 9/12/2015.
 */
public abstract class MonetaryInputHandler implements InputHandler<BigDecimal> {

    public static MonetaryInputHandler forOptionalAmount(final Context appContext) {
        return new MonetaryInputHandler() {
            @Override
            public BigDecimal handleNull(CharSequence input) {
                return ZERO;
            }
        };
    }

    public static MonetaryInputHandler forRequiredAmount(final Context appContext) {
        return new MonetaryInputHandler() {
            @Override
            public BigDecimal handleNull(CharSequence input) {
                new AlertDialog.Builder(appContext)
                        .setTitle("A required field is missing. Check your inputs.")
                        .setCancelable(false)
                        .create();
                return ZERO;
            }
        };
    }

    @Override
    public BigDecimal handleInput(CharSequence input) {
        if (input == null) {
            return handleNull(input);
        }
        String inputStr = input.toString();
        try {
            return new BigDecimal(Double.parseDouble(inputStr));
        } catch (NumberFormatException e) {
            return handleInvalidFormat(input);
        }
    }

    public BigDecimal handleInvalidFormat(CharSequence input) {
        // TODO: handle number format exception
        return ZERO;
    }

    public abstract BigDecimal handleNull(CharSequence input);
}

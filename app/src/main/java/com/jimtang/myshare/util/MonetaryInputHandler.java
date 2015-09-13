package com.jimtang.myshare.util;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by tangz on 9/12/2015.
 */
public abstract class MonetaryInputHandler implements InputHandler<Double> {

    public static MonetaryInputHandler forOptionalAmount(final Context appContext) {
        return new MonetaryInputHandler() {
            @Override
            public Double handleNull(CharSequence input) {
                return 0.0;
            }
        };
    }

    public static MonetaryInputHandler forRequiredAmount(final Context appContext) {
        return new MonetaryInputHandler() {
            @Override
            public Double handleNull(CharSequence input) {
                new AlertDialog.Builder(appContext)
                        .setTitle("A required field is missing. Check your inputs.")
                        .setCancelable(false)
                        .create();
                return 0.0;
            }
        };
    }

    @Override
    public Double handleInput(CharSequence input) {
        if (input == null) {
            return handleNull(input);
        }
        String inputStr = input.toString();
        try {
            return Double.parseDouble(inputStr);
        } catch (NumberFormatException e) {
            return handleInvalidFormat(input);
        }
    }

    public Double handleInvalidFormat(CharSequence input) {
        // TODO: handle number format exception
        return 0.0;
    }

    public abstract Double handleNull(CharSequence input);
}

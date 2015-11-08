package com.jimtang.myshare.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * A barebones OnClickListener implementation that that validates inputs prior to starting a new
 * activity via an Intent.
 *
 * Created by tangz on 11/8/2015.
 */
public abstract class ValidatableIntentClickListener implements View.OnClickListener{

    private Context context;
    private Class<? extends Activity> secondActivity;

    public ValidatableIntentClickListener(Context context, Class<? extends Activity> secondActivity) {
        this.context = context;
        this.secondActivity = secondActivity;
    }

    protected abstract boolean validateInputs();

    protected abstract void addParcelables(Intent intent);

    @Override
    public void onClick(View v) {
        if (validateInputs()) {
            Intent intent = new Intent(context, secondActivity);
            addParcelables(intent);
            context.startActivity(intent);
        }
    }
}

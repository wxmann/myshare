package com.jimtang.myshare.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.jimtang.myshare.R;
import com.jimtang.myshare.calc.ExpenseSumAggregator;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.util.ArrayList;

/**
 * Created by tangz on 10/18/2015.
 */
public class CumulativeTotalsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cumulative_totals);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            ArrayList<Expense> receivedExpenses = receivedIntent.getParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES);
            MonetaryAmount sum = ExpenseSumAggregator.getInstance().aggregate(receivedExpenses);

            EditText subtotalField = (EditText) findViewById(R.id.cumul_subtotal);
            subtotalField.setText(sum.toNumericString());
        }

        // TODO: handle tax and tip inputs and actually do the calculation
    }
}

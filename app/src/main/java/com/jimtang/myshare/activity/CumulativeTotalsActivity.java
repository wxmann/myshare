package com.jimtang.myshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.calc.SumAggregator;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by tangz on 10/18/2015.
 */
public class CumulativeTotalsActivity extends AbstractMyShareActivity {

    ArrayList<Expense> expenses = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cumulative_totals);

        final EditText subtotalField = (EditText) findViewById(R.id.cumul_subtotal);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            expenses = receivedIntent.getParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES);
            MonetaryAmount subtotal = SumAggregator.getInstance().aggregateExpenses(expenses);
            subtotalField.setText(subtotal.toNumericString());
        }

        Button button = (Button) findViewById(R.id.cumul_next_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MonetaryAmount subtotalAmt = new MonetaryAmount(subtotalField.getText().toString());

                EditText taxField = (EditText) findViewById(R.id.cumul_tax);
                MonetaryAmount taxAmt = new MonetaryAmount(taxField.getText().toString());

                EditText tipField = (EditText) findViewById(R.id.cumul_tip);
                BigDecimal tipPercentage = new BigDecimal(tipField.getText().toString());
                // we can do this without worrying about an ArithmeticException because
                // all tip percentage user inputs will be rational.
                BigDecimal tipDecimal = tipPercentage.divide(new BigDecimal(100));
                MonetaryAmount tipAmt = subtotalAmt.add(taxAmt).multiply(tipDecimal);

                CumulativeCost totalCost = new CumulativeCost(subtotalAmt, taxAmt, tipAmt);
                Intent intent = new Intent(CumulativeTotalsActivity.this, DisplaySharesActivity.class);
                intent.putExtra(IntentConstants.ADDED_EXPENSES, expenses);
                intent.putExtra(IntentConstants.TOTAL_COSTS, totalCost);

                startActivity(intent);
            }
        });
    }
}

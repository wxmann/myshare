package com.jimtang.myshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.calc.ExpenseSumAggregator;
import com.jimtang.myshare.model.Cost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by tangz on 10/18/2015.
 */
public class CumulativeTotalsActivity extends Activity {

    ArrayList<Expense> expenses = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cumulative_totals);

        final EditText subtotalField = (EditText) findViewById(R.id.cumul_subtotal);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            expenses = receivedIntent.getParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES);
            MonetaryAmount subtotal = ExpenseSumAggregator.getInstance().aggregate(expenses);
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

                Cost totalCost = new Cost(subtotalAmt, taxAmt, tipAmt);
                Intent intent = new Intent(CumulativeTotalsActivity.this, DisplaySharesActivity.class);
                intent.putExtra(IntentConstants.ADDED_EXPENSES, expenses);
                intent.putExtra(IntentConstants.TOTAL_COSTS, totalCost);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

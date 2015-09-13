package com.jimtang.myshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jimtang.myshare.util.MonetaryInputHandler;

import java.math.BigDecimal;

public class FrontPageActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);

        Button calculateButton = (Button)findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.calculate_button)) {
            MyShareApplication app = ((MyShareApplication) getApplicationContext());

            // handle cumulative amounts
            TextView subtotalText = (TextView)findViewById(R.id.subtotal_input);
            BigDecimal subtotal = MonetaryInputHandler.forRequiredAmount(this).handleInput(subtotalText.getText());

            TextView taxText = (TextView)findViewById(R.id.tax_input);
            BigDecimal tax = MonetaryInputHandler.forOptionalAmount(this).handleInput(taxText.getText());

            TextView tipText = (TextView)findViewById(R.id.tip_input);
            BigDecimal tipPercentage = MonetaryInputHandler.forOptionalAmount(this).handleInput(tipText.getText());

            app.setTotals(subtotal, tax, tipPercentage);

            // handle individual amounts
            TextView nameText = (TextView)findViewById(R.id.name_input);
            String name = nameText.getText().toString();

            TextView purchaseNameText = (TextView)findViewById(R.id.purchase_input);
            String purchaseName = purchaseNameText.getText().toString();

            TextView amtText = (TextView)findViewById(R.id.amt_input);
            BigDecimal indivAmt = MonetaryInputHandler.forRequiredAmount(this).handleInput(amtText.getText());

            app.addIndividualCost(name, purchaseName, indivAmt);

            // go!
            Intent goToDisplay = new Intent(this, DisplayShareActivity.class);
            startActivity(goToDisplay);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

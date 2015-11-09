package com.jimtang.myshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.adapter.SplitCostsListAdapter;
import com.jimtang.myshare.calc.ShareCostsCalculator;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangz on 10/18/2015.
 */
public class DisplaySharesActivity extends Activity {

    ArrayList<Expense> expenses = Lists.newArrayList();
    CumulativeCost totalCost = CumulativeCost.FREE;
    ShareCostsCalculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_shares);

        Intent intent = getIntent();
        if (intent != null) {
            totalCost = intent.getParcelableExtra(IntentConstants.TOTAL_COSTS);
            expenses = intent.getParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES);
        }
        calculator = new ShareCostsCalculator(totalCost, expenses);
        List<Share> allShares = calculator.allShares();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.sharesListView);
        SplitCostsListAdapter adapter = new SplitCostsListAdapter(this, allShares);
        expandableListView.setAdapter(adapter);
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

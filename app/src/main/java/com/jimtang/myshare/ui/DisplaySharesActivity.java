package com.jimtang.myshare.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jimtang.myshare.R;
import com.jimtang.myshare.calc.ShareCostsCalculator;
import com.jimtang.myshare.model.Cost;
import com.jimtang.myshare.model.Expense;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by tangz on 10/18/2015.
 */
public class DisplaySharesActivity extends Activity {

    ArrayList<Expense> expenses = Lists.newArrayList();
    Cost totalCost = Cost.FREE;
    ShareCostsCalculator calculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_shares);

        Intent intent = getIntent();
        if (intent != null) {
            totalCost = intent.getParcelableExtra(IntentConstants.TOTAL_COSTS);
            expenses = intent.getParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES);
        }
        calculator = new ShareCostsCalculator(totalCost, expenses);

        List<String> names = Lists.newArrayList(calculator.getParticipants());
        Map<String, Cost> shareMap = calculator.allShares();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.sharesListView);
        SplitCostsListAdapter adapter = new SplitCostsListAdapter(this, names, shareMap);
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

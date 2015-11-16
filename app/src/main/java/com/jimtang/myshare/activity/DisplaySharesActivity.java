package com.jimtang.myshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.adapter.SplitCostsListAdapter;
import com.jimtang.myshare.calc.ShareCostsCalculator;
import com.jimtang.myshare.db.MyShareDbDAO;
import com.jimtang.myshare.listener.SaveShareButtonListener;
import com.jimtang.myshare.model.CumulativeCost;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MyShareSession;
import com.jimtang.myshare.model.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangz on 10/18/2015.
 */
public class DisplaySharesActivity extends AbstractMyShareActivity {

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
        final List<Share> allShares = calculator.allShares();

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.sharesListView);
        SplitCostsListAdapter adapter = new SplitCostsListAdapter(this, allShares);
        expandableListView.setAdapter(adapter);

        Button saveButton = (Button) findViewById(R.id.persist_share_button);
        saveButton.setOnClickListener(new SaveShareButtonListener(this) {
            @Override
            protected void useShareName(String shareName) {
                MyShareSession session = new MyShareSession(shareName, allShares);
                MyShareDbDAO dao = MyShareDbDAO.getInstanceForContext(DisplaySharesActivity.this);
                dao.saveSession(session);
            }
        });
    }
}

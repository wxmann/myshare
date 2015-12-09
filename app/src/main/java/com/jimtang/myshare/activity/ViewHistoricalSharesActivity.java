package com.jimtang.myshare.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.jimtang.myshare.R;
import com.jimtang.myshare.adapter.HistoricalSharesAdapter;
import com.jimtang.myshare.db.GetShareDAO;
import com.jimtang.myshare.model.MyShareSession;

import java.util.List;

public class ViewHistoricalSharesActivity extends AbstractMyShareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_historical_shares);

        ListView listView = (ListView) findViewById(R.id.historicalSharesListView);
        List<MyShareSession> sessions = GetShareDAO.getInstanceForContext(this).getAllSessions();
        listView.setAdapter(new HistoricalSharesAdapter(this, R.layout.display_historical_shares, sessions));
    }
}

package com.jimtang.myshare.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.db.GetShareDAO;
import com.jimtang.myshare.model.MyShareSession;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

public class ViewHistoricalSharesActivity extends AbstractMyShareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_historical_shares);

        List<MyShareSession> sessionList = GetShareDAO.getInstanceForContext(this).getAllSessions();
        List<String> sessionReprs = Lists.newArrayList(
                Collections2.transform(sessionList, new Function<MyShareSession, String>() {
            @Override
            public String apply(MyShareSession input) {
                String timeStampStr = new SimpleDateFormat("MM/dd/yyyy h:mm a").format(input.getTimestamp());
                String firstLine = input.getSessionName() + " | Saved: " + timeStampStr;
                String secondLine = "Involves: " + input.getPeople();
                return firstLine + "\n" + secondLine;
            }
        }));
        ListView listView = (ListView) findViewById(R.id.historicalSharesListView);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sessionReprs);

        listView.setAdapter(adapter);
    }
}

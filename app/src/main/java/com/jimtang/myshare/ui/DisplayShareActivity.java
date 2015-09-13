package com.jimtang.myshare.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.jimtang.myshare.MyShareApplication;
import com.jimtang.myshare.R;

import java.util.List;

/**
 * Created by tangz on 9/9/2015.
 */
public class DisplayShareActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amount_display);

        MyShareApplication app = (MyShareApplication) getApplication();

        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.share_list);
        expListView.setAdapter(assembleExpandableList(app));
    }

    private ExpandableListAdapter assembleExpandableList(MyShareApplication app) {
        List<String> headerStrs = Lists.newArrayList();
        ListMultimap<String, String> children = ArrayListMultimap.create();
        for (String name : app.participants()) {
            String headerStr = assembleHeaderStringForOnePerson(name, app);
            headerStrs.add(headerStr);
            children.putAll(headerStr, assembleChildrenStringForOnePerson(name, app));
        }
        return new ExpandableListImpl(this, headerStrs, children);
    }

    private String assembleHeaderStringForOnePerson(String name, MyShareApplication app) {
        return name + " owes: " + app.totalFor(name);
    }

    private List<String> assembleChildrenStringForOnePerson(String name, MyShareApplication app) {
        String subtotalStr = "Subtotal portion: $" + app.subtotalFor(name);
        String taxStr = "Tax portion: $" + app.taxFor(name);
        String tipStr = "Tip portion: $" + app.tipFor(name);

        return Lists.newArrayList(subtotalStr, taxStr, tipStr);
    }
}

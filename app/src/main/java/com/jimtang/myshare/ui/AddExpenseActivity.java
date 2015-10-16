package com.jimtang.myshare.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;

import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddExpenseActivity extends Activity {

    List<String> nameOptions = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses);

        Intent intent = getIntent();
        if (intent != null) {
            nameOptions = intent.getStringArrayListExtra(IntentConstants.ALL_NAMES);
        }
    }
}

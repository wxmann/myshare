package com.jimtang.myshare.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddPeopleDisplayActivity extends ListActivity {

    ArrayAdapter<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people_display);

        Intent intent = getIntent();
        if (intent != null) {
            String inputName = intent.getStringExtra(IntentConstants.USER_INPUT_NAME);
            nameList = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{inputName});
            setListAdapter(nameList);
        }
    }

    public void addName(String name) {
        nameList.add(name);
    }
}

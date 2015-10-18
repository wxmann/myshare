package com.jimtang.myshare.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.util.ArrayAdapterListMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddPeopleDisplayActivity extends ListActivity {

    private ArrayAdapter<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people_display);

        // receive intent for the first name from the entry screen
        Intent intent = getIntent();
        if (intent != null) {
            String inputName = intent.getStringExtra(IntentConstants.USER_INPUT_NAME);
            nameList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Lists.newArrayList(inputName));
            setListAdapter(nameList);
        }

        // button to add additional names
        Button addMoreNamesButton = (Button) findViewById(R.id.add_more_people_button);
        addMoreNamesButton.setOnClickListener(new NameButtonListener(this) {
            @Override
            protected void doWithInputName(String inputName) {
                nameList.add(inputName);
            }
        });

        // button to get to next activity
        Button nextButton = (Button) findViewById(R.id.add_people_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPeopleDisplayActivity.this, AddExpenseActivity.class);
                intent.putStringArrayListExtra(IntentConstants.ALL_NAMES, ArrayAdapterListMapper.toList(nameList));
                startActivity(intent);
            }
        });
    }
}

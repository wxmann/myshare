package com.jimtang.myshare.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 10/13/2015.
 */
public class AddPeopleEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people_entry);

        Button button = (Button) findViewById(R.id.add_people_button);
        button.setOnClickListener(new NameButtonListener(this) {

            @Override
            protected void doWithInputName(String inputName) {
                Context context = getContext();
                Intent intent = new Intent(context, AddPeopleDisplayActivity.class);
                intent.putExtra(IntentConstants.USER_INPUT_NAME, inputName);
                context.startActivity(intent);
            }
        });
    }
}

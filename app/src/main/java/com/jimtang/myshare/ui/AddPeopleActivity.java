package com.jimtang.myshare.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 11/5/2015.
 */
public class AddPeopleActivity extends Activity {

    static final String PEOPLE_ENTRY_FRAGMENT = "peopleEntryFrag";
    static final String PEOPLE_DISPLAY_FRAGMENT = "peopleDisplayFrag";

    AddPeopleDisplayFragment displayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people);

        // initialize entry fragment
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.people_frag_container, new AddPeopleEntryFragment(), PEOPLE_ENTRY_FRAGMENT)
                .commit();

        // button to add additional names
        Button addMoreNamesButton = (Button) findViewById(R.id.add_more_people_button);
        addMoreNamesButton.setOnClickListener(new NameButtonListener(this) {
            @Override
            protected void doWithInputName(String inputName) {
                Fragment entryFrag = fragmentManager.findFragmentByTag(PEOPLE_ENTRY_FRAGMENT);

                if (entryFrag != null) {
                    displayFragment = new AddPeopleDisplayFragment();
                    // do not add to back-stack, it was just an entry fragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.people_frag_container, displayFragment, PEOPLE_DISPLAY_FRAGMENT)
                            .commit();
                } else {
                    displayFragment = (AddPeopleDisplayFragment) fragmentManager.findFragmentByTag(PEOPLE_DISPLAY_FRAGMENT);
                }
                displayFragment.addName(inputName);
            }
        });

        // button to get to next activity
        Button nextButton = (Button) findViewById(R.id.add_people_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPeopleActivity.this, AddExpenseActivity.class);
                intent.putStringArrayListExtra(IntentConstants.ALL_NAMES, displayFragment.getAllNames());
                startActivity(intent);
            }
        });
    }
}

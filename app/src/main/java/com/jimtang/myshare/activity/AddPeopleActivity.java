package com.jimtang.myshare.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jimtang.myshare.R;
import com.jimtang.myshare.exception.EmptyInputsException;
import com.jimtang.myshare.fragment.AddPeopleDisplayFragment;
import com.jimtang.myshare.fragment.AddPeopleEntryFragment;
import com.jimtang.myshare.listener.AddPersonButtonListener;
import com.jimtang.myshare.listener.ValidatableIntentClickListener;

import java.util.ArrayList;

/**
 * Created by tangz on 11/5/2015.
 */
public class AddPeopleActivity extends AbstractMyShareActivity {

    static final String PEOPLE_ENTRY_FRAGMENT = "peopleEntryFrag";
    static final String PEOPLE_DISPLAY_FRAGMENT = "peopleDisplayFrag";

    AddPeopleDisplayFragment displayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);

        // initialize entry fragment
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.people_frag_container, new AddPeopleEntryFragment(), PEOPLE_ENTRY_FRAGMENT)
                .commit();

        // button to add additional names
        View addMoreNamesButton = findViewById(R.id.add_more_people_button);
        addMoreNamesButton.setOnClickListener(new AddPersonButtonListener(this) {
            @Override
            protected void useInputName(String inputName) {
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
        View nextButton = findViewById(R.id.add_people_next_button);
        nextButton.setOnClickListener(new ValidatableIntentClickListener(AddPeopleActivity.this, AddExpenseActivity.class) {
            @Override
            protected boolean validateInputs() {
                if (displayFragment == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter at least one person.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                ArrayList<String> allNames = displayFragment.getAllNames();
                if (allNames == null || allNames.isEmpty()) {
                    throw new EmptyInputsException("Names should not be empty.");
                }
                return true;
            }

            @Override
            protected void addExtras(Intent intent) {
                intent.putStringArrayListExtra(IntentConstants.ALL_NAMES, displayFragment.getAllNames());
            }
        });
    }
}

package com.jimtang.myshare.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.fragment.AddExpenseDisplayFragment;
import com.jimtang.myshare.fragment.AddExpenseEntryFragment;
import com.jimtang.myshare.listener.ExpenseButtonListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddExpenseActivity extends Activity {

    static final String EXPENSE_ENTRY_FRAGMENT = "expenseEntryFragment";
    static final String EXPENSE_DISPLAY_FRAGMENT = "expenseDisplayFragment";

    FragmentManager fragmentManager;
    List<String> nameOptions = Lists.newArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses);

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.expense_frag_container, new AddExpenseEntryFragment(), EXPENSE_ENTRY_FRAGMENT)
                .commit();

        Intent intent = getIntent();
        if (intent != null) {
            nameOptions = intent.getStringArrayListExtra(IntentConstants.ALL_NAMES);
        }

        View addExpenseButton = findViewById(R.id.add_expense_button);
        addExpenseButton.setOnClickListener(new ExpenseButtonListener(this, nameOptions) {
            @Override
            protected void doWithExpenseObject(Expense expense) {

                Fragment entryFrag = fragmentManager.findFragmentByTag(EXPENSE_ENTRY_FRAGMENT);
                AddExpenseDisplayFragment displayFrag;

                if (entryFrag != null) {
                    displayFrag = new AddExpenseDisplayFragment();

                    // do NOT add to back-stack as we do not want to see the entry screen again, or have it be saved.
                    fragmentManager.beginTransaction()
                            .replace(R.id.expense_frag_container, displayFrag, EXPENSE_DISPLAY_FRAGMENT)
                            .commit();
                } else {
                    displayFrag = (AddExpenseDisplayFragment) fragmentManager.findFragmentByTag(EXPENSE_DISPLAY_FRAGMENT);
                }
                displayFrag.addExpense(expense);
            }
        });

        View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExpenseActivity.this, CumulativeTotalsActivity.class);

                // get the expenses from the fragment, and add them to a parcelable array
                AddExpenseDisplayFragment fragment = (AddExpenseDisplayFragment) fragmentManager.findFragmentByTag(EXPENSE_DISPLAY_FRAGMENT);
                ArrayList<Expense> expenses = fragment.getAddedExpenses();
                intent.putParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES, expenses);

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

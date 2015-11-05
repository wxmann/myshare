package com.jimtang.myshare.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Expense;

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
        setContentView(R.layout.add_expenses);

        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.expense_frag_container, new AddExpenseEntryFragment(), EXPENSE_ENTRY_FRAGMENT)
                .commit();

        Intent intent = getIntent();
        if (intent != null) {
            nameOptions = intent.getStringArrayListExtra(IntentConstants.ALL_NAMES);
        }

        Button addExpenseButton = (Button) findViewById(R.id.add_expense_button);
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

        Button calculateButton = (Button) findViewById(R.id.calculate_button);
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
}

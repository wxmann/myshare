package com.jimtang.myshare.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.exception.EmptyInputsException;
import com.jimtang.myshare.fragment.AddExpenseDisplayFragment;
import com.jimtang.myshare.fragment.AddExpenseEntryFragment;
import com.jimtang.myshare.listener.AddExpenseButtonListener;
import com.jimtang.myshare.listener.ValidatableIntentClickListener;
import com.jimtang.myshare.model.Expense;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddExpenseActivity extends AbstractMyShareActivity {

    static final String EXPENSE_ENTRY_FRAGMENT = "expenseEntryFragment";
    static final String EXPENSE_DISPLAY_FRAGMENT = "expenseDisplayFragment";

    FragmentManager fragmentManager;
    AddExpenseDisplayFragment expensesDisplayFragment;
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
        addExpenseButton.setOnClickListener(new AddExpenseButtonListener(this, nameOptions) {
            @Override
            protected void useExpenseObject(Expense expense) {

                Fragment entryFrag = fragmentManager.findFragmentByTag(EXPENSE_ENTRY_FRAGMENT);

                if (entryFrag != null) {
                    expensesDisplayFragment = new AddExpenseDisplayFragment();

                    // do NOT add to back-stack as we do not want to see the entry screen again, or have it be saved.
                    fragmentManager.beginTransaction()
                            .replace(R.id.expense_frag_container, expensesDisplayFragment, EXPENSE_DISPLAY_FRAGMENT)
                            .commit();
                } else {
                    expensesDisplayFragment = (AddExpenseDisplayFragment)
                            fragmentManager.findFragmentByTag(EXPENSE_DISPLAY_FRAGMENT);
                }
                expensesDisplayFragment.addExpense(expense);
            }
        });

        View calculateButton = findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(new ValidatableIntentClickListener(AddExpenseActivity.this,
                CumulativeTotalsActivity.class) {
            @Override
            protected boolean validateInputs() {
                if (expensesDisplayFragment == null) {
                    Toast.makeText(getApplicationContext(),
                            "Please enter at least one expense to split.", Toast.LENGTH_SHORT).show();
                    return false;
                }

                ArrayList<Expense> allExpenses = expensesDisplayFragment.getAddedExpenses();
                if (allExpenses == null || allExpenses.isEmpty()) {
                    throw new EmptyInputsException("Expenses should not be empty.");
                }
                return true;
            }

            @Override
            protected void addExtras(Intent intent) {
                intent.putParcelableArrayListExtra(IntentConstants.ADDED_EXPENSES,
                        expensesDisplayFragment.getAddedExpenses());
            }
        });
    }
}

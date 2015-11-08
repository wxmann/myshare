package com.jimtang.myshare.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.util.Collections;
import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public abstract class ExpenseButtonListener implements View.OnClickListener {

    static final String INVALID_NAME_MESSAGE =
            "One of the names you entered: %s doesn't match any of the names entered in the previous Add People screen. " +
                    "Please double-check the names you entered.";
    static final String EMPTY_EXPENSE_MESSAGE = "Please empty a non-empty expense name.";
    static final String NEG_AMOUNTS_MESSAGE = "Amounts can't be less than zero.";
    static final String EMPTY_AMOUNT_MESSAGE = "Please enter a non-empty expense amount.";
    static final String EMPTY_NAMES_MESSAGE = "Please enter at least one name.";

    private Context context;
    private List<String> nameOptions;
    AlertDialog alertDialog;

    public ExpenseButtonListener(Context context, List<String> nameOptions) {
        this.context = context;
        this.nameOptions = Collections.unmodifiableList(nameOptions);
    }

    void doWithExpenseObject(Expense expense) {
        if (validateExpense(expense)) {
            useExpenseObject(expense);
            alertDialog.dismiss();
        }
    }

    protected abstract void useExpenseObject(Expense expense);

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        // setup the auto-dropdown menu
        final MultiAutoCompleteTextView enterNamesField = (MultiAutoCompleteTextView)
                dialogView.findViewById(R.id.multiAutoCompleteTextView1);

        enterNamesField.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, nameOptions);
        enterNamesField.setThreshold(1);
        enterNamesField.setAdapter(arrayAdapter);

        // setup the rest of the dialog box
        builder.setTitle(R.string.add_expense_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // hack for validation: do nothing, override in onShowListener
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String commaSepNames = enterNamesField.getText().toString();
                        String[] participantNames = commaSepNames.split("\\s*,\\s*");

                        CheckBox isSharedCheckBox = (CheckBox) dialogView.findViewById(R.id.sharedCheckBox);
                        boolean isShared = isSharedCheckBox.isChecked();

                        EditText inputExpenseNameField = (EditText) dialogView.findViewById(R.id.expense_name_field);
                        String inputExpenseName = inputExpenseNameField.getText().toString();

                        EditText inputExpenseAmtField = (EditText) dialogView.findViewById(R.id.expense_amount_field);
                        String inputExpenseAmtText = inputExpenseAmtField.getText().toString();
                        if (Strings.isNullOrEmpty(inputExpenseAmtText)) {
                            Toast.makeText(context, EMPTY_AMOUNT_MESSAGE, Toast.LENGTH_SHORT)
                                 .show();
                            return;
                        }
                        MonetaryAmount inputExpenseAmt = new MonetaryAmount(inputExpenseAmtText);

                        Expense expense = isShared ?
                                Expense.getSharedByAllInstance(nameOptions.toArray(new String[0]),
                                        inputExpenseName, inputExpenseAmt)
                                : Expense.getInstance(participantNames, inputExpenseName, inputExpenseAmt);

                        doWithExpenseObject(expense);
                    }
                });
            }
        });
        alertDialog.show();
    }

    private boolean validateExpense(Expense expense) {
        // 1. validate the people that have been entered
        // are included in the names entered in the previous screen.
        String[] names = expense.getPeople();
        for (String name: names) {
            if (Strings.isNullOrEmpty(name)) {
                Toast.makeText(context, EMPTY_NAMES_MESSAGE, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!nameOptions.contains(name)) {
                Toast.makeText(context,
                        String.format(INVALID_NAME_MESSAGE, name), Toast.LENGTH_LONG).show();
                return false;
            }
        }

        // 2. validate the name of the expense is not empty
        String expenseName = expense.getExpenseName();
        if (Strings.isNullOrEmpty(expenseName)) {
            Toast.makeText(context,
                    EMPTY_EXPENSE_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }

        // 3. validate the monetary amount is valid.
        // This shouldn't be needed since the input field is unsigned.
        MonetaryAmount amt = expense.getAmount();
        if (amt.compareTo(MonetaryAmount.ZERO) < 0) {
            Toast.makeText(context, NEG_AMOUNTS_MESSAGE, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

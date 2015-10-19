package com.jimtang.myshare.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Expense;
import com.jimtang.myshare.model.MonetaryAmount;

import java.util.Collections;
import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public abstract class ExpenseButtonListener implements View.OnClickListener {

    private Context context;
    private List<String> nameOptions;

    public ExpenseButtonListener(Context context, List<String> nameOptions) {
        this.context = context;
        this.nameOptions = Collections.unmodifiableList(nameOptions);
    }

    protected abstract void doWithExpenseObject(Expense expense);

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.add_expense_dialog, null);

        // setup the auto-dropdown menu
        final MultiAutoCompleteTextView enterNamesField = (MultiAutoCompleteTextView)
                dialogView.findViewById(R.id.multiAutoCompleteTextView1);

        enterNamesField.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, nameOptions);
        enterNamesField.setThreshold(1);
        enterNamesField.setAdapter(arrayAdapter);

        // setup the rest of the dialog box
        builder.setTitle(R.string.add_expense_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String commaSepNames = enterNamesField.getText().toString();
                        String[] participantNames = commaSepNames.split("\\s*,\\s*");

                        CheckBox isSharedCheckBox = (CheckBox) dialogView.findViewById(R.id.sharedCheckBox);
                        boolean isShared = isSharedCheckBox.isChecked();

                        EditText inputExpenseNameField = (EditText) dialogView.findViewById(R.id.expense_name_field);
                        String inputExpenseName = inputExpenseNameField.getText().toString();

                        EditText inputExpenseAmtField = (EditText) dialogView.findViewById(R.id.expense_amount_field);
                        MonetaryAmount inputExpenseAmt = new MonetaryAmount(inputExpenseAmtField.getText().toString());

                        Expense expense = isShared ?
                                Expense.getSharedByAllInstance(nameOptions.toArray(new String[0]), inputExpenseName, inputExpenseAmt)
                                : Expense.getInstance(participantNames, inputExpenseName, inputExpenseAmt);

                        doWithExpenseObject(expense);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog inputNameDialog = builder.create();
        inputNameDialog.show();
    }
}

package com.jimtang.myshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Expense;

import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public class ExpenseCollectionAdapter extends ArrayAdapter<Expense> {

    private List<Expense> expenseList;

    public ExpenseCollectionAdapter(Context context, int resource) {
        super(context, resource);
        expenseList = Lists.newArrayList();
    }

    public ExpenseCollectionAdapter(Context context, int resource, List<Expense> expenseList) {
        super(context, resource, expenseList);
        this.expenseList = expenseList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View newView = convertView;
        if (newView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            newView = inflater.inflate(R.layout.display_expense_item, null);
        }

        Expense expense = expenseList.get(position);
        TextView itemDisplay = (TextView) newView.findViewById(R.id.expense_item_display);
        itemDisplay.setText(getItemString(expense));

        return newView;
    }

    private String getItemString(Expense expense) {
        StringBuilder builder = new StringBuilder();
        builder.append("Item: ").append(expense.getExpenseName()).append("\n");
        builder.append("Paying: ").append(Joiner.on(", ").join(expense.getPeople())).append("\n");
        builder.append("Subtotal amount: ").append(expense.getAmount().toFormattedString());
        return builder.toString();
    }
}

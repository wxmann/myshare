package com.jimtang.myshare.ui;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.model.Expense;

import java.util.List;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddExpenseDisplayFragment extends ListFragment {

    List<Expense> preloadedExpenses = Lists.newArrayList();
    ExpenseCollectionAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_expenses_display_frag, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);

        adapter = new ExpenseCollectionAdapter(getActivity(), R.layout.expense_listitem, preloadedExpenses);
        listView.setAdapter(adapter);
        return view;
    }

    public void addExpense(Expense expense) {
        if (adapter == null) {
            preloadedExpenses.add(expense);
        } else {
            adapter.add(expense);
        }
    }
}

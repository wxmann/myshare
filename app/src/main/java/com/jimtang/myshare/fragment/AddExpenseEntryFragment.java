package com.jimtang.myshare.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 10/16/2015.
 */
public class AddExpenseEntryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_expenses_entry, container, false);
    }
}

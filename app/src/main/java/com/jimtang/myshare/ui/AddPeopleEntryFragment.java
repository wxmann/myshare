package com.jimtang.myshare.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 11/5/2015.
 */
public class AddPeopleEntryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_people_entry_frag, container, false);
    }
}

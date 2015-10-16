package com.jimtang.myshare.ui;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.jimtang.myshare.R;
import com.jimtang.myshare.ui.listener.DialogListener;

/**
 * Created by tangz on 10/14/2015.
 */
public class AddPeopleDisplayFragment extends ListFragment {

    ArrayAdapter<String> nameList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_people_display_frag, container, false);
        nameList = new ArrayAdapter<String>(getContext(), R.layout.add_people_display_frag);
        return view;
    }

    public void addName(String name) {
        nameList.add(name);
    }

}

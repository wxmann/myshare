package com.jimtang.myshare.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.collect.Lists;
import com.jimtang.myshare.R;
import com.jimtang.myshare.util.ArrayAdapterListMapper;

import java.util.ArrayList;

/**
 * Created by tangz on 11/5/2015.
 */
public class AddPeopleDisplayFragment extends Fragment {

    ArrayList<String> preloadedPeople = Lists.newArrayList();
    ArrayAdapter<String> nameList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_people_display_frag, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);

        nameList = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, preloadedPeople);
        listView.setAdapter(nameList);
        return view;
    }

    public void addName(String name) {
        if (nameList == null) {
            preloadedPeople.add(name);
        } else {
            nameList.add(name);
        }
    }

    public ArrayList<String> getAllNames() {
        return nameList == null ? preloadedPeople : ArrayAdapterListMapper.toList(nameList);
    }
}

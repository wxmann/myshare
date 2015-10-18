package com.jimtang.myshare.util;

import android.widget.ArrayAdapter;

import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by tangz on 10/16/2015.
 */
public final class ArrayAdapterListMapper {

    private ArrayAdapterListMapper() {
    }

    public static <T> ArrayList<T> toList(ArrayAdapter<T> arrayAdapter) {
        ArrayList<T> list = Lists.newArrayList();
        for (int i = 0; i < arrayAdapter.getCount(); i++) {
            list.add(arrayAdapter.getItem(i));
        }
        return list;
    }
}

package com.jimtang.myshare.ui;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 9/13/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class IndivPurchasesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.indiv_amts_frag, container, false);
    }
}

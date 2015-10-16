package com.jimtang.myshare.ui;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.jimtang.myshare.R;
import com.jimtang.myshare.ui.listener.DialogListener;

/**
 * Created by tangz on 10/13/2015.
 */
public class AddPeopleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people_main);
    }
}

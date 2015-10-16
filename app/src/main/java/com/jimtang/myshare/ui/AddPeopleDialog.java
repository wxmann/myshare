package com.jimtang.myshare.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jimtang.myshare.R;
import com.jimtang.myshare.ui.listener.DialogListener;

/**
 * Created by tangz on 10/13/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AddPeopleDialog extends DialogFragment {

    DialogListener addPeopleDialogListener;
    View dialogView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_person_dialog, null);

        builder.setTitle(R.string.add_people_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addPeopleDialogListener.onDialogPositiveClick(AddPeopleDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addPeopleDialogListener.onDialogNegativeClick(AddPeopleDialog.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // We assume that the dialog listener is the entry fragment to the AddPeopleActivity.
        // If this assumption changes, we need to make changes here accordingly.
        addPeopleDialogListener = (DialogListener) getTargetFragment();
    }
}

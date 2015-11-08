package com.jimtang.myshare.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.common.base.Strings;
import com.jimtang.myshare.R;

/**
 * Created by tangz on 10/16/2015.
 */
public abstract class NameButtonListener implements View.OnClickListener {

    private Context context;
    View dialogView;
    AlertDialog alertDialog;

    public NameButtonListener(Context context) {
        this.context = context;
    }

    void doWithInputName(String inputName) {
        if (validateInputName(inputName)) {
            useInputName(inputName);
            alertDialog.dismiss();
        }
    }

    protected abstract void useInputName(String inputName);

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.dialog_add_person, null);

        builder.setTitle(R.string.add_people_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // overriden in the show listener - hack for the validation portion.
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText userInput = (EditText) dialogView.findViewById(R.id.add_person_name);
                        String inputName = userInput.getText().toString();
                        doWithInputName(inputName);
                    }
                });
            }
        });
        alertDialog.show();
    }

    private boolean validateInputName(String inputName) {
        if (Strings.isNullOrEmpty(inputName)) {
            Toast.makeText(alertDialog.getContext(),
                    "Please enter a non-empty name.", Toast.LENGTH_SHORT).show();
            return false;
        } else if(inputName.contains(",")) {
            Toast.makeText(alertDialog.getContext(),
                    "Inputted names cannot enter a comma. Please change the name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

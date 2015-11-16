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
 * Created by tangz on 11/15/2015.
 */
public abstract class SaveShareButtonListener implements View.OnClickListener {

    private Context context;
    AlertDialog alertDialog;

    public SaveShareButtonListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_save_share, null);

        builder.setTitle("Save Share")
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
                        EditText userInput = (EditText) dialogView.findViewById(R.id.save_share_name);
                        String shareName = userInput.getText().toString();
                        doWithShareName(shareName);
                    }
                });
            }
        });
        alertDialog.show();
    }

    private boolean validateInputName(String inputName) {
        if (Strings.isNullOrEmpty(inputName)) {
            Toast.makeText(alertDialog.getContext(),
                    "Please enter a non-empty share name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void doWithShareName(String shareName) {
        if (validateInputName(shareName)) {
            useShareName(shareName);
            alertDialog.dismiss();
        }
    }

    protected abstract void useShareName(String shareName);
}

package com.jimtang.myshare.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jimtang.myshare.R;

/**
 * Created by tangz on 10/16/2015.
 */
public abstract class NameButtonListener implements View.OnClickListener {

    private Context context;

    public NameButtonListener(Context context) {
        this.context = context;
    }

    public final Context getContext() {
        return context;
    }

    protected abstract void doWithInputName(String inputName);

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View dialogView = inflater.inflate(R.layout.dialog_add_person, null);

        builder.setTitle(R.string.add_people_dialog_title)
                .setView(dialogView)
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText userInput = (EditText) dialogView.findViewById(R.id.add_person_name);
                        String inputName = userInput.getText().toString();
                        doWithInputName(inputName);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog inputNameDialog = builder.create();
        inputNameDialog.show();
    }
}

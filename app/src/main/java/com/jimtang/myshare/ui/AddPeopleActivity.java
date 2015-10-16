package com.jimtang.myshare.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jimtang.myshare.R;
import com.jimtang.myshare.ui.listener.DialogListener;

/**
 * Created by tangz on 10/13/2015.
 */
public class AddPeopleActivity extends Activity {

    String inputName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_people_entry);

        Button button = (Button) findViewById(R.id.add_people_button);
        button.setOnClickListener(new NameButtonListener(this));
    }

    private static class NameButtonListener implements View.OnClickListener {

        private Context context;

        private NameButtonListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.add_person_dialog, null);

            builder.setTitle(R.string.add_people_dialog_title)
                    .setView(dialogView)
                    .setPositiveButton(R.string.done, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText userInput = (EditText) dialogView.findViewById(R.id.add_person_name);
                            String inputName = userInput.getText().toString();
                            Intent intent = new Intent(context, DisplayPeopleActivity.class);
                            intent.putExtra(IntentConstants.USER_INPUT_NAME, inputName);
                            context.startActivity(intent);
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
}

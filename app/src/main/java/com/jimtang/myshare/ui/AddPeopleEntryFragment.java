package com.jimtang.myshare.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jimtang.myshare.R;
import com.jimtang.myshare.ui.listener.DialogListener;

/**
 * Created by tangz on 10/13/2015.
 */
public class AddPeopleEntryFragment extends Fragment implements View.OnClickListener, DialogListener {

    Button addPeopleButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_people_entry_frag, container, false);
        addPeopleButton = (Button) view.findViewById(R.id.add_people_button);
        addPeopleButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        showAddPeopleDialog();
    }

    private void showAddPeopleDialog() {
        DialogFragment addPeopleDialog = new AddPeopleDialog();
        addPeopleDialog.show(getFragmentManager(), "AddPeople");
        addPeopleDialog.setTargetFragment(this, 0);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment fragment) {
        EditText nameTextField = (EditText) fragment.getView().findViewById(R.id.add_person_name);
        // TODO: handle null
        String name = nameTextField.getText().toString();

        AddPeopleDisplayFragment displayFragment = new AddPeopleDisplayFragment();
        displayFragment.addName(name);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.add_people_entry_fragment, displayFragment)
                .commit();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment fragment) {
        // do nothing
    }


}

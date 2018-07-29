package com.udacity.surbi.listnow.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.udacity.surbi.listnow.R;


public class SearchDialogFragment extends DialogFragment {
    private static final String KEY_NEW_NAME = "new_name";
    EditText etNewName;
    String newName;


    public SearchDialogFragment() {
    }

    public static SearchDialogFragment newInstance() {
        SearchDialogFragment frag = new SearchDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            newName = savedInstanceState.getString(KEY_NEW_NAME);
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Search";
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        etNewName = new EditText(getContext());
        etNewName.setHint(getString(R.string.home_dialog_rename_new_name));
        if (newName != null){
            etNewName.setText(newName);
        }
        FrameLayout container = new FrameLayout(getContext());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(getResources().getDimensionPixelSize(R.dimen.dialog_margin));
        params.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.dialog_margin));

        etNewName.setLayoutParams(params);
        container.addView(etNewName);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setView(container);

        alertDialogBuilder.setPositiveButton(getString(R.string.accept),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (etNewName.getText().toString().length() > 0) {
                    SearchDialogListener listener = (SearchDialogListener) getActivity();
                    if (listener != null) {
                        listener.onFinishSearchDialog(etNewName.getText().toString());
                    }
                    dismiss();
                } else {
                    etNewName.setHint(getString(R.string.home_dialog_rename_new_name_please));
                }

            }
        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null && getShowsDialog()) {
                    dialog.dismiss();
                }
            }

        });

        return alertDialogBuilder.create();
    }

    public interface SearchDialogListener {
        void onFinishSearchDialog(String inputText);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_NEW_NAME, etNewName.getText().toString());
    }

}
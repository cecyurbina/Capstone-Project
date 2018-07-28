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


public class RenameDialogFragment extends DialogFragment {
    private static final String KEY_TITLE = "title";
    private static final String KEY_ID = "id";
    private static final String KEY_NEW_NAME = "new_name";
    EditText etNewName;
    String newName;


    public RenameDialogFragment() {
    }

    public static RenameDialogFragment newInstance(String title, String id) {
        RenameDialogFragment frag = new RenameDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_ID, id);
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
        String title = getArguments().getString(KEY_TITLE);
        final String id = getArguments().getString(KEY_ID);
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
                RenameDialogListener listener = (RenameDialogListener) getTargetFragment();
                if (listener != null) {
                    listener.onFinishRenameDialog(etNewName.getText().toString(), id);
                }
                dismiss();

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

    public interface RenameDialogListener {
        void onFinishRenameDialog(String inputText, String id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_NEW_NAME, etNewName.getText().toString());
    }

}
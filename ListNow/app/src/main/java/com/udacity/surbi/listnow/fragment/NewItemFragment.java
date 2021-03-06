package com.udacity.surbi.listnow.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.activity.NewItemActivity;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.utils.DatabaseHelper;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemFragment extends Fragment {
    private Unbinder unbinder;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_quantity)
    EditText etQuantity;
    @BindView(R.id.et_unit)
    EditText etUnit;
    @BindView(R.id.switch_image)
    Switch switchImage;
    @BindView(R.id.button_cancel)
    Button bCancel;
    @BindView(R.id.button_accept)
    Button bAccept;
    private OnNewItemFragmentInteractionListener listener;
    private Item item;

    public NewItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //enable button if input is not empty
                if (etName.getText().length() > 0) {
                    bAccept.setEnabled(true);
                } else {
                    bAccept.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        switchImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    switchImage.setContentDescription(getString(R.string.check_item));
                } else {
                    switchImage.setContentDescription(getString(R.string.uncheck_item));
                }
            }
        });
        if (listener.isEdition()) {
            fillFields();
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public interface OnNewItemFragmentInteractionListener {
        String getListKey();

        boolean isEdition();

        String getItemString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (NewItemActivity) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @OnClick(R.id.button_accept)
    void submitButton(View view) {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        getItem();
        if (listener.isEdition()) {
            databaseHelper.updateItem(listener.getListKey(), item);
        } else {
            databaseHelper.addItemToList(listener.getListKey(), item);
        }
        if (getActivity() != null) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(NewListFragment.KEY_RESULT_ITEM, getItemString());
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
    }

    @OnClick(R.id.button_cancel)
    void cancelButton(View view) {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void getItem() {
        String keyItem = null;
        if (item != null) {
            keyItem = item.getKey();
        }
        item = new Item();
        item.setKey(keyItem);
        item.setName(etName.getText().toString());
        if (etQuantity.getText().toString().length() > 0) {
            item.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
        }
        item.setUnit(etUnit.getText().toString());
        item.setRejected(false);
        item.setImage(switchImage.isChecked());
        item.setChecked(false);

    }

    private String getItemString() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(item);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    private void getItemFromString() {
        ObjectMapper mapper = new ObjectMapper();
        //JSON from String to Object
        try {
            item = mapper.readValue(listener.getItemString(), Item.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillFields() {
        getItemFromString();
        if (item.getName() != null) {
            etName.setText(item.getName());
        }
        if (item.getQuantity() != null) {
            etQuantity.setText(String.valueOf(item.getQuantity()));
        }
        if (item.getUnit() != null) {
            etUnit.setText(item.getUnit());
        }
        switchImage.setChecked(item.getImage());
    }
}

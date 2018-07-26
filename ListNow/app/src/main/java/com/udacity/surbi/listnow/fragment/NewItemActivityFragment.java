package com.udacity.surbi.listnow.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.udacity.surbi.listnow.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemActivityFragment extends Fragment {
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

    public NewItemActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}

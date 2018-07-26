package com.udacity.surbi.listnow.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.surbi.listnow.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewItemActivityFragment extends Fragment {

    public NewItemActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_item, container, false);
    }
}

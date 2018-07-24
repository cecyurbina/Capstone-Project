package com.udacity.surbi.listnow.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.surbi.listnow.fragment.CheckListFragment;
import com.udacity.surbi.listnow.R;

public class CheckListContainerActivity extends AppCompatActivity implements CheckListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

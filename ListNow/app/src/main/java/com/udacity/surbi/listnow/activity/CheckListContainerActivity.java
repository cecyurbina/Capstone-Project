package com.udacity.surbi.listnow.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.surbi.listnow.data.ListStructure;
import com.udacity.surbi.listnow.fragment.CheckListFragment;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.fragment.ListHomeFragment;

import java.io.IOException;

public class CheckListContainerActivity extends AppCompatActivity implements CheckListFragment.OnFragmentInteractionListener {
    private String jsonList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        jsonList = intent.getStringExtra(ListHomeFragment.KEY_LIST_JSON);
        setContentView(R.layout.activity_check_list);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public String getList() {
        return jsonList;
    }
}

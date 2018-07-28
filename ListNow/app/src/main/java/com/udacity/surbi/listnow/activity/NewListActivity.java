package com.udacity.surbi.listnow.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.fragment.ListHomeFragment;
import com.udacity.surbi.listnow.fragment.NewListFragment;
import com.udacity.surbi.listnow.utils.DatabaseHelper;

public class NewListActivity extends AppCompatActivity implements NewListFragment.OnFragmentInteractionListener {
    private String jsonList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        jsonList = intent.getStringExtra(ListHomeFragment.KEY_LIST_JSON);

        setContentView(R.layout.activity_new_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public String getList() {
        return jsonList;
    }

    @Override
    public boolean isNewList() {
        return jsonList == null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

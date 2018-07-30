package com.udacity.surbi.listnow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.fragment.NewItemFragment;
import com.udacity.surbi.listnow.fragment.NewListFragment;

import java.util.Objects;

public class NewItemActivity extends AppCompatActivity implements NewItemFragment.OnNewItemFragmentInteractionListener {
    private String key;
    private String itemToEditString;
    private boolean isEdition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            key = b.getString(NewListFragment.KEY_LIST_ID);
            isEdition = b.getBoolean(NewListFragment.KEY_EDITION, false);
            itemToEditString = b.getString(NewListFragment.KEY_ITEM, "");
        }
        setContentView(R.layout.activity_new_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public String getListKey() {
        return key;
    }

    @Override
    public boolean isEdition() {
        return isEdition;
    }

    @Override
    public String getItemString() {
        return itemToEditString;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = NavUtils.getParentActivityIntent(this);
            Objects.requireNonNull(intent).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

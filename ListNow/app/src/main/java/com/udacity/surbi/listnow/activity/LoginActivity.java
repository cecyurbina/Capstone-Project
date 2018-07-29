package com.udacity.surbi.listnow.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

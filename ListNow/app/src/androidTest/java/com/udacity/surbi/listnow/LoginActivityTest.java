package com.udacity.surbi.listnow;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;


import com.udacity.surbi.listnow.activity.LoginActivity;
import com.udacity.surbi.listnow.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/*
Test phone view opens an activity to see steps details and tablet view shows details on same view
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private static final String MESSAGE = "This is a test";
    private static final String PACKAGE_NAME = "com.udacity.surbi.listnow";

    /* Instantiate an IntentsTestRule object. */
    @Rule
    public IntentsTestRule<MainActivity> mIntentsRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void verifyLoginActivity() {

        MainActivity act = mIntentsRule.getActivity();
        act.showLoginScreen();

        intended(hasComponent(LoginActivity.class.getName()));
    }
}

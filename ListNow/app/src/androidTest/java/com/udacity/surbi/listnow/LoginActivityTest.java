package com.udacity.surbi.listnow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;


import com.udacity.surbi.listnow.activity.LoginActivity;
import com.udacity.surbi.listnow.activity.MainActivity;
import com.udacity.surbi.listnow.activity.NewListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.udacity.surbi.listnow.fragment.ListHomeFragment.KEY_LIST_JSON;
import static org.hamcrest.core.AllOf.allOf;

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

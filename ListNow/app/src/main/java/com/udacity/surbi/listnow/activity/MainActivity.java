package com.udacity.surbi.listnow.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.fragment.EmptyHomeFragment;
import com.udacity.surbi.listnow.fragment.ListHomeFragment;
import com.udacity.surbi.listnow.fragment.SearchDialogFragment;
import com.udacity.surbi.listnow.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, EmptyHomeFragment.OnFragmentInteractionListener, ListHomeFragment.OnFragmentInteractionListener, SearchDialogFragment.SearchDialogListener {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            ListHomeFragment firstFragment = new ListHomeFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_new_list:
                createList();
                break;
            case R.id.nav_search:
                showSearchDialog();
                break;
            case R.id.nav_logout:
                signOut();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createList() {
        Intent intent = new Intent(getApplicationContext(), NewListActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /**
     * Show dialog to enter new name
     */
    private void showSearchDialog() {
        FragmentManager fm = getSupportFragmentManager();
        SearchDialogFragment alertDialog = SearchDialogFragment.newInstance();
        alertDialog.show(fm, "fragment_alert");
    }

    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                showLoginScreen();
            }
        });
    }

    public void showLoginScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            showLoginScreen();
        } else {
            showLists();
        }
    }

    @Override
    public void onFinishSearchDialog(final String inputText) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("lists").child(inputText);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string.home_list_added), Toast.LENGTH_LONG).show();
                    DataSnapshot usersData = dataSnapshot.child(inputText).child("users");
                    List<String> newUser = new ArrayList<>();
                    for (DataSnapshot userData : usersData.getChildren()) {
                        String tempUser = (String) userData.getValue();
                        newUser.add(tempUser);
                    }
                    newUser.add(currentUser.getUid());
                    DatabaseHelper databaseHelper = new DatabaseHelper();
                    databaseHelper.addUserList(newUser, inputText);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.home_list_not_foud), Toast.LENGTH_LONG).show();
                }
                showLists();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), getString(R.string.home_list_not_foud), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showEmptyMessage() {
        EmptyHomeFragment firstFragment = new EmptyHomeFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }

    private void showLists(){
        ListHomeFragment firstFragment = new ListHomeFragment();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, firstFragment).commit();
    }


    @Override
    public void newList() {
        createList();
    }

    @Override
    public void searchList() {
        showSearchDialog();
    }
}

package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView navBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ServerRequester.IsValidToken(getApplicationContext());

        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.bottomBar);
        navBar.setOnNavigationItemSelectedListener(selectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.trips_page: {
                    if (!ServerRequester.IsValidToken(getApplicationContext())) //TODO
                        fragment = new NoAccountFragment();
                    else
                        fragment = new tripsFragment();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.favorites_page: {
                    if (!ServerRequester.IsValidToken(getApplicationContext())) //TODO
                        fragment = new NoAccountFragment();
                    else
                        fragment = new favoritesFragment();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.main_page: {
                    fragment = new MainFragment();
                    loadFragment(fragment);
                    return true;
                }
                case R.id.profile_page: {
                    if (!ServerRequester.IsValidToken(getApplicationContext())) //TODO
                        fragment = new NoAccountFragment();
                    else
                        fragment = new profileFragment();
                    loadFragment(fragment);
                    return true;
                }
                default: {
                }
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.default_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
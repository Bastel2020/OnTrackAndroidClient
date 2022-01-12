package com.bastel2020.ontrack;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView navBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        ServerRequester.IsValidToken(getApplicationContext());

        ServerRequester.SyncFavorites(getApplicationContext());

        setContentView(R.layout.activity_main);

        navBar = findViewById(R.id.bottomBar);
        navBar.setOnNavigationItemSelectedListener(selectedListener);

        Helpers.loadFragment(new MainFragment(), getSupportFragmentManager());
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
                    Helpers.loadFragment(fragment, getSupportFragmentManager());
                    return true;
                }
                case R.id.favorites_page: {
                    if (!ServerRequester.IsValidToken(getApplicationContext())) //TODO
                        fragment = new NoAccountFragment();
                    else
                        fragment = new favoritesFragment();
                    Helpers.loadFragment(fragment, getSupportFragmentManager());
                    return true;
                }
                case R.id.main_page: {
                    fragment = new MainFragment();
                    Helpers.loadFragment(fragment, getSupportFragmentManager());
                    return true;
                }
                case R.id.profile_page: {
                    if (!ServerRequester.IsValidToken(getApplicationContext())) //TODO
                        fragment = new NoAccountFragment();
                    else
                        fragment = new profileFragment();
                    Helpers.loadFragment(fragment, getSupportFragmentManager());
                    return true;
                }
                default: {
                }
            }
            return false;
        }
    };
}
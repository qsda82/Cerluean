package com.example.datingapptest;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);



        //I added this if statement to keep the selected fragment when rotating the device
        bottomNav.getMenu().getItem(2).setChecked(true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new com.example.datingapptest.Fragment.HomeFragment()).commit();

        switch_activity();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new com.example.datingapptest.Fragment.HomeFragment();
                            break;
                        case R.id.nav_account:
                            selectedFragment = new com.example.datingapptest.Fragment.AccountFragment();
                            break;
                        case R.id.nav_personal_data:
                            selectedFragment = new com.example.datingapptest.Fragment.PersonalDataFragment();
                            break;
                        case R.id.nav_message:
                            selectedFragment = new com.example.datingapptest.Fragment.MessageFragment();
                            break;
                        case R.id.nav_match:
                            selectedFragment = new com.example.datingapptest.Fragment.MatchFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    private void switch_activity() {
        int id = getIntent().getIntExtra("id", 0);
        if (id == 1) {
            bottomNav.getMenu().getItem(1).setChecked(true);
            Fragment selectedFragment = new com.example.datingapptest.Fragment.PersonalDataFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    selectedFragment).commit();

        }
    }
}

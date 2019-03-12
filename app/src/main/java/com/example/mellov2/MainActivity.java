package com.example.mellov2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_current_status:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new CurrentStatusFragment()).commit();
                    return true;
                case R.id.navigation_trends_stats:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new TrendsStatsFragment()).commit();
                    return true;
                case R.id.navigation_calibration:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new CalibrationFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_container,
                            new SettingsFragment()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_current_status);
        }
        navigation.setSelectedItemId(R.id.navigation_current_status);

    }

}
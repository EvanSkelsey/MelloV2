package com.example.mellov2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v14.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //Navigation Bar
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_current_status);
        }
        navigation.setSelectedItemId(R.id.navigation_current_status);

    }

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
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                            new TrendsStatsFragment()).commit();
                    return true;
                case R.id.navigation_calibration:
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                            new CalibrationFragment()).commit();
                    return true;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().add(R.id.main_container,
                            new SettingsFragment()).commit();
                    return true;
            }
            return false;
        }
    };
    

}

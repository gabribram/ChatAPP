package com.gabri.gpschat;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gabri.gpschat.fragment.AvailableFragment;
import com.gabri.gpschat.fragment.ContacteFragment;
import com.gabri.gpschat.fragment.SettingFragment;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.Utils;
import com.google.firebase.FirebaseApp;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {
    NearByFragment nearByFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        int badgeCount = 4;
        ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+
//        ShortcutBadger.removeCount(MainActivity.this);
//        ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3
//        Intent intent=new Intent(MainActivity.this, MapsActivity.class);
//        startActivity(intent);
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_score, R.drawable.ic_nearyby, R.color.color_tab_4);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_assets, R.drawable.alert, R.color.color_tab_2);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.nav_control, R.drawable.control, R.color.color_tab_5);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.nav_settings, R.drawable.setting, R.color.color_tab_1);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(0);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Add or remove notification for each item
        bottomNavigation.setNotification("12", 1);
//        bottomNavigation.setNotification("", 1);
        if (nearByFragment==null)nearByFragment=new NearByFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, nearByFragment).commit();


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
//                // selection code...
                if (position == 0) {


                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, nearByFragment).commit();

                }
                else if (position == 1) {

                    Utils.setToPrefString(Constants.KEY_FRAGMENTFLAG,"message",MainActivity.this);
                    Utils.setToPrefString(Constants.KEY_SEND_COUND,"10",MainActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new AvailableFragment()).commit();


                }
                else if (position == 2) {
                    Utils.setToPrefString(Constants.KEY_FRAGMENTFLAG,"contact",MainActivity.this);
                    Utils.setToPrefString(Constants.KEY_SEND_COUND,"15",MainActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new AvailableFragment()).commit();


                }
                else if (position == 3) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new SettingFragment()).commit();

                }
//
                return true;
            }
        });


    }
}

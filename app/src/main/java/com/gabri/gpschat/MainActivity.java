package com.gabri.gpschat;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gabri.gpschat.fragment.AvailableFragment;
import com.gabri.gpschat.fragment.ContacteFragment;
import com.gabri.gpschat.fragment.SettingFragment;
import com.gabri.gpschat.model.RecentModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.Utils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends AppCompatActivity {
    AvailableFragment availeFragment;
    AHBottomNavigation bottomNavigation;
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
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
//        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_score, R.drawable.ic_nearyby, R.color.color_tab_4);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.nav_control, R.drawable.ic_message, R.color.color_tab_4);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.nav_settings, R.drawable.setting, R.color.color_tab_2);

// Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);



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


//        bottomNavigation.setNotification("", 1);
        if (availeFragment==null)availeFragment=new AvailableFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, availeFragment).commit();


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
//                // selection code...
                if (position == 0) {


                    Utils.setToPrefString(Constants.KEY_FRAGMENTFLAG,"message",MainActivity.this);
                    Utils.setToPrefString(Constants.KEY_SEND_COUND,"10",MainActivity.this);
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer,availeFragment).commit();

                }
                else if (position == 1) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.contentContainer, new SettingFragment()).commit();


                }


//
                return true;
            }
        });

     load_badge();
    }
    public void load_badge(){


        Query query = FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).orderByChild("userId").equalTo(Utils.getFromPref(Constants.USER_ID,MainActivity.this));
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int total_unread_count=0;
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    RecentModel model = post.getValue(RecentModel.class);
                    if (model!=null){
                        total_unread_count+=Integer.parseInt(model.getUnread_count_message());
                    }


                }
                if (total_unread_count>0){
                    bottomNavigation.setNotification(Integer.toString(total_unread_count), 0);
                }
                else{
                    bottomNavigation.setNotification("", 0);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("database", databaseError.toString());
            }
        });



    }
    public void  reailtime_location_put(){


    }
}

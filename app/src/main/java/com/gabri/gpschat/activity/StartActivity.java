package com.gabri.gpschat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gabri.gpschat.MainActivity;
import com.gabri.gpschat.R;
import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        getUserSession();
    }
    public void getUserSession(){


        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent=new Intent(StartActivity.this,MainActivity.class);
            startActivity(intent);
        }
        else {

            Intent intent=new Intent(StartActivity.this,LoginActivity.class);
            startActivity(intent);

        }

    }
}

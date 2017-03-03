package com.gabri.gpschat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.gabri.gpschat.MainActivity;
import com.gabri.gpschat.R;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.GPSTracker;
import com.gabri.gpschat.utility.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Arrays;
import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "facebook";
    EditText email_edittext,password_edittext;
    Button login_button;
    TextView signup_textview;
    String email, password;
    private AVLoadingIndicatorView avi;
    FirebaseAuth mAuth;
    SharedPreferences cookies_string;
    SharedPreferences.Editor editor;
    DatabaseReference userDatabase;
    private boolean facebookLogined;

    GPSTracker gpsTracker;
    CallbackManager mCallbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gpsTracker = new GPSTracker(this);
        userDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE);
        cookies_string = getSharedPreferences(Constants.KEY_COOKIES, Context.MODE_PRIVATE);
        editor = cookies_string.edit();
        avi= (AVLoadingIndicatorView) findViewById(R.id.avi);
        mAuth = FirebaseAuth.getInstance();
        login_button=(Button)findViewById(R.id.login_button);
        email_edittext=(EditText)findViewById(R.id.email_editText);
        password_edittext=(EditText)findViewById(R.id.password_editText);
        signup_textview=(TextView)findViewById(R.id.signup_textView);
        signup_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });

        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onSuccess:" + error.getLocalizedMessage());
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token)
    {
        if (token == null) return;
        AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful())
                {

                }
                else
                {
                    Utils.setToPrefString(Constants.FACEBOOK, "true", LoginActivity.this);
                    FirebaseUser user = task.getResult().getUser();
                    String uid = user.getUid();
                    UserModel userModel = new UserModel();
                    userModel.setObjectId(uid);
                    userModel.setEmail(user.getEmail());
                    userModel.setFirstName(user.getDisplayName());
                    userModel.setLastName("");
                    userModel.setLatitude("" + gpsTracker.getLatitude());
                    userModel.setLongitude("" + gpsTracker.getLongitude());
                    String date = Calendar.getInstance().getTime().getTime() + "";
                    userModel.setCreateAt(date);
                    userModel.setUpdateAt(date);
                    userModel.setNet_status("1");
                    userDatabase.child(uid).setValue(userModel.getHashMap());
                    editor.putString(Constants.USER_ID,uid);
                    editor.putString(Constants.KEY_FIRSTNAME,user.getDisplayName());
                    editor.putString(Constants.KEY_LASTNAME,"");
                    editor.putString(Constants.KEY_USERMAIL, user.getEmail());
                    editor.putString(Constants.KEY_PHOTOURL, user.getPhotoUrl().toString());
                    editor.commit();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    ProgressDialog progressDialog;


    public void OnFacebook(View view)
    {
        if (mAuth.getCurrentUser() == null || !Utils.getFromPref(Constants.FACEBOOK, this).equals("true"))
        {
            FacebookLogin();
        }
        else
        {
           // Toast.makeText(this, "Logined", Toast.LENGTH_SHORT).show();
        }
//        else
//        {
//            mAuth.signOut();
//            if (AccessToken.getCurrentAccessToken() != null)
//            {
//                LoginManager.getInstance().logOut();
//            }
//            Utils.setToPrefString(Constants.FACEBOOK, "false", this);
//        }
    }

    private void FacebookLogin() {
//        Toast.makeText(this, "face", Toast.LENGTH_SHORT).show();
        if (isFacebookLogined()) {
//            Toast.makeText(this, "face_logout", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logOut();
        }
        else
        {
//            Toast.makeText(this, "face_login", Toast.LENGTH_SHORT).show();
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));
        }
    }

    private void login()
    {
        if (!isvalidate()) return;
        avi.show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressDialog.dismiss();
                if (task.isSuccessful())
                {
                    AuthResult result = task.getResult();
//                    Toast.makeText(getApplicationContext(), result.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                    UserInformationRead(task.getResult().getUser());

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                avi.hide();
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void UserInformationRead(final FirebaseUser user) {
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                avi.hide();
                try {
                    UserModel model = dataSnapshot.getValue(UserModel.class);
                    Log.d("value", dataSnapshot.toString());

                    if (model == null) {

                    } else {
                        editor.putString(Constants.KEY_FIRSTNAME,model.getFirstName());
                        editor.putString(Constants.KEY_LASTNAME,model.getLastName());
                        editor.putString(Constants.KEY_USERMAIL,model.getEmail());
                        editor.putString(Constants.KEY_PHOTOURL,model.getPhotoURL());
                        editor.putString(Constants.USER_ID, model.getObjectId());
                        editor.commit();
                        String uid = user.getUid();
                        UserModel userModel = new UserModel();
                        userModel.setObjectId(uid);
                        userModel.setEmail(model.getEmail());
                        userModel.setFirstName(model.getFirstName());
                        userModel.setLastName(model.getLastName());
                        userModel.setPhotoURL(model.getPhotoURL());
                        String date = Calendar.getInstance().getTime().getTime() + "";
                        userModel.setCreateAt(date);
                        userModel.setUpdateAt(date);
                        userModel.setNet_status("1");
                        userDatabase.child(uid).setValue(userModel.getHashMap());
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
                catch (Exception e){};
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "User null", Toast.LENGTH_SHORT).show();
            }
        });


    }

    boolean isvalidate()
    {
        email = email_edittext.getText().toString().trim();
        password = password_edittext.getText().toString().trim();
        if (email.equals("")){
            email_edittext.setError("Please email address");
            return false;
        }
        if (password.equals("")){
            password_edittext.setError("Please enter password");
            return false;}
        if (password.length()<6){
            password_edittext.setError("Password length should be 6 more");
            return false;}
        return true;
    }

    public boolean isFacebookLogined() {
        if (AccessToken.getCurrentAccessToken() != null) return true;
        return false;
    }
}

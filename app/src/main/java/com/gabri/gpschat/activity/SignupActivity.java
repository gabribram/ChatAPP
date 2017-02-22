package com.gabri.gpschat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabri.gpschat.MainActivity;
import com.gabri.gpschat.R;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    EditText email_edittext,password_edittext,firstname_edittext,lastname_edittext;
    TextView already_signuptextview;
    Button singup_button;
    String emailstring,passwordstaring,firstnamestring,lastnamestring;
    FirebaseAuth mAuth;
    DatabaseReference userDatabase;
    SharedPreferences cookies_string;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE);
        getSupportActionBar().hide();
        cookies_string = getSharedPreferences(Constants.KEY_COOKIES, Context.MODE_PRIVATE);
        editor = cookies_string.edit();
        email_edittext=(EditText)findViewById(R.id.signup_emaileditText);
        password_edittext=(EditText)findViewById(R.id.signup_passworkeditText);
        firstname_edittext=(EditText)findViewById(R.id.first_editText);
        lastname_edittext=(EditText)findViewById(R.id.last_editText);
        already_signuptextview=(TextView)findViewById(R.id.alreadysignup_textView);
        singup_button=(Button)findViewById(R.id.signup_button);
        mAuth=FirebaseAuth.getInstance();
        singup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        already_signuptextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    ProgressDialog progressDialog;
    private void signup()
    {
        if (!isvalidate()) return;
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating user data from server..");
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailstring, passwordstaring)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = task.getResult().getUser();
                            String uid = user.getUid();
                            UserModel userModel = new UserModel();
                            userModel.setObjectId(uid);
                            userModel.setEmail(emailstring);
                            userModel.setFirstName(firstnamestring);
                            userModel.setLastName(lastnamestring);
                            String date = Calendar.getInstance().getTime().getTime() + "";
                            userModel.setCreateAt(date);
                            userModel.setUpdateAt(date);
                            userModel.setNet_status("1");
                            userDatabase.child(uid).setValue(userModel.getHashMap());
                            editor.putString(Constants.KEY_FIRSTNAME,firstnamestring);
                            editor.putString(Constants.KEY_LASTNAME,lastnamestring);
                            editor.putString(Constants.KEY_USERMAIL,emailstring);
                            editor.commit();
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    boolean isvalidate()
    {
        emailstring = email_edittext.getText().toString().trim();
        passwordstaring = password_edittext.getText().toString().trim();
        firstnamestring=firstname_edittext.getText().toString().trim();
        lastnamestring=lastname_edittext.getText().toString().trim();
        if (firstnamestring.equals("")){
            firstname_edittext.setError("Please enter first name");
            return false;
        }
        if (lastnamestring.equals("")){
            lastname_edittext.setError("Please enter last name");
            return false;
        }
        if (emailstring.equals("")){
            email_edittext.setError("Please email address");
            return false;
        }
        if (passwordstaring.equals("")){
            password_edittext.setError("Please enter password");
            return false;}
        if (passwordstaring.length()<6){
            password_edittext.setError("Password length should be 6 more");
            return false;}
        return true;
    }
}

package com.gabri.gpschat.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.gabri.gpschat.MainActivity;
import com.gabri.gpschat.R;
import com.gabri.gpschat.activity.LoginActivity;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.GPSTracker;
import com.gabri.gpschat.utility.LocatioinService;
import com.gabri.gpschat.utility.LocationTracker;
import com.gabri.gpschat.utility.TrackerSettings;
import com.gabri.gpschat.utility.Utils;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarviewbindings.bindings.AvatarViewBindings;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public FirebaseAuth auth;
    public FirebaseUser currnetUser;
    DatabaseReference userDatabase;
    View setting_view;
    Button logout_button,update_button;
    EditText firstname_edittext,lastname_edit;
    private AvatarView profileImageview;
    private IImageLoader imageLoader;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    SharedPreferences cookies_string;
    SharedPreferences.Editor editor;
    LocationTracker tracker;
    String path,avata_path;
    Bitmap userBitmap;
    ACProgressFlower dialog;
    MainActivity mainActivity;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setting_view=inflater.inflate(R.layout.fragment_setting, container, false);
        cookies_string = getActivity().getSharedPreferences(Constants.KEY_COOKIES, Context.MODE_PRIVATE);
        editor = cookies_string.edit();
        mainActivity = MainActivity.getInstance();
        avata_path=cookies_string.getString(Constants.KEY_PHOTOURL,null);
        userDatabase = FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE);
        logout_button=(Button)setting_view.findViewById(R.id.signout_button);
        update_button=(Button)setting_view.findViewById(R.id.update_button);
        firstname_edittext=(EditText)setting_view.findViewById(R.id.firstname_editText);
        lastname_edit=(EditText)setting_view.findViewById(R.id.lastname_editText);
        profileImageview=(AvatarView)setting_view.findViewById(R.id.avatar_image);
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Updating")
                .fadeColor(Color.DKGRAY).build();
        loadprofile();
        profileImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        imageLoader = new PicassoLoader();
        if (avata_path.equals("")){
            imageLoader.loadImage(profileImageview, (String)null, cookies_string.getString(Constants.KEY_FIRSTNAME,null));
        }
        else {
            imageLoader.loadImage(profileImageview, avata_path, cookies_string.getString(Constants.KEY_FIRSTNAME,null));
        }

        auth=FirebaseAuth.getInstance();
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            updating_profile();
            }
        });
        return setting_view;
    }
    public void signout(){

        auth.signOut();

//            mAuth.signOut();
            if (AccessToken.getCurrentAccessToken() != null)
            {
                LoginManager.getInstance().logOut();
            }
        if (Utils.getFromPref(Constants.FACEBOOK, getActivity()).equals("true"))   Utils.setToPrefString(Constants.FACEBOOK, "false", getActivity());
        UserModel userModel = new UserModel();
        userModel.setObjectId(cookies_string.getString(Constants.USER_ID,null));
        userModel.setEmail(cookies_string.getString(Constants.KEY_USERMAIL,null));
        userModel.setFirstName(firstname_edittext.getText().toString().trim());
        userModel.setLastName(lastname_edit.getText().toString().trim());
        userModel.setPhotoURL(cookies_string.getString(Constants.KEY_PHOTOURL,null));
        String date = Calendar.getInstance().getTime().getTime() + "";
        userModel.setNet_status("0");
        userModel.setCreateAt(date);
        userModel.setUpdateAt(date);
        userDatabase.child(cookies_string.getString(Constants.USER_ID,null)).setValue(userModel.getHashMap());


        Intent intent=new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);

    }
    public void updating_profile(){

        dialog.show();
        currnetUser= auth.getCurrentUser();
        final String uid = currnetUser.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(Constants.USER_TABLE + "_profiles").child(uid + ".png");
        if (userBitmap != null)
        {
//            Bitmap bitmap = imageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            userBitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    UserModel userModel = new UserModel();
                    userModel.setObjectId(uid);
                    userModel.setEmail(cookies_string.getString(Constants.KEY_USERMAIL,null));
                    userModel.setFirstName(firstname_edittext.getText().toString().trim());
                    userModel.setLastName(lastname_edit.getText().toString().trim());
                    userModel.setPhotoURL(downloadUrl.toString());
                    String date = Calendar.getInstance().getTime().getTime() + "";
                    userModel.setCreateAt(date);
                    userModel.setUpdateAt(date);
                    userDatabase.child(uid).setValue(userModel.getHashMap());
                    editor.putString(Constants.KEY_FIRSTNAME,firstname_edittext.getText().toString().trim());
                    editor.putString(Constants.KEY_LASTNAME,lastname_edit.getText().toString().trim());
                    editor.putString(Constants.KEY_PHOTOURL,downloadUrl.toString());
                    editor.commit();
                    dialog.dismiss();
                }
            });

        }
        else
        {
            UserModel userModel = new UserModel();
            userModel.setObjectId(uid);
            userModel.setEmail(cookies_string.getString(Constants.KEY_USERMAIL,null));
            userModel.setFirstName(firstname_edittext.getText().toString().trim());
            userModel.setLastName(lastname_edit.getText().toString().trim());
            userModel.setPhotoURL(cookies_string.getString(Constants.KEY_PHOTOURL,null));
            String date = Calendar.getInstance().getTime().getTime() + "";
            userModel.setCreateAt(date);
            userModel.setUpdateAt(date);
            userDatabase.child(uid).setValue(userModel.getHashMap());
            editor.putString(Constants.KEY_FIRSTNAME,firstname_edittext.getText().toString().trim());
            editor.putString(Constants.KEY_LASTNAME,lastname_edit.getText().toString().trim());
            editor.commit();
            dialog.dismiss();
        }




    }
    public void loadprofile(){
    firstname_edittext.setText(cookies_string.getString(Constants.KEY_FIRSTNAME,null));
    lastname_edit.setText(cookies_string.getString(Constants.KEY_LASTNAME,null));

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            String uploadFolder = Constants.KEY_SAVEPHOTO_URL;
            Bundle extras = data.getExtras();
            Bitmap thumbnail = (Bitmap) extras.get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            String PATH = Environment.getExternalStorageDirectory() + "/" + uploadFolder + "/";
            File folder = new File(PATH);
            if (!folder.exists()) {
                folder.mkdir();//If there is no folder it will be created.
            }
            File destination = new File(PATH,
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            path = destination.getAbsolutePath();
            userBitmap = thumbnail;
            profileImageview.setImageBitmap(thumbnail);
        }
    }

public void start_locationupload(){




}




}


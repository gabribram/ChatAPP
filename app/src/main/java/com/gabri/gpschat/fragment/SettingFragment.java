package com.gabri.gpschat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.gabri.gpschat.R;
import com.gabri.gpschat.activity.LoginActivity;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import agency.tango.android.avatarviewbindings.bindings.AvatarViewBindings;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    public FirebaseAuth auth;
    public FirebaseUser currnetUser;
    View setting_view;
    Button logout_button;
    EditText firstname_edittext,lastname_edit;
    private AvatarView profileImageview;
    private static final String STONOGA_IMAGE = "http://brzeg24.pl/wp-content/uploads/2015/06/Zbigniew-Stonoga-e1434539892479.jpg";
    private IImageLoader imageLoader;
    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setting_view=inflater.inflate(R.layout.fragment_setting, container, false);
        logout_button=(Button)setting_view.findViewById(R.id.signout_button);
        firstname_edittext=(EditText)setting_view.findViewById(R.id.firstname_editText);
        lastname_edit=(EditText)setting_view.findViewById(R.id.lastname_editText);
        profileImageview=(AvatarView)setting_view.findViewById(R.id.avatar_image);
//        loadprofile();
        imageLoader = new PicassoLoader();
        imageLoader.loadImage(profileImageview, STONOGA_IMAGE, "Stonoga");
        auth=FirebaseAuth.getInstance();
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });
        return setting_view;
    }
    public void signout(){

        auth.signOut();
        Constants.currentUserModel=null;
        Intent intent=new Intent(getActivity(),LoginActivity.class);
        startActivity(intent);

    }

public void loadprofile(){
    firstname_edittext.setText(Constants.currentUserModel.getFirstName());

}






}


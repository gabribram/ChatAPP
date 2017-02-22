package com.gabri.gpschat.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gabri.gpschat.R;
import com.gabri.gpschat.activity.ChatMainActivity;
import com.gabri.gpschat.adapts.AvailableUserAdapter;
import com.gabri.gpschat.adapts.ContacteUserAdapter;
import com.gabri.gpschat.model.RecentModel;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContacteFragment extends Fragment {

    View contact_view;
    ListView availble_userlistview;
    ContacteUserAdapter adapter;
    private List<UserModel> userlist;
    ACProgressFlower dialog;
    String currentUserId;
    public ContacteFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contact_view=inflater.inflate(R.layout.fragment_contacte, container, false);
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Reading")
                .fadeColor(Color.DKGRAY).build();
        userlist = new ArrayList<UserModel>();
        currentUserId = Utils.getFromPref(Constants.USER_ID, getContext());
        availble_userlistview = (ListView)contact_view.findViewById(R.id.contact_listview);
        load_user();

        return contact_view;
    }


    public void load_user(){




        dialog.show();
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dialog.dismiss();
//                userlist = new ArrayList<UserModel>();
                for (DataSnapshot post : dataSnapshot.getChildren())
                {
                    UserModel model = post.getValue(UserModel.class);
                    Log.d("value", post.toString());
                    userlist.add(model);
                }
                adapter = new ContacteUserAdapter(getActivity(), userlist);
                availble_userlistview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "User null", Toast.LENGTH_SHORT).show();
            }
        });

        availble_userlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserModel model = adapter.getItem(position);
//                Toast.makeText(getActivity(), model.getObjectId(), Toast.LENGTH_LONG).show();
                String uid = model.getObjectId();
                CheckExists(uid);
            }
        });




    }

    private void CheckExists(final String uid) {
//        String currentUserId = Utils.getFromPref(Constants.USER_ID, getContext());
        Log.d("current", currentUserId);
        if (currentUserId.equals("")) return;
        Query query = FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).orderByChild("userId").equalTo(currentUserId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean ok = false;
                RecentModel tempModel = new RecentModel();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    RecentModel model = post.getValue(RecentModel.class);

                    if (model != null && model.getOtherUser().equals(uid))
                    {
                        ok = true;
                        tempModel = model;
                        break;
                    }
                }
                if (ok == false)
                {
                    Log.d("ok", "null");
                    createChatRoom(uid);
                }
                else
                {
                    Toast.makeText(getActivity(), "Already exists", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ChatMainActivity.class);
                    intent.putExtra("model", tempModel);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("database", databaseError.toString());
            }
        });

    }

    private void createChatRoom(String uid) {

        String groupId = FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_TABLE).push().getKey();
        String recentKey1 = FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).push().getKey();
        String recentKey2 = FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).push().getKey();
        String date = Calendar.getInstance().getTime().getTime() + "";
        RecentModel myModel = new RecentModel();
        myModel.setCreateAt(date);
        myModel.setUpdatedAt(date);
        myModel.setGroupId(groupId);
        myModel.setLastDate("");
        myModel.setLastMessage("");
        myModel.setObjectId(recentKey1);
        myModel.setOtherRecentId(recentKey2);
        myModel.setOtherUser(uid);
        myModel.setUserId(currentUserId);

        RecentModel otherModel = new RecentModel();
        otherModel.setCreateAt(date);
        otherModel.setUpdatedAt(date);
        otherModel.setGroupId(groupId);
        otherModel.setLastDate("");
        otherModel.setLastMessage("");
        otherModel.setObjectId(recentKey2);
        otherModel.setOtherRecentId(recentKey1);
        otherModel.setOtherUser(currentUserId);
        if (uid.equals(currentUserId))    otherModel.setUserId(uid + "_");
        else otherModel.setUserId(uid);

        FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).child(recentKey1).setValue(myModel.getHashMap());
        FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).child(recentKey2).setValue(otherModel.getHashMap());

        Intent intent = new Intent(getActivity(), ChatMainActivity.class);
        intent.putExtra("model", myModel);
        startActivity(intent);
    }

}

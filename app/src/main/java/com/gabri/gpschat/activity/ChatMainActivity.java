package com.gabri.gpschat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gabri.gpschat.LocationShowActivity;
import com.gabri.gpschat.R;
import com.gabri.gpschat.adapts.MessageAdapter;
import com.gabri.gpschat.model.MessageModel;
import com.gabri.gpschat.model.RecentModel;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.Utils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatMainActivity extends AppCompatActivity {

    ListView listView;
    List<MessageModel> messageModelList;
    RecentModel model;
    RecentModel othermodel;
    MessageAdapter adapter;
    EmojiconEditText editText;
    TextView other_username_textview;
    ImageButton back_imagebutton,location_imagebutton;
    String otherUserName, otherUserPhoto;
    String currentUserId, currentUserProfile, currentUserName;
    long currentDate;
    LinearLayout chatLayout;
    ImageButton emojiBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        getSupportActionBar().hide();
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Bundle bundle = getIntent().getExtras();
        model = (RecentModel) bundle.get("model");
        othermodel=(RecentModel)bundle.get("othermodel");
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.listView);
        editText = (EmojiconEditText) findViewById(R.id.editChat);
        other_username_textview=(TextView)findViewById(R.id.username_textView);
        back_imagebutton=(ImageButton)findViewById(R.id.exit_chat_imageButton);
        location_imagebutton=(ImageButton)findViewById(R.id.locatioin_imageButton);
        back_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            model.setAvaiable_status("0");
            FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).child(model.getObjectId()).setValue(model.getHashMap());
            onBackPressed();
            }
        });
        location_imagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatMainActivity.this, LocationShowActivity.class);
                intent.putExtra("other_user",model.getOtherUser());
                startActivity(intent);
            }
        });
        GetData();
    }

    public void OnSend(View view)
    {


        FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).child(model.getOtherRecentId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data_testing",dataSnapshot.getValue().toString());
                RecentModel other = dataSnapshot.getValue(RecentModel.class);
                String available = other.getAvaiable_status();
                String unread_count_string=other.getUnread_count_message();
                int unread_count=Integer.parseInt(unread_count_string);
                if (available.equals("0")){
                    unread_count+=1;
                    other.setUnread_count_message(Integer.toString(unread_count));
                    FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).child(other.getObjectId()).setValue(other.getHashMap());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        String chatString = editText.getText().toString().trim();
        if (chatString.equals(""))  return;
        String messageKey = FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_TABLE).child(model.getGroupId()).push().getKey();
        MessageModel mod = new MessageModel();
        String date = Calendar.getInstance().getTime().getTime() + "";
        mod.setSenderId(currentUserId);
        mod.setSenderPhotoURL(currentUserProfile);
        mod.setSenderUsername(currentUserName);
        mod.setCreatedAt(date);
        mod.setUpdatedAt(date);
        mod.setText(chatString);
        mod.setObjectId(messageKey);
        mod.setGroupId(model.getGroupId());
        editText.setText("");
        FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_TABLE).child(model.getGroupId()).child(messageKey).setValue(mod.getHashMap());
    }
    private void GetData()
    {



        model.setAvaiable_status("1");
        model.setUnread_count_message("0");
        FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).child(model.getObjectId()).setValue(model.getHashMap());

        currentUserId = Utils.getFromPref(Constants.USER_ID, this);
        currentUserName = Utils.getFromPref(Constants.KEY_FIRSTNAME, this);
        currentUserProfile = Utils.getFromPref(Constants.KEY_PHOTOURL, this);
        currentDate = Calendar.getInstance().getTime().getTime();
        messageModelList = new ArrayList<MessageModel>();
        chatLayout = (LinearLayout) findViewById(R.id.rootView);
        emojiBtn = (ImageButton) findViewById(R.id.btnEmoji);
        EmojIconActions  emojIcon=new EmojIconActions(this,chatLayout,editText,emojiBtn);
        emojiBtn.setImageResource(R.drawable.ic_emoticon);
        emojIcon.ShowEmojIcon();
        FirebaseDatabase.getInstance().getReference(Constants.USER_TABLE).child(model.getOtherUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel other = dataSnapshot.getValue(UserModel.class);
                otherUserName = other.getFirstName() + " " + other.getLastName();
                other_username_textview.setText(otherUserName);
                otherUserPhoto = other.getPhotoURL();
                FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_TABLE).child(model.getGroupId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot post : dataSnapshot.getChildren())
                        {
                            MessageModel messageModel = post.getValue(MessageModel.class);
                            messageModelList.add(messageModel);
                        }
                        adapter = new MessageAdapter(ChatMainActivity.this, messageModelList);
                        listView.setAdapter(adapter);
                        listView.setSelection(messageModelList.size() - 1);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_TABLE).child(model.getGroupId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel model = dataSnapshot.getValue(MessageModel.class);
                long dd = Long.valueOf(model.getCreatedAt());
                if (dd >= currentDate) {
                    messageModelList.add(model);
                    adapter.notifyDataSetChanged();
                }
                adapter = new MessageAdapter(ChatMainActivity.this, messageModelList);
                listView.setAdapter(adapter);
                listView.setSelection(messageModelList.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}

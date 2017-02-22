package com.gabri.gpschat.adapts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabri.gpschat.R;
import com.gabri.gpschat.activity.ChatMainActivity;
import com.gabri.gpschat.model.RecentModel;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by gabri on 20/02/2017.
 */

public class AvailableUserAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<UserModel> userslist = new ArrayList<>();
    String currentUserId;
    public AvailableUserAdapter(Activity activity, List<UserModel> userslist) {
        this.activity = activity;
        this.userslist.addAll(userslist);
    }

    @Override
    public int getCount() {
        return userslist.size();
    }

    @Override
    public UserModel getItem(int location) {
        return userslist.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.available_user_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.firstname_textview = (TextView) view.findViewById(R.id.fisrtname_textView);
            viewHolder.lastname_textview = (TextView) view.findViewById(R.id.lastname_textView);
            viewHolder.add_imagebutton=(ImageView)view.findViewById(R.id.adduser_imageButton);
            viewHolder.user_profile_avatarview=(AvatarView)view.findViewById(R.id.probile_avatar_image);
            viewHolder.netstatus_imageview=(ImageView)view.findViewById(R.id.net_status_imageView);
            viewHolder.notification_textview=(TextView)view.findViewById(R.id.notification_textView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.firstname_textview.setText(userslist.get(position).getFirstName());
        viewHolder.lastname_textview.setText(userslist.get(position).getLastName());



        Query query = FirebaseDatabase.getInstance().getReference(Constants.RECENT_TABLE).orderByChild("otherUser").equalTo(userslist.get(position).getObjectId());
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








        if (userslist.get(position).getNet_status().equals("1")){
            viewHolder.netstatus_imageview.setImageResource(R.drawable.online);
        }
        else if(userslist.get(position).getNet_status().equals("0")){
            viewHolder.netstatus_imageview.setImageResource(R.drawable.offline);
        }
        IImageLoader imageLoader=new PicassoLoader();
        if (userslist.get(position).getPhotoURL().equals("")){
            imageLoader.loadImage(viewHolder.user_profile_avatarview, (String)null,userslist.get(position).getFirstName());
        }
        else {
            imageLoader.loadImage(viewHolder.user_profile_avatarview, userslist.get(position).getPhotoURL(),userslist.get(position).getFirstName());
        }

        return view;
    }

    private static final class ViewHolder {
        private static AvatarView user_profile_avatarview;
        private static TextView firstname_textview;
        private static TextView lastname_textview;
        private static ImageView add_imagebutton;
        private static ImageView netstatus_imageview;
        private static TextView notification_textview;
    }

}

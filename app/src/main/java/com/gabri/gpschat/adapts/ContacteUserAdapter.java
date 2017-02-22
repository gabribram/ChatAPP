package com.gabri.gpschat.adapts;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabri.gpschat.R;
import com.gabri.gpschat.model.UserModel;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

/**
 * Created by gabri on 20/02/2017.
 */

public class ContacteUserAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<UserModel> userslist = new ArrayList<>();

    public ContacteUserAdapter(Activity activity, List<UserModel> userslist) {
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
            view = vi.inflate(R.layout.contacte_user_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.firstname_textview = (TextView) view.findViewById(R.id.fisrtname_textView);
            viewHolder.lastname_textview = (TextView) view.findViewById(R.id.lastname_textView);
            viewHolder.add_imagebutton=(ImageView)view.findViewById(R.id.adduser_imageButton);
            viewHolder.user_profile_avatarview=(AvatarView)view.findViewById(R.id.probile_avatar_image);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.firstname_textview.setText(userslist.get(position).getFirstName());
        viewHolder.lastname_textview.setText(userslist.get(position).getLastName());
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
    }

}

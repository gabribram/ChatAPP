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
import com.gabri.gpschat.model.MessageModel;
import com.gabri.gpschat.model.UserModel;
import com.gabri.gpschat.utility.Constants;
import com.gabri.gpschat.utility.Utils;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by gabri on 20/02/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private Context activity;
    private LayoutInflater inflater;
    private List<MessageModel> userslist = new ArrayList<>();
    private String currentId;

    public MessageAdapter(Context activity, List<MessageModel> userslist) {
        this.activity = activity;
        this.userslist.addAll(userslist);
        currentId = Utils.getFromPref(Constants.USER_ID, activity);
    }

    @Override
    public int getCount() {
        return userslist.size();
    }

    @Override
    public MessageModel getItem(int location) {
        return userslist.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = getItem(position);
        if (messageModel.getSenderId().equals(currentId))
        {
            return 1;
        }
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder viewHolder;
        MessageModel model = getItem(position);
        int type = getItemViewType(position);
        EmojiconTextView textView1; TextView textView2;
        AvatarView avatarView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (type == 0)
                view = vi.inflate(R.layout.left_chat_item, null);
            else
                view = vi.inflate(R.layout.right_chat_item, null);
        }
        textView1 = (EmojiconTextView) view.findViewById(R.id.fisrtname_textView);
        textView2 = (TextView) view.findViewById(R.id.lastname_textView);
        avatarView = (AvatarView) view.findViewById(R.id.probile_avatar_image);
        IImageLoader imageLoader = new PicassoLoader();
        if (model.getSenderPhotoURL().equals("")){
            imageLoader.loadImage(avatarView, (String)null ,model.getSenderUsername());
        }
        else {
            imageLoader.loadImage(avatarView, model.getSenderPhotoURL(), model.getSenderUsername());
        }
        textView1.setText(model.getText());
        textView2.setText(Utils.getStringFromDate(model.getCreatedAt()));
        return view;
    }

    private static final class ViewHolder {
        private static AvatarView user_profile_avatarview;
        private static TextView firstname_textview;
        private static TextView lastname_textview;
    }

}

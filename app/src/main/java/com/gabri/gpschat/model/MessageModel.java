package com.gabri.gpschat.model;

import java.util.HashMap;

/**
 * Created by gabri on 18/02/2017.
 */

public class MessageModel {

    private String objectId;
    private String groupId;
    private String senderId;
    private String senderUsername;
    private String createdAt;
    private String updatedAt;
    private String text;
    private String senderPhotoURL;

    public MessageModel(String objectId, String groupId, String senderId, String senderUsername, String createdAt, String updatedAt, String text, String senderPhotoURL) {
        this.objectId = objectId;
        this.groupId = groupId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.text = text;
        this.senderPhotoURL = senderPhotoURL;
    }

    public MessageModel() {
        this.objectId = "";
        this.groupId = "";
        this.senderId = "";
        this.senderUsername = "";
        this.createdAt = "";
        this.text = "";
        this.updatedAt = "";
        this.senderPhotoURL = "";
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderPhotoURL() {
        return senderPhotoURL;
    }

    public void setSenderPhotoURL(String senderPhotoURL) {
        this.senderPhotoURL = senderPhotoURL;
    }

    public HashMap<String, String> getHashMap()
    {
//        this.objectId = "";
//        this.groupId = "";
//        this.senderId = "";
//        this.senderUsername = "";
//        this.createdAt = "";
//        this.text = "";
//        this.updatedAt = "";
//        this.senderPhotoURL = "";
        HashMap<String, String> map = new HashMap<>();
        map.put("objectId", this.objectId);
        map.put("groupId", this.groupId);
        map.put("senderId", this.senderId);
        map.put("senderUsername", this.senderUsername);
        map.put("createdAt", this.createdAt);
        map.put("updatedAt", this.updatedAt);
        map.put("text", this.text);
        map.put("senderPhotoURL", this.senderPhotoURL);
        return map;
    }
}

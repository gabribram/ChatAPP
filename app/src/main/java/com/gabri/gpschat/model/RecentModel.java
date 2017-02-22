package com.gabri.gpschat.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by gabri on 18/02/2017.
 */

public class RecentModel implements Serializable{
    private String objectId;
    private String groupId;
    private String otherUser;
    private String otherRecentId;
    private String createAt;
    private String lastMessage;
    private String lastDate;
    private String updatedAt;
    private String otherUserName;
    private String userId;
    private String avaiable_status;
    private String unread_count_message;

    public RecentModel(String objectId, String groupId, String otherUser, String otherRecentId, String createAt, String lastMessage, String lastDate, String updatedAt, String otherUserName, String userId, String avaiable_status, String unread_count_message) {
        this.objectId = objectId;
        this.groupId = groupId;
        this.otherUser = otherUser;
        this.otherRecentId = otherRecentId;
        this.createAt = createAt;
        this.lastMessage = lastMessage;
        this.lastDate = lastDate;
        this.updatedAt = updatedAt;
        this.otherUserName = otherUserName;
        this.userId = userId;
        this.avaiable_status = avaiable_status;
        this.unread_count_message = unread_count_message;
    }



    public RecentModel() {
        this.objectId = "";
        this.createAt = "";
        this.otherUser = "";
        this.lastMessage = "";
        this.lastDate = "";
        this.updatedAt = "";
        this.otherUserName = "";
        this.userId = "";
        this.groupId = "";
        this.otherRecentId = "";
        this.avaiable_status="";
        this.unread_count_message="";

    }
    public String getAvaiable_status() {
        return avaiable_status;
    }

    public void setAvaiable_status(String avaiable_status) {
        this.avaiable_status = avaiable_status;
    }
    public String getOtherRecentId() {
        return otherRecentId;
    }

    public void setOtherRecentId(String otherRecentId) {
        this.otherRecentId = otherRecentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(String otherUser) {
        this.otherUser = otherUser;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getUnread_count_message() {
        return unread_count_message;
    }

    public void setUnread_count_message(String unread_count_message) {
        this.unread_count_message = unread_count_message;
    }

    public HashMap<String, String> getHashMap()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("otherUser", this.otherUser);
        map.put("objectId", this.objectId);
        map.put("createAt", this.createAt);

        map.put("otherUserName", this.otherUserName);
        map.put("groupId", this.groupId);
        map.put("lastMessage", this.lastMessage);
        map.put("lastDate", this.lastDate);
        map.put("userId", this.userId);
        map.put("lastDate", this.lastDate);
        map.put("otherRecentId", this.otherRecentId);
        map.put("avaiable_status",this.avaiable_status);
        map.put("unread_count_message",this.unread_count_message);
        return map;
    }
}

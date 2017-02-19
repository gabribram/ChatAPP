package com.gabri.gpschat.model;

import java.util.HashMap;

/**
 * Created by gabri on 18/02/2017.
 */

public class RecentModel {
    private String objectId;
    private String groupId;
    private String otherUser;
    private String createAt;
    private String lastMessage;
    private String lastDate;
    private String updatedAt;
    private String otherUserName;
    private String userId;


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
    }

    public RecentModel(String objectId, String groupId, String otherUser, String createAt, String lastMessage, String lastDate, String updatedAt, String otherUserName, String userId) {
        this.objectId = objectId;
        this.groupId = groupId;
        this.otherUser = otherUser;
        this.createAt = createAt;
        this.lastMessage = lastMessage;
        this.lastDate = lastDate;
        this.updatedAt = updatedAt;
        this.otherUserName = otherUserName;
        this.userId = userId;
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

    public HashMap<String, String> getHashMap()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("address", this.otherUser);
        map.put("objectId", this.objectId);
        map.put("createAt", this.createAt);
        map.put("otherUserName", this.otherUserName);
        map.put("groupId", this.groupId);
        map.put("lastMessage", this.lastMessage);
        map.put("lastDate", this.lastDate);
        map.put("userId", this.userId);
        map.put("lastDate", this.lastDate);
        return map;
    }
}

package com.gabri.gpschat.model;

import java.util.HashMap;

/**
 * Created by gabri on 18/02/2017.
 */

public class UserModel {

    private String objectId;
    private String createAt;
    private String firstName;
    private String lastName;
    private String updateAt;
    private String token;
    private String photoURL;
    private String birthday;
    private String longitude;
    private String latitude;
    private String address;
    private String email;
    private String net_status;

    public UserModel() {
        this.objectId = "";
        this.createAt = "";
        this.firstName = "";
        this.lastName = "";
        this.updateAt = "";
        this.token = "";
        this.photoURL = "";
        this.birthday = "";
        this.longitude = "";
        this.latitude = "";
        this.address = "";
        this.email = "";
        this.net_status="";
    }

    public UserModel(String objectId, String createAt, String firstName, String lastName, String updateAt, String token, String photoURL, String birthday, String longitude, String latitude, String address, String email, String net_status) {
        this.objectId = objectId;
        this.createAt = createAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.updateAt = updateAt;
        this.token = token;
        this.photoURL = photoURL;
        this.birthday = birthday;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.email = email;
        this.net_status = net_status;
    }

    public String getNet_status() {
        return net_status;
    }

    public void setNet_status(String net_status) {
        this.net_status = net_status;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HashMap<String, String> getHashMap()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("address", this.address);
        map.put("objectId", this.objectId);
        map.put("createAt", this.createAt);
        map.put("updateAt", this.updateAt);
        map.put("email", this.email);
        map.put("firstName", this.firstName);
        map.put("lastName", this.lastName);
        map.put("birthday", this.birthday);
        map.put("longitude", this.longitude);
        map.put("latitude", this.latitude);
        map.put("photoURL", this.photoURL);
        map.put("token", this.token);
        map.put("net_status",this.net_status);
        return map;
    }

}

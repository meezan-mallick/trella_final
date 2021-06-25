package com.android.blogapplication;

public class UserModel {

    private String user_id;
    private String userName;
    private String email;
    private String profile_image;

    public UserModel() {

    }

    public UserModel(String user_id, String userName, String email, String profile_image) {
        this.user_id = user_id;
        this.userName = userName;
        this.email = email;
        this.profile_image = profile_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}

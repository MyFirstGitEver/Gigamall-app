package com.example.gigamall_app.entities;

public class UserEntity {
    private Integer id;
    private String userName, password, userDisplayName, url;
    private int starCount;

    public UserEntity(String userName, String password, String userDisplayName, int starCount) {
        this.userName = userName;
        this.password = password;
        this.userDisplayName = userDisplayName;
        this.starCount = starCount;
    }

    public UserEntity() {

    }

    public String getUrl() {
        return url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }
}
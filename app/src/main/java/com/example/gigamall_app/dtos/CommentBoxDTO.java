package com.example.gigamall_app.dtos;

import com.example.gigamall_app.entities.UserEntity;

import java.util.Date;

public class CommentBoxDTO  {
    private Integer id;

    private int productId, contentInStar, level, childCount;
    private String contentInText, attatchedUrl;
    private Date commentDate;

    private UserEntity user;

    public Integer getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }
    public int getContentInStar() {
        return contentInStar;
    }
    public String getContentInText() {
        return contentInText;
    }
    public String getAttatchedUrl() {
        return attatchedUrl;
    }
    public Date getCommentDate() {
        return commentDate;
    }

    public int getLevel() {
        return level;
    }

    public int getChildCount() {
        return childCount;
    }


    public UserEntity getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }
}
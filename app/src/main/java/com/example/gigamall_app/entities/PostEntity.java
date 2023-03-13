package com.example.gigamall_app.entities;

import java.util.List;

public class PostEntity {
    private Integer id;
    private String title;
    private int likeCnt;

    private BrandEntity brand;
    private List<MediaEntity> images;

    private boolean isLiked = false;

    public PostEntity() {
    }

    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public int getLikeCnt() {
        return likeCnt;
    }

    public List<MediaEntity> getImages() {
        return images;
    }

    public BrandEntity getBrand() {
        return brand;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
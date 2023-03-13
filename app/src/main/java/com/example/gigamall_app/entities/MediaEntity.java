package com.example.gigamall_app.entities;

public class MediaEntity {
    private Integer id;

    private String title;
    private String url;

    private Integer width, height;

    private boolean isLiked;

    public MediaEntity() {
    }

    public Integer getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
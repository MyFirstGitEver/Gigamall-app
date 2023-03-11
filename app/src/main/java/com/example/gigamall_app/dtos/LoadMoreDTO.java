package com.example.gigamall_app.dtos;

public class LoadMoreDTO {
    private int id;
    private int level;
    private int page;
    private int childCount;

    public LoadMoreDTO(int id, int level, int page, int childCount) {
        this.id = id;
        this.level = level;
        this.page = page;
        this.childCount = childCount;
    }

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public int getPage() {
        return page;
    }

    public int getChildCount(){
        return childCount;
    }
}

package com.example.gigamall_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gigamall_app.entities.PostEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EventFragmentViewModel extends ViewModel {
    private List<PostEntity> posts = null;
    private final MutableLiveData<List<PostEntity>> top6PostsHolder = new MutableLiveData<>(null);

    public List<PostEntity> getPosts(){
        return posts;
    }
    public void setPosts(List<PostEntity> posts){
        this.posts = posts;
    }

    public MutableLiveData<List<PostEntity>> getTop6PostsHolder(){
        return top6PostsHolder;
    }
    public void setTop6Posts(List<PostEntity> posts){
        top6PostsHolder.setValue(posts);
    }

    public String listIds(){
        if(posts == null){
            return "[]";
        }

        List<Integer> ids = new ArrayList<>();

        for(PostEntity post : posts){
            if(post == null){
                continue;
            }

            ids.add(post.getId());
        }

        return new Gson().toJson(ids);
    }
}
package com.example.gigamall_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gigamall_app.entities.PostEntity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EventFragmentViewModel extends ViewModel {
    private MutableLiveData<List<PostEntity>> postsHolder = new MutableLiveData<>(null);
    private MutableLiveData<List<PostEntity>> top6PostsHolder = new MutableLiveData<>(null);

    public MutableLiveData<List<PostEntity>> getPostsHolder(){
        return postsHolder;
    }
    public void setPosts(List<PostEntity> posts){
        postsHolder.setValue(posts);
    }

    public MutableLiveData<List<PostEntity>> getTop6PostsHolder(){
        return top6PostsHolder;
    }
    public void setTop6Posts(List<PostEntity> posts){
        top6PostsHolder.setValue(posts);
    }

    public String listIds(){
        if(postsHolder.getValue() == null){
            return "[]";
        }

        List<Integer> ids = new ArrayList<>();

        for(PostEntity post : postsHolder.getValue()){
            if(post == null){
                continue;
            }

            ids.add(post.getId());
        }

        return new Gson().toJson(ids);
    }
}
package com.example.gigamall_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gigamall_app.dtos.CommentBoxDTO;

import java.util.List;

public class ProductFragmentViewModel extends ViewModel {
    private MutableLiveData<List<Object>> commentsHolder = new MutableLiveData<>(null);

    public void setComments(List<Object> comments){
        commentsHolder.setValue(comments);
    }

    public MutableLiveData<List<Object>> observeProducts(){
        return commentsHolder;
    }
}
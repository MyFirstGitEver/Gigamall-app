package com.example.gigamall_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gigamall_app.entities.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentViewModel extends ViewModel {
    private MutableLiveData<List<ProductEntity>> searchResultsHolder = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<List<ProductEntity>> getSearchResultsHolder() {
        return searchResultsHolder;
    }
}
package com.example.gigamall_app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gigamall_app.entities.BrandEntity;
import com.example.gigamall_app.entities.ProductEntity;

import java.util.List;

public class GigaMallActivityViewModel extends ViewModel {
    private MutableLiveData<List<ProductEntity>> productsHolder = new MutableLiveData<>(null);
    private MutableLiveData<List<BrandEntity>> brandsHolder = new MutableLiveData<>(null);

    public void setProducts(List<ProductEntity> products){
        productsHolder.setValue(products);
    }

    public MutableLiveData<List<ProductEntity>> observeProducts(){
        return productsHolder;
    }

    public void setBrands(List<BrandEntity> brands){
        brandsHolder.setValue(brands);
    }

    public MutableLiveData<List<BrandEntity>> observeBrands(){
        return brandsHolder;
    }

    public boolean fetchedBrands(){
        return brandsHolder.getValue() != null;
    }
}
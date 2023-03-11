package com.example.gigamall_app.interfaces;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class MainPageScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstCompletelyVisible =
                ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstCompletelyVisibleItemPosition();
        int firstVisible =
                ((LinearLayoutManager)(recyclerView.getLayoutManager())).findFirstVisibleItemPosition();

        if(firstCompletelyVisible == 7){
            onLookingAtBrands();
        }

        if(firstVisible == 1){
            onBannerInvisible();
        }
        else if(firstVisible == 0){
            onBannerVisible();
        }
    }

    protected abstract void onBannerVisible();
    public abstract void onLookingAtBrands();
    public abstract void onBannerInvisible();
}
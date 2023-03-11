package com.example.gigamall_app.interfaces;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ProductPageScrollListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int absolutelyFirst = manager.findFirstCompletelyVisibleItemPosition();
        int firstVisible = manager.findFirstVisibleItemPosition();

        if(firstVisible == 0){
            atTopOfPage();
        }

        if(absolutelyFirst == 1 && dy > 0){
            onOverScroll();
        }
    }

    public abstract void atTopOfPage();
    public abstract void onOverScroll();
}
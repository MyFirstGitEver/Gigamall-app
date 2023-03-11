package com.example.gigamall_app.interfaces;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class OnEndReachedListener extends RecyclerView.OnScrollListener {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int firstVisible = manager.findFirstVisibleItemPosition();
        int visibleCount = manager.getChildCount();

        if(isLastPage() || isLoading()){
            return;
        }

        if(manager.getItemCount() <= firstVisible + visibleCount && dy != 0){
            onEndReached();
        }
    }

    public abstract void onEndReached();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}

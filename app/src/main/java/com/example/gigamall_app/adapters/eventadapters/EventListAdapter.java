package com.example.gigamall_app.adapters.eventadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.PostEntity;
import com.example.gigamall_app.interfaces.OnCommentClickListener;
import com.example.gigamall_app.tools.viewholders.NullViewHolder;
import com.example.gigamall_app.tools.viewholders.PostViewHolder;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<PostEntity> posts;
    private List<PostEntity> top6;

    private final OnCommentClickListener onCommentClickListener;
    private final int screenWidth;

    public EventListAdapter(List<PostEntity> posts, OnCommentClickListener onCommentClickListener, int screenWidth){
        this.posts = posts;
        this.onCommentClickListener = onCommentClickListener;
        this.screenWidth = screenWidth;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == 1){
            return new PostViewHolder(
                    inflater.inflate(R.layout.a_single_pic_post, parent, false),
                    1,
                    screenWidth,
                    onCommentClickListener);
        }
        else if(viewType == 2){
            return new PostViewHolder(
                    inflater.inflate(R.layout.two_pics_post, parent, false),
                    2,
                    screenWidth,
                    onCommentClickListener);
        }
        else if(viewType == 3){
            return new PostViewHolder(
                    inflater.inflate(R.layout.three_pics_post, parent, false),
                    3,
                    screenWidth,
                    onCommentClickListener);
        }
        else if(viewType == 4){
            return new PostViewHolder(
                    inflater.inflate(R.layout.four_pics_post, parent, false),
                    4,
                    screenWidth,
                    onCommentClickListener);
        }
        else if(viewType == 0){
            return new TopPostsViewHolder(inflater.inflate(R.layout.top_posts_part, parent, false));
        }
        else{
            return new NullViewHolder(inflater.inflate(R.layout.a_loading, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }
        else if(posts.get(position - 1) != null){
            return posts.get(position - 1).getImages().size();
        }
        else{
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position != 0){
            if(posts.get(position - 1) == null){
                return;
            }

            ((PostViewHolder)holder).bind(posts.get(position - 1));
        }
        else{
            ((TopPostsViewHolder)holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        if(posts == null){
            return 1;
        }

        return posts.size() + 1;
    }

    public void setTop6(List<PostEntity> posts){
        top6 = posts;
        notifyItemChanged(0);
    }

    public void addFooter(){
        posts.add(null);
        notifyItemInserted(posts.size());
    }

    public void removeFooter(){
        posts.remove(posts.size() - 1);
        notifyItemRemoved(posts.size() + 1);
    }

    public void addPosts(List<PostEntity> posts){
        if(this.posts == null){
            this.posts = posts;
            notifyItemRangeInserted(1, posts.size());
        }
        else{
            int oldLength = this.posts.size();
            this.posts.addAll(posts);

            notifyItemRangeInserted(oldLength + 1, posts.size());
        }
    }

    public class TopPostsViewHolder extends RecyclerView.ViewHolder {
        RecyclerView top6List;

        public TopPostsViewHolder(@NonNull View itemView) {
            super(itemView);

            top6List = itemView.findViewById(R.id.top6List);
        }

        public void bind(){
            if(top6 != null){
                top6List.setLayoutManager(new LinearLayoutManager(
                        itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                top6List.setAdapter(new PostListAdapter(top6));
            }
        }
    }
}
package com.example.gigamall_app.adapters.eventadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.PostEntity;
import com.example.gigamall_app.interfaces.OnCommentClickListener;
import com.example.gigamall_app.tools.viewholders.PostViewHolder;

import java.util.List;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.TopPostViewHolder> {
    private final List<PostEntity> posts;

    public PostListAdapter(List<PostEntity> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public TopPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopPostViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.a_top_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopPostViewHolder holder, int position) {
        holder.bind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class TopPostViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg, postImg;

        public TopPostViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.userImg);
            postImg = itemView.findViewById(R.id.postImg);
        }

        public void bind(PostEntity post){
            Glide.with(itemView.getContext()).load(post.getImages().get(0).getUrl()).into(postImg);
            Glide.with(itemView.getContext()).load(post.getBrand().getLogoUrl()).into(userImg);
        }
    }
}
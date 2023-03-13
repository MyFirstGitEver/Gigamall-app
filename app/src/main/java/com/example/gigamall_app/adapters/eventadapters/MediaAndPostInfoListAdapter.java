package com.example.gigamall_app.adapters.eventadapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.MediaEntity;
import com.example.gigamall_app.entities.PostEntity;

public class MediaAndPostInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final PostEntity post;

    public MediaAndPostInfoListAdapter(PostEntity post) {
        this.post = post;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == 0){
            return new PostInfoViewHolder(inflater.inflate(R.layout.post_info_part, parent, false));
        }
        else{
            return new MediaViewHolder(inflater.inflate(R.layout.a_media, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }
        else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            ((PostInfoViewHolder)holder).bind();
        }
        else{
            ((MediaViewHolder)holder).bind(post.getImages().get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return 1 + post.getImages().size();
    }

    public class PostInfoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView userImg;
        private final TextView userNameTxt, titleTxt;
        private final ImageButton likeBtn;
        private AppCompatButton commentBtn;

        public PostInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.userImg);

            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            titleTxt = itemView.findViewById(R.id.titleTxt);

            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentsBtn);
        }

        public void bind(){
            userNameTxt.setText(post.getBrand().getName());
            titleTxt.setText(post.getTitle());
            //commentBtn.setText(itemView.getContext().getString(R.string.comment_btn_text, dtos.size()));

            Glide.with(itemView.getContext()).load(post.getBrand().getLogoUrl()).into(userImg);

            if(post.isLiked()){
                likeBtn.setBackgroundTintList(ColorStateList.valueOf(
                        itemView.getContext().getResources().getColor(android.R.color.holo_red_light)));
            }
            else {
                likeBtn.setBackgroundTintList(ColorStateList.valueOf(
                        itemView.getContext().getResources().getColor(android.R.color.black)));
            }

            likeBtn.setOnClickListener(v ->{
                post.setLiked(!post.isLiked());
                startScaleAnimation((ImageButton) v);

                if(post.isLiked()){
                    ((ImageButton)v).setColorFilter(itemView.getContext()
                            .getResources().getColor(android.R.color.holo_red_light));
                }
                else{
                    ((ImageButton)v).setColorFilter(itemView.getContext()
                            .getResources().getColor(android.R.color.black));
                }
            });
        }

        private void startScaleAnimation(ImageButton imageButton){
            imageButton.startAnimation(AnimationUtils.loadAnimation(imageButton.getContext(), R.anim.like_anim));
        }
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        private final ImageView postImg;
        private final TextView titleTxt;
        private final ImageButton likeBtn;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);

            postImg = itemView.findViewById(R.id.postImg);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            likeBtn = itemView.findViewById(R.id.likeBtn);
        }

        public void bind(MediaEntity media){
            Glide.with(itemView.getContext()).load(media.getUrl()).into(postImg);
            titleTxt.setText(media.getTitle());

            if(media.isLiked()){
                likeBtn.setBackgroundTintList(ColorStateList.valueOf(
                        itemView.getContext().getResources().getColor(android.R.color.holo_red_light)));
            }
            else {
                likeBtn.setBackgroundTintList(ColorStateList.valueOf(
                        itemView.getContext().getResources().getColor(android.R.color.black)));
            }

            likeBtn.setOnClickListener(v ->{
                media.setLiked(!media.isLiked());
                startScaleAnimation((ImageButton) v);

                if(media.isLiked()){
                    ((ImageButton)v).setColorFilter(itemView.getContext()
                            .getResources().getColor(android.R.color.holo_red_light));
                }
                else{
                    ((ImageButton)v).setColorFilter(itemView.getContext()
                            .getResources().getColor(android.R.color.black));
                }
            });
        }

        private void startScaleAnimation(ImageButton imageButton){
            imageButton.startAnimation(AnimationUtils.loadAnimation(imageButton.getContext(), R.anim.like_anim));
        }
    }
}

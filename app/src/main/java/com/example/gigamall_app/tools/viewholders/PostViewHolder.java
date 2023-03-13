package com.example.gigamall_app.tools.viewholders;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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
import com.example.gigamall_app.interfaces.OnCommentClickListener;

import java.util.List;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public final ImageView userImg;
    public final TextView userNameTxt, postTitleTxt;
    public final AppCompatButton commentBtn;
    public final ImageButton likeBtn;

    private ImageView[] imageViews;
    private Context context;

    private final double screenWidth;
    private final OnCommentClickListener onCommentClickListener;

    public PostViewHolder(@NonNull View itemView, int count, int screenWidth, OnCommentClickListener onCommentClickListener) {
        super(itemView);
        this.onCommentClickListener = onCommentClickListener;
        this.screenWidth = screenWidth;

        userImg = itemView.findViewById(R.id.userImg);

        userNameTxt = itemView.findViewById(R.id.userNameTxt);
        postTitleTxt = itemView.findViewById(R.id.postTitleTxt);

        commentBtn = itemView.findViewById(R.id.commentsBtn);
        likeBtn = itemView.findViewById(R.id.likeBtn);

        imageViews = new ImageView[count];

        for(int i=0;i<count;i++){
            switch (i){
                case 0:
                    imageViews[i] = itemView.findViewById(R.id.firstImg);
                    break;
                case 1:
                    imageViews[i] = itemView.findViewById(R.id.secondImg);
                    break;
                case 2:
                    imageViews[i] = itemView.findViewById(R.id.thirdImg);
                    break;
                default:
                    imageViews[i] = itemView.findViewById(R.id.fourthImg);
            }
        }

        context = itemView.getContext();
    }

    public void bind(PostEntity post){
        userNameTxt.setText(post.getBrand().getName());
        postTitleTxt.setText(post.getTitle());

        if(post.isLiked()){
            likeBtn.setBackgroundTintList(ColorStateList.valueOf(
                    context.getResources().getColor(android.R.color.holo_red_light)));
        }
        else {
            likeBtn.setBackgroundTintList(ColorStateList.valueOf(
                    context.getResources().getColor(android.R.color.black)));
        }

        likeBtn.setOnClickListener(v ->{
            post.setLiked(!post.isLiked());
            startScaleAnimation((ImageButton) v);

            if(post.isLiked()){
                ((ImageButton)v).setColorFilter(context.getResources().getColor(android.R.color.holo_red_light));
            }
            else{
                ((ImageButton)v).setColorFilter(context.getResources().getColor(android.R.color.black));
            }
        });
        Glide.with(context).load(post.getBrand().getLogoUrl()).into(userImg);

        commentBtn.setOnClickListener(v ->{
            onCommentClickListener.onCommentClick(post.getId());
        });

        if(imageViews.length == 1){
            scaleImagesWithOnePic(post.getImages());
        }
        else if(imageViews.length == 2){
            scaleImagesWithTwoPics(post.getImages());
        }
        else if(imageViews.length >= 3){
            scaleImagesWithManyPics(post.getImages());
        }

        for(int i=0;i<imageViews.length;i++){
            Glide.with(context).load(post.getImages().get(i).getUrl()).into(imageViews[i]);
        }
    }

    private void scaleImagesWithOnePic(List<MediaEntity> images){
        MediaEntity media = images.get(0);

        imageViews[0].getLayoutParams().height = scaleDown(media.getWidth(), media.getHeight(), screenWidth);
        imageViews[0].requestLayout();
    }

    private void scaleImagesWithTwoPics(List<MediaEntity> images){
        MediaEntity first = images.get(0);
        MediaEntity second = images.get(1);

        int firstHeight = scaleDown(first.getWidth(), first.getHeight(), screenWidth / 2);
        int secondHeight = scaleDown(second.getWidth(), second.getHeight(), screenWidth / 2);

        int min = Math.min(firstHeight, secondHeight);

        imageViews[0].getLayoutParams().height = min;
        imageViews[1].getLayoutParams().height = min;

        imageViews[0].requestLayout();
        imageViews[1].requestLayout();
    }

    private void scaleImagesWithManyPics(List<MediaEntity> images){
        int largest = 0;
        double largestHeight = Double.MIN_VALUE;

        for(int i=0;i<images.size();i++){
            MediaEntity media = images.get(i);

            if(largestHeight < scaleDown(media.getWidth(), media.getHeight(), screenWidth / 2)){
                largestHeight = scaleDown(media.getWidth(), media.getHeight(), screenWidth / 2);
                largest = i;
            }
        }

        MediaEntity temp = images.get(0);
        images.set(0, images.get(largest)); // first elem is the largest
        images.set(largest, temp); // largest position is old first elem

        imageViews[0].getLayoutParams().height = (int) largestHeight;
        imageViews[0].requestLayout();
    }

    private int scaleDown(int width, int height, double widthBound){
        if(width > widthBound){
            return (int) ((widthBound / width) * height);
        }

        return height;
    }

    private void startScaleAnimation(ImageButton imageButton){
        imageButton.startAnimation(AnimationUtils.loadAnimation(imageButton.getContext(), R.anim.like_anim));
    }
}
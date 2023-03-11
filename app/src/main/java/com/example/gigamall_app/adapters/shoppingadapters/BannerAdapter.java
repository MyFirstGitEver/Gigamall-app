package com.example.gigamall_app.adapters.shoppingadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        return new BannerViewHolder(inflater.inflate(R.layout.a_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        Context context = holder.bannerImg.getContext();

        switch (position){
            case 0:
                Glide.with(context).load(R.drawable.banner1).into(holder.bannerImg);
                break;
            case 1:
                Glide.with(context).load(R.drawable.banner2).into(holder.bannerImg);
                break;
            default:
                Glide.with(context).load(R.drawable.banner3).into(holder.bannerImg);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView bannerImg;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerImg = itemView.findViewById(R.id.bannerImg);
        }
    }
}

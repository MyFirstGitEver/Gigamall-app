package com.example.gigamall_app.adapters.shoppingadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.tools.viewholders.NullViewHolder;
import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.BrandEntity;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BrandEntity> brands;

    public BrandAdapter(){

    }

    public BrandAdapter(List<BrandEntity> brands){
        this.brands = brands;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(brands == null){
            return new NullViewHolder(inflater.inflate(R.layout.full_parent_loading, parent, false));
        }
        else{
            return new BrandViewHolder(inflater.inflate(R.layout.a_brand, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(brands == null){
            return;
        }

        Glide.with(holder.itemView.getContext()).load(brands.get(position).getLogoUrl()).into(((BrandViewHolder)holder).brandImg);
    }

    @Override
    public int getItemCount() {
        if(brands == null){
            return 1;
        }
        else{
            return brands.size();
        }
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder {
        private ImageView brandImg;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);

            brandImg = itemView.findViewById(R.id.brandImg);
        }
    }
}

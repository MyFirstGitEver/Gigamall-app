package com.example.gigamall_app.adapters.shoppingadapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.fragments.product_related.ByCategoryFragment;
import com.example.gigamall_app.interfaces.OpenCategoryListener;

import java.util.List;

public class FashionListAdapter extends RecyclerView.Adapter<FashionListAdapter.FashionViewHolder> {
    private final List<ProductEntity> products;
    private final int position;
    private final OpenCategoryListener listener;

    private static final int MALE = 2;
    private static final int FEMALE = 3;

    public FashionListAdapter(OpenCategoryListener listener, List<ProductEntity> products, int position) {
        this.products = products;
        this.position = position;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FashionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FashionViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.a_fashion_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FashionViewHolder holder, int position) {
        ProductEntity product = products.get(position);

        holder.descriptionTxt.setText(product.getDescription());
        Glide.with(holder.itemView.getContext()).load(product.getUrl()).into(holder.productImg);

        holder.findDetailsBtn.setOnClickListener(v ->{
            if(this.position == MALE){
                listener.onOpenCategory("women's clothing");
            }
            else{
                listener.onOpenCategory("men's clothing");
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class FashionViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTxt;
        ImageView productImg;
        AppCompatButton findDetailsBtn;

        public FashionViewHolder(@NonNull View itemView) {
            super(itemView);

            descriptionTxt = itemView.findViewById(R.id.descriptionTxt);
            findDetailsBtn = itemView.findViewById(R.id.findDetailsBtn);
            productImg = itemView.findViewById(R.id.productImg);
        }
    }
}
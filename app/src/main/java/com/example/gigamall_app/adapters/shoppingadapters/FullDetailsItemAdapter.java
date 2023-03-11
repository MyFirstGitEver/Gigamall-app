package com.example.gigamall_app.adapters.shoppingadapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.tools.viewholders.DetailsViewHolder;
import com.example.gigamall_app.tools.viewholders.NullViewHolder;
import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.interfaces.OpenCategoryListener;

import java.util.List;

public class FullDetailsItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final List<ProductEntity> hotProducts;
    private final int layoutId;

    private final OnProductClickListener onProductClickListener;
    private OpenCategoryListener openCategoryListener;

    private static final int LOADING = 0;
    private static final int PRODUCT = 1;

    public FullDetailsItemAdapter(List<ProductEntity> hotProducts, int layoutId,
                                  OnProductClickListener onProductClickListener) {
        this.hotProducts = hotProducts;
        this.layoutId = layoutId;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == LOADING){
            return new NullViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.a_loading,  parent, false));
        }
        return new DetailsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(layoutId,  parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        if(hotProducts.get(position) == null){
            return LOADING;
        }

        return PRODUCT;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int position) {
        if(hotProducts.get(position) == null){
            return;
        }

        ProductEntity product = hotProducts.get(position);
        DetailsViewHolder holder = (DetailsViewHolder) Holder;

        Glide.with(holder.productImg.getContext()).load(product.getUrl()).into(holder.productImg);
        holder.titleTxt.setText(product.getTitle());
        holder.priceTxt.setText(product.getPrice() + " $");

        holder.container.setOnClickListener(v ->{
            onProductClickListener.onProductClick(product);
        });

        if(holder.seeMoreBtn == null){
            return;
        }

        if(openCategoryListener == null){
            holder.seeMoreBtn.setVisibility(View.GONE);
        }
        else{
            holder.seeMoreBtn.setVisibility(View.VISIBLE);

            holder.seeMoreBtn.setOnClickListener( v ->
                    openCategoryListener.onOpenCategory(product.getType()));
        }
    }

    @Override
    public int getItemCount() {
        return hotProducts.size();
    }

    public void addFooter(){
        hotProducts.add(null);
        notifyItemInserted(hotProducts.size() - 1);
    }

    public void removeFooter(){
        hotProducts.remove(hotProducts.size() - 1);
        notifyItemRemoved(hotProducts.size());
    }

    public void addProducts(List<ProductEntity> products){
        int oldLength = hotProducts.size();

        hotProducts.addAll(products);
        notifyItemRangeInserted(oldLength, products.size());
    }

    public void reset(){
        notifyItemRangeRemoved(0, hotProducts.size());
        hotProducts.clear();
    }

    public void setOpenCategoryListener(OpenCategoryListener openCategoryListener) {
        this.openCategoryListener = openCategoryListener;
    }


}
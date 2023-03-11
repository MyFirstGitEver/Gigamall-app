package com.example.gigamall_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.shoppingadapters.BannerAdapter;
import com.example.gigamall_app.adapters.shoppingadapters.BrandAdapter;
import com.example.gigamall_app.adapters.shoppingadapters.FashionListAdapter;
import com.example.gigamall_app.adapters.shoppingadapters.FullDetailsItemAdapter;
import com.example.gigamall_app.entities.BrandEntity;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.interfaces.OpenCategoryListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<ProductEntity> products;
    private List<BrandEntity> brands;
    private final OnProductClickListener onProductClickListener;
    private final OpenCategoryListener openCategoryListener;

    public MainListAdapter(
            List<ProductEntity> products,
            OnProductClickListener onProductClickListener,
            OpenCategoryListener openCategoryListener){
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        this.openCategoryListener = openCategoryListener;

        Collections.sort(products, new Comparator<ProductEntity>() {
            @Override
            public int compare(ProductEntity p1, ProductEntity p2) {
                return p1.getSold() - p2.getSold();
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 0:
                return new BannerViewHolder(inflater.inflate(R.layout.banner_part, parent, false));
            case 1:
                return new HotSalesViewHolder(inflater.inflate(R.layout.hot_sale_part, parent, false));
            case 2:
            case 3:
                return new FashionViewHolder(inflater.inflate(R.layout.fashion_part, parent, false));
            case 4:
                return new ElectronicsViewHolder(inflater.inflate(R.layout.electronics_part, parent, false));
            case 5:
                return new CategoryViewHolder(inflater.inflate(R.layout.category_part, parent, false));
            case 6:
                return new RecommendedViewHolder(inflater.inflate(R.layout.recommended_products_part, parent, false));
            default:
                return new BrandViewHolder(inflater.inflate(R.layout.brands_part, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        switch (position){
            case 0:
                BannerViewHolder bannerHolder = (BannerViewHolder) holder;
                bannerHolder.bannerPager.setAdapter(new BannerAdapter());

                bannerHolder.syncTabs();
                break;
            case 1:
                HotSalesViewHolder hotSalesHolder = (HotSalesViewHolder) holder;

                hotSalesHolder.hotSaleList.setAdapter(new FullDetailsItemAdapter(
                        products.subList(products.size() - 5, products.size()), R.layout.a_hot_sale, onProductClickListener));
                hotSalesHolder.hotSaleList.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.HORIZONTAL, false));
                break;
            case 2:
            case 3:
                FashionViewHolder fashionHolder = (FashionViewHolder) holder;
                List<ProductEntity> products;

                if(position == 2){
                    fashionHolder.titleTxt.setText(R.string.male);
                    products = filterUsingType("men's clothing");
                }
                else{
                    fashionHolder.titleTxt.setText(R.string.female);
                    products = filterUsingType("women's clothing");
                }

                fashionHolder.fashionList.setAdapter(new FashionListAdapter(openCategoryListener, products, position));
                break;
            case 4:
                ElectronicsViewHolder electronicsHolder = (ElectronicsViewHolder) holder;

                electronicsHolder.electronicsList.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

                electronicsHolder.electronicsList.setAdapter(
                        new FullDetailsItemAdapter(filterUsingType("electronics"), R.layout.a_electronics_item, onProductClickListener));
                break;
            case 5:
                CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;

                categoryHolder.maleFashionBtn.setOnClickListener(
                        v -> openCategoryListener.onOpenCategory("men's clothing"));

                categoryHolder.femaleFashionBtn.setOnClickListener(
                        v -> openCategoryListener.onOpenCategory("women's clothing"));

                categoryHolder.electronicsBtn.setOnClickListener(
                        v -> openCategoryListener.onOpenCategory("electronics"));

                categoryHolder.jewelBtn.setOnClickListener(
                        v -> openCategoryListener.onOpenCategory("jewelery"));
                break;
            case 6:
                RecommendedViewHolder recommendedHolder = (RecommendedViewHolder) holder;

                recommendedHolder.recommendedList.setLayoutManager(new GridLayoutManager(context, 2));
                recommendedHolder.recommendedList.setAdapter(
                        new FullDetailsItemAdapter(getRandomProducts(), R.layout.a_recommended, onProductClickListener));
                break;
            case 7:
                BrandViewHolder brandHolder = (BrandViewHolder) holder;

                if(brands == null){
                    brandHolder.brandList.setLayoutManager(new LinearLayoutManager(context));
                    brandHolder.brandList.setAdapter(new BrandAdapter());
                }
                else{
                    brandHolder.brandList.setLayoutManager(new GridLayoutManager(context, 3));
                    brandHolder.brandList.setAdapter(new BrandAdapter(brands));
                }
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public void setBrands(List<BrandEntity> brands){
        this.brands = brands;
        notifyItemChanged(7);
    }

    private List<ProductEntity> filterUsingType(String type){
        List<ProductEntity> filtered = new ArrayList<>();

        for(ProductEntity product : products){
            if(product.getType().equals(type)){
                filtered.add(product);
            }
        }

        return filtered;
    }

    private List<ProductEntity> getRandomProducts(){
        List<Integer> ints = new ArrayList<>();
        List<ProductEntity> randoms = new ArrayList<>();

        for(int i=0;i<20;i++){
            ints.add(i);
        }

        Collections.shuffle(ints);

        for(int i : ints.subList(0, 6)){
            randoms.add(products.get(i));
        }

        return randoms;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        private ViewPager2 bannerPager;
        private TabLayout dotsTab;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerPager = itemView.findViewById(R.id.bannerPager);
            dotsTab = itemView.findViewById(R.id.dotsTab);
        }

        public void syncTabs(){
            new TabLayoutMediator(dotsTab, bannerPager,
                    (tab, position) -> {}
            ).attach();
        }
    }

    public class HotSalesViewHolder extends RecyclerView.ViewHolder{
        RecyclerView hotSaleList;

        public HotSalesViewHolder(@NonNull View itemView) {
            super(itemView);
            hotSaleList = itemView.findViewById(R.id.hotSaleList);
        }
    }

    public class FashionViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTxt;
        private ViewPager2 fashionList;

        public FashionViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.desTxt);
            fashionList = itemView.findViewById(R.id.fashionList);
        }
    }

    public class ElectronicsViewHolder extends RecyclerView.ViewHolder{
        RecyclerView electronicsList;

        public ElectronicsViewHolder(@NonNull View itemView) {
            super(itemView);

            electronicsList = itemView.findViewById(R.id.electronicsList);
        }
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        AppCompatButton maleFashionBtn;
        AppCompatButton femaleFashionBtn;
        AppCompatButton electronicsBtn;
        AppCompatButton jewelBtn;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            maleFashionBtn = itemView.findViewById(R.id.maleFashionBtn);
            femaleFashionBtn = itemView.findViewById(R.id.femaleFashionBtn);
            electronicsBtn = itemView.findViewById(R.id.electronicsBtn);
            jewelBtn = itemView.findViewById(R.id.jewelBtn);
        }
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recommendedList;
        AppCompatButton seeMoreBtn;

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);

            recommendedList = itemView.findViewById(R.id.recommendedList);
            seeMoreBtn = itemView.findViewById(R.id.seeMoreBtn);
        }
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder {
        RecyclerView brandList;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);

            brandList = itemView.findViewById(R.id.brandList);
        }
    }
}
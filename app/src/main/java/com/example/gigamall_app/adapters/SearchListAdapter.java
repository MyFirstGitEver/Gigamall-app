package com.example.gigamall_app.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.arrayadapters.SuggestionAdapter;
import com.example.gigamall_app.adapters.shoppingadapters.FullDetailsItemAdapter;
import com.example.gigamall_app.dtos.LoadingState;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.interfaces.OnSearchClickListener;
import com.example.gigamall_app.services.ProductService;
import com.example.gigamall_app.tools.SearchTextManager;
import com.example.gigamall_app.tools.viewholders.DetailsViewHolder;
import com.example.gigamall_app.tools.viewholders.NullViewHolder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static int SEARCH__PART = 0;
    private static  int PRODUCT = 1;
    private static  int LOADING = 2;

    private final OnProductClickListener onProductClickListener;
    private final OnSearchClickListener onSearchClickListener;
    private final List<ProductEntity> products;
    private final LoadingState loadingState;

    public SearchListAdapter(
            OnProductClickListener onProductClickListener, OnSearchClickListener onSearchClickListener, List<ProductEntity> products,
            LoadingState loadingState) {
        this.onProductClickListener = onProductClickListener;
        this.onSearchClickListener = onSearchClickListener;
        this.products = products;
        this.loadingState = loadingState;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == SEARCH__PART){
            return new SearchViewHolder(inflater.inflate(R.layout.search_part, parent, false));
        }
        else if(viewType == PRODUCT){
            return new DetailsViewHolder(inflater.inflate(R.layout.a_recommended, parent, false));
        }
        else{
            return new NullViewHolder(inflater.inflate(R.layout.a_loading, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return SEARCH__PART;
        }
        else{
            if(products.get(itemPos(position)) == null){
                return LOADING;
            }

            return PRODUCT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            onBindSearchPart((SearchViewHolder) holder);
        }
        else if(products.get(itemPos(position)) != null){
            onBindProduct((DetailsViewHolder) holder, products.get(itemPos(position)));
        }
    }

    public void loadMoreProducts(Response<List<ProductEntity>> response){
        products.remove(products.size() - 1); // remove footer
        notifyItemRemoved(products.size() + 1);

        int oldLength = products.size();
        int sz = response.body().size();

        products.addAll(response.body());
        if(response.body().size() != 0){
            products.add(null);
            sz++;
        }
        else{
            loadingState.setLastPage(true);
        }

        for(int i=oldLength;i<oldLength+sz;i++){
            notifyItemInserted(i);
        }

        loadingState.setLoading(false);
    }

    public LoadingState getLoadingState() {
        return loadingState;
    }

    @Override
    public int getItemCount() {
        return 1 + products.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        RecyclerView pictureSuggestionList;
        AutoCompleteTextView searchEditTxt;
        ImageButton closeBtn;
        TextView confirmTxt;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            pictureSuggestionList = itemView.findViewById(R.id.pictureSuggestionList);
            searchEditTxt = itemView.findViewById(R.id.searchEditTxt);
            closeBtn = itemView.findViewById(R.id.closeBtn);
            confirmTxt = itemView.findViewById(R.id.confirmTxt);
        }
    }

    private void loadMoreSuggestions(String term){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            ProductService.service.search(term, 0).enqueue(new Callback<List<ProductEntity>>() {
                @Override
                public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                    loadMoreProducts(response);
                    onSearchClickListener.onSearchClick(term);
                }

                @Override
                public void onFailure(Call<List<ProductEntity>> call, Throwable t) {

                }
            });
        }, 200);
    }

    private void onBindSearchPart(SearchViewHolder holder){
        Context context = holder.itemView.getContext();

        holder.pictureSuggestionList.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.pictureSuggestionList.setAdapter(new FullDetailsItemAdapter(new ArrayList<>(),
                R.layout.a_electronics_item,
                onProductClickListener));

        if(loadingState.isSearched()){
            holder.confirmTxt.setText(holder.itemView.getContext().getString(
                    R.string.search_result, SearchTextManager.getLastTerm(context)));
            holder.confirmTxt.setVisibility(View.VISIBLE);
        }
        else{
            holder.confirmTxt.setVisibility(View.GONE);
        }

        holder.searchEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String term = textView.getText().toString();
                    SearchTextManager.storeLastTerm(textView.getContext(), term);
                    SearchTextManager.insertNewHint(textView.getContext(), term);

                    notifyItemRangeRemoved(1, products.size());
                    products.clear();
                    products.add(null);
                    notifyItemInserted(1);

                    loadMoreSuggestions(term);
                    loadingState.setLoading(true);

                    textView.setText("");
                    loadingState.setCurrentPage(0);
                    loadingState.setLastPage(false);
                    loadingState.setSearched(true);
                    return true;
                }

                return false;
            }
        });

        holder.searchEditTxt.setAdapter(new SuggestionAdapter(context,
                R.layout.a_suggestion,
                new ArrayList<>()));

        ProductService.service.getMostPopular().enqueue(new Callback<List<ProductEntity>>() {
            @Override
            public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                ((FullDetailsItemAdapter)holder.pictureSuggestionList.getAdapter()).addProducts(response.body());
            }

            @Override
            public void onFailure(Call<List<ProductEntity>> call, Throwable t) {

            }
        });
    }

    private void onBindProduct(DetailsViewHolder holder, ProductEntity product){
        Glide.with(holder.productImg.getContext()).load(product.getUrl()).into(holder.productImg);
        holder.titleTxt.setText(product.getTitle());
        holder.priceTxt.setText(product.getPrice() + " $");

        holder.container.setOnClickListener(v ->{
            onProductClickListener.onProductClick(product);
        });

        if(holder.seeMoreBtn == null){
            return;
        }

        holder.seeMoreBtn.setVisibility(View.GONE);
    }

    private int itemPos(int position){
        return position - 1;
    }
}
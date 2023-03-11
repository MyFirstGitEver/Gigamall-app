package com.example.gigamall_app.fragments.product_related;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.SearchListAdapter;
import com.example.gigamall_app.dtos.LoadingState;
import com.example.gigamall_app.entities.ProductEntity;

import com.example.gigamall_app.interfaces.OnEndReachedListener;
import com.example.gigamall_app.interfaces.OnSearchClickListener;
import com.example.gigamall_app.services.ProductService;

import com.example.gigamall_app.tools.CustomAppBarLayoutBehaviour;
import com.example.gigamall_app.tools.SearchTextManager;
import com.example.gigamall_app.viewmodels.SearchFragmentViewModel;
import com.google.android.material.appbar.AppBarLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends ProductRelatedFragment {

    private RecyclerView searchList;
    private LoadingState loadingState;

    private AppBarLayout appBarLayout;
    private TextView titleTxt;
    private ImageButton searchBtn;

    private final OnSearchClickListener onSearchClickListener = (term) ->{
        searchList.scrollToPosition(0);

        titleTxt.setText("Kết quả tìm kiếm cho \"" + term + "\"");
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehaviour)layoutParams.getBehavior()).setScrollBehavior(true);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle);

        if(savedInstanceState != null){
            loadingState = savedInstanceState.getParcelable("loadingState");
        }
        else{
            loadingState = new LoadingState(0, false, false, false);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null)
        {
            dialog.getWindow().getAttributes().windowAnimations = R.style.appearFromTop;
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);

        searchList = view.findViewById(R.id.searchList);
        appBarLayout = view.findViewById(R.id.appBarLayout);

        searchBtn = view.findViewById(R.id.searchBtn);
        titleTxt = view.findViewById(R.id.titleTxt);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViewModel();
        initListBehaviours();

        searchBtn.setOnClickListener(v -> searchList.scrollToPosition(0));

        appBarLayout.setExpanded(false);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        ((CustomAppBarLayoutBehaviour)layoutParams.getBehavior()).setScrollBehavior(loadingState.isSearched());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("loadingState", loadingState);
    }

    private void initListBehaviours() {
        searchList.setLayoutManager(new LinearLayoutManager(getContext()));
        searchList.addOnScrollListener(new OnEndReachedListener() {
            @Override
            public void onEndReached() {
                SearchListAdapter adapter = ((SearchListAdapter)searchList.getAdapter());

                adapter.getLoadingState().setLoading(true);
                adapter.getLoadingState().setCurrentPage(
                        adapter.getLoadingState().getCurrentPage() + 1);

                String lastTerm = SearchTextManager.getLastTerm(getContext());

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> {
                    ProductService.service.search(lastTerm, adapter.getLoadingState().getCurrentPage())
                            .enqueue(new Callback<List<ProductEntity>>() {
                                @Override
                                public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                                    ((SearchListAdapter)searchList.getAdapter()).loadMoreProducts(response);
                                }

                                @Override
                                public void onFailure(Call<List<ProductEntity>> call, Throwable t) {

                                }
                            });
                }, 200);
            }

            @Override
            public boolean isLastPage() {
                return ((SearchListAdapter)searchList.getAdapter()).getLoadingState().isLastPage();
            }

            @Override
            public boolean isLoading() {
                return ((SearchListAdapter)searchList.getAdapter()).getLoadingState().isLoading();
            }
        });
    }

    private void initViewModel() {
        SearchFragmentViewModel viewModel = new ViewModelProvider(this).get(SearchFragmentViewModel.class);

        viewModel.getSearchResultsHolder().observe(getViewLifecycleOwner(), new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(List<ProductEntity> products) {
                searchList.setAdapter(new SearchListAdapter(onProductClickListener, onSearchClickListener, products,
                        loadingState));
            }
        });
    }
}
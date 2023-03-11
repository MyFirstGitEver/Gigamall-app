package com.example.gigamall_app.fragments.product_related;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.shoppingadapters.FullDetailsItemAdapter;
import com.example.gigamall_app.dialogs.PriceFilterDialog;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.OnEndReachedListener;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ByCategoryFragment extends ProductRelatedFragment {
    private RecyclerView productList;
    private AppCompatButton filterBtn, findMoreBtn;
    private ImageButton closeBtn, searchBtn;
    private TextView categoryTxt, filterTxt;
    private Spinner sortBySpinner;

    private int currentPage;
    private boolean isLoading, isLastPage;
    private float minPrice, maxPrice, currentMin = 0.0f, currentMax = Float.MAX_VALUE;

    private final View.OnClickListener startFilteringListener = v -> {
        PriceFilterDialog dialog = new PriceFilterDialog();

        Bundle bundle = new Bundle();
        bundle.putFloat("min", minPrice);
        bundle.putFloat("max", maxPrice);

        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "filter");
    };

    private final View.OnClickListener closeListener = v -> {
        filterTxt.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);

        currentMin = 0.0f;
        currentMax = Float.MAX_VALUE;
        currentPage = 0;

        filter( 0);
    };

    private final View.OnClickListener findMoreListener = v ->{
        FindMoreFragment fragment = new FindMoreFragment();
        fragment.show(getParentFragmentManager(), "find more");
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialogStyle);
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
        View view = inflater.inflate(R.layout.by_category, container, false);

        productList = view.findViewById(R.id.productList);

        findMoreBtn = view.findViewById(R.id.findMoreBtn);
        filterBtn = view.findViewById(R.id.filterBtn);
        closeBtn = view.findViewById(R.id.closeBtn);
        searchBtn = view.findViewById(R.id.searchBtn);

        filterTxt = view.findViewById(R.id.filterPriceTxt);
        categoryTxt = view.findViewById(R.id.categoryTxt);

        sortBySpinner = view.findViewById(R.id.sortBySpinner);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initList();
        initViewData();

        getParentFragmentManager().setFragmentResultListener("filter", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                currentMin = result.getFloat("start");
                currentMax = result.getFloat("end");

                filterTxt.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.VISIBLE);

                filterTxt.setText(getContext().getResources().getString(R.string.range, currentMin, currentMax));

                currentPage = 0;
                filter(sortBySpinner.getSelectedItemPosition());
            }
        });

        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                currentPage = 0;
                filter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initViewData(){
        filterBtn.setOnClickListener(startFilteringListener);
        findMoreBtn.setOnClickListener(findMoreListener);
        closeBtn.setOnClickListener(closeListener);
        searchBtn.setOnClickListener(onSearchClickListener);

        categoryTxt.setText(getContext().getResources().getString(R.string.tag, getArguments().getString("cat")));
    }

    private void initList(){
        productList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productList.setAdapter(
                new FullDetailsItemAdapter(new ArrayList<>(), R.layout.a_recommended, onProductClickListener));
        productList.addOnScrollListener(new OnEndReachedListener() {
            @Override
            public void onEndReached() {
                isLoading = true;
                currentPage++;

                Handler handler = new Handler();
                handler.postDelayed(() -> filter(sortBySpinner.getSelectedItemPosition()),
                        200);
            }

            @Override
            public boolean isLastPage() {
                return isLoading;
            }

            @Override
            public boolean isLoading() {
                return isLastPage;
            }
        });
    }

    private void filter(int sortCondition){
        ProductService.service.getCategorizedProducts(
                getArguments().getString("cat"), currentPage, sortCondition, currentMin, currentMax)
                .enqueue(new Callback<List<ProductEntity>>() {
                    @Override
                    public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                        resultHandler(response);
                    }

                    @Override
                    public void onFailure(Call<List<ProductEntity>> call, Throwable t) {

                    }
                });
    }

    private void resultHandler(Response<List<ProductEntity>> response){
        FullDetailsItemAdapter adapter = (FullDetailsItemAdapter) productList.getAdapter();

        if(currentPage == 0){
            minPrice = Float.parseFloat(response.headers().get("min"));
            maxPrice = Float.parseFloat(response.headers().get("max"));

            isLastPage = false;
            adapter.reset();
        }
        else{
            adapter.removeFooter();
        }

        if(response.body().size() == 0){
            isLastPage = true;
            return;
        }

        adapter.addProducts(response.body());

        if(response.body().size() == 6){
            adapter.addFooter();
        }
        else{
            isLastPage = true;
        }

        isLoading = false;
    }
}
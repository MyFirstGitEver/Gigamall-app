package com.example.gigamall_app.fragments.shoppingfragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.fragments.product_related.ByCategoryFragment;
import com.example.gigamall_app.fragments.product_related.ProductFragment;
import com.example.gigamall_app.fragments.product_related.SearchFragment;
import com.example.gigamall_app.interfaces.MainPageScrollListener;
import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.MainListAdapter;
import com.example.gigamall_app.dialogs.StarUsageDialog;
import com.example.gigamall_app.entities.BrandEntity;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.interfaces.OpenCategoryListener;
import com.example.gigamall_app.services.ProductService;
import com.example.gigamall_app.viewmodels.GigaMallActivityViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingFragment extends Fragment {
    private boolean isBannerVisible = true;

    private RecyclerView mainList;
    private MotionLayout mainContainer;

    private ImageButton searchBtn2;
    private AppCompatButton searchBtn;

    private GigaMallActivityViewModel model;

    private final OpenCategoryListener openCategoryListener = (category) ->{
        ByCategoryFragment fragment = new ByCategoryFragment();
        Bundle bundle = new Bundle();

        bundle.putString("cat", category);
        fragment.setArguments(bundle);
        fragment.show(getParentFragmentManager(), "product fragment");
    };

    private final OnProductClickListener onProductClickListener = (product) ->{
        ProductFragment fragment = new ProductFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);

        fragment.setArguments(bundle);

        fragment.show(getParentFragmentManager(), "product fragment");
    };

    private final View.OnClickListener onSearchClickListener = v -> {
        SearchFragment fragment = new SearchFragment();

        fragment.show(getParentFragmentManager(), "search");
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_fragment, container, false);

        mainList = view.findViewById(R.id.mainList);
        mainContainer = view.findViewById(R.id.mainContainer);

        searchBtn = view.findViewById(R.id.searchBtn);
        searchBtn2 = view.findViewById(R.id.searchBtn2);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null){
            isBannerVisible = savedInstanceState.getBoolean("isBannerVisible");
        }

        mainList.setLayoutManager(new LinearLayoutManager(getContext()));

        model = new ViewModelProvider(this).get(GigaMallActivityViewModel.class);

        showFirstLoginDialog();
        observeViewModel();

        searchBtn.setOnClickListener(onSearchClickListener);
        searchBtn2.setOnClickListener(onSearchClickListener);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("isBannerVisible", isBannerVisible);
    }

    private void observeViewModel() {
        model.observeProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductEntity>>() {
            @Override
            public void onChanged(List<ProductEntity> products) {
                if(products == null){
                    fetchProducts();
                    return;
                }

                mainList.setAdapter(new MainListAdapter(products, onProductClickListener, openCategoryListener));
                mainList.addOnScrollListener(new MainPageScrollListener() {
                    @Override
                    public void onLookingAtBrands() {
                        if(model.fetchedBrands()){
                            return;
                        }

                        fetchBrands();
                    }

                    @Override
                    protected void onBannerVisible() {
                        if(!isBannerVisible){
                            mainContainer.transitionToStart();
                            isBannerVisible = true;
                        }
                    }

                    @Override
                    public void onBannerInvisible() {
                        if(isBannerVisible){
                            mainContainer.transitionToEnd();
                            isBannerVisible = false;
                        }
                    }
                });
            }
        });

        model.observeBrands().observe(getViewLifecycleOwner(), new Observer<List<BrandEntity>>() {
            @Override
            public void onChanged(List<BrandEntity> brands) {
                if(brands == null){
                    return;
                }

                ((MainListAdapter)mainList.getAdapter()).setBrands(brands);
            }
        });
    }

    private void showFirstLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn được tặng 100 sao! Nhấn vào đường dẫn bên dưới để xem thêm")
                .setTitle("Tặng quà đăng nhập lần đầu")
                .setCancelable(false)
                .setPositiveButton("Tôi đã hiểu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton("Tìm hiểu thêm...", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showStarUsageDialog();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void fetchBrands() {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(() -> {
            ProductService.service.getAlLBrands().enqueue(new Callback<List<BrandEntity>>() {
                @Override
                public void onResponse(Call<List<BrandEntity>> call, Response<List<BrandEntity>> response) {
                    model.setBrands(response.body());
                }

                @Override
                public void onFailure(Call<List<BrandEntity>> call, Throwable t) {

                }
            });
        }, 150);
    }

    private void fetchProducts(){
        ProductService.service.getAll().enqueue(new Callback<List<ProductEntity>>() {
            @Override
            public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                model.setProducts(response.body());
            }

            @Override
            public void onFailure(Call<List<ProductEntity>> call, Throwable t) {

            }
        });
    }

    private void showStarUsageDialog(){
        StarUsageDialog dialog = new StarUsageDialog();
        dialog.show(getParentFragmentManager(), "rules");
    }
}

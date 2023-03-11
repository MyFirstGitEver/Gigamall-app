package com.example.gigamall_app.fragments.product_related;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.shoppingadapters.FullDetailsItemAdapter;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.interfaces.OpenCategoryListener;
import com.example.gigamall_app.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindMoreFragment extends ProductRelatedFragment {
    private RecyclerView typeList;
    private ImageButton closeBtn, searchBtn;

    private final OpenCategoryListener openCategoryListener = (category) ->{
        ByCategoryFragment fragment = new ByCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("cat", category);

        fragment.setArguments(bundle);
        fragment.show(getParentFragmentManager(), "open category");
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
        return inflater.inflate(R.layout.find_more_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        typeList = view.findViewById(R.id.typeList);
        closeBtn = view.findViewById(R.id.closeBtn);
        searchBtn = view.findViewById(R.id.searchBtn);

        typeList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        typeList.setAdapter(new FullDetailsItemAdapter(new ArrayList<>(), R.layout.a_recommended, onProductClickListener));
        ((FullDetailsItemAdapter)typeList.getAdapter()).setOpenCategoryListener(openCategoryListener);

        ProductService.service.getEachCategory().enqueue(new Callback<List<ProductEntity>>() {
            @Override
            public void onResponse(Call<List<ProductEntity>> call, Response<List<ProductEntity>> response) {
                ((FullDetailsItemAdapter)typeList.getAdapter()).addProducts(response.body());
            }

            @Override
            public void onFailure(Call<List<ProductEntity>> call, Throwable t) {

            }
        });

        closeBtn.setOnClickListener(v -> dismiss());
        searchBtn.setOnClickListener(onSearchClickListener);
    }
}
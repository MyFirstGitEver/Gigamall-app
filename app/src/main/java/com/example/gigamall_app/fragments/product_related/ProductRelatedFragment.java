package com.example.gigamall_app.fragments.product_related;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.example.gigamall_app.interfaces.OnProductClickListener;

public class ProductRelatedFragment extends DialogFragment {
    protected final OnProductClickListener onProductClickListener = (product) ->{
        ProductFragment fragment = new ProductFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);

        fragment.setArguments(bundle);

        fragment.show(getParentFragmentManager(), "product fragment");
    };

    protected final View.OnClickListener onSearchClickListener = v -> {
        SearchFragment fragment = new SearchFragment();

        fragment.show(getParentFragmentManager(), "search");
    };
}
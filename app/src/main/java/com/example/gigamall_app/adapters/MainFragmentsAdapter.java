package com.example.gigamall_app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gigamall_app.fragments.shoppingfragments.EventFragment;
import com.example.gigamall_app.fragments.shoppingfragments.MeFragment;
import com.example.gigamall_app.fragments.shoppingfragments.ShoppingFragment;

public class MainFragmentsAdapter extends FragmentStateAdapter {
    public MainFragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ShoppingFragment();
            case 1:
                return new EventFragment();
            default:
                return new MeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

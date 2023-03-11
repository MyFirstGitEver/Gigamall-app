package com.example.gigamall_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.MainFragmentsAdapter;
import com.example.gigamall_app.fragments.product_related.ByCategoryFragment;
import com.example.gigamall_app.fragments.product_related.ProductFragment;
import com.example.gigamall_app.interfaces.OnProductClickListener;
import com.example.gigamall_app.interfaces.OpenCategoryListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class GigaMallActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private ViewPager2 mainPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giga_mall);

        getSupportActionBar().hide();

        bottomNav = findViewById(R.id.bottomNav);
        mainPager = findViewById(R.id.mainPager);

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.shopping:
                        mainPager.setCurrentItem(0);
                        break;
                    case R.id.event:
                        mainPager.setCurrentItem(1);
                        break;
                    default:
                        mainPager.setCurrentItem(2);
                }

                return true;
            }
        });

        mainPager.setAdapter(new MainFragmentsAdapter(this));
    }
}
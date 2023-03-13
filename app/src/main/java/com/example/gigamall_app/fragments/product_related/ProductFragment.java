package com.example.gigamall_app.fragments.product_related;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.ProductMainInfoListAdapter;
import com.example.gigamall_app.dtos.CommentBoxDTO;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.interfaces.ProductPageScrollListener;
import com.example.gigamall_app.interfaces.ShowPreviewClickListener;
import com.example.gigamall_app.services.CommentService;
import com.example.gigamall_app.tools.ShowPreviewFromThumb;
import com.example.gigamall_app.viewmodels.ProductFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFragment extends ProductRelatedFragment {
    private RecyclerView productMainInfoList;
    private TextView titleTxt;
    private MotionLayout topBarContainer;
    private ImageView previewImg;
    private ConstraintLayout container;

    private AppCompatButton findMoreBtn;
    private FloatingActionButton scrollToTopBtn;
    private ImageButton backBtn, searchBtn;

    private ProductFragmentViewModel viewModel;
    private ProductEntity product;

    private ShowPreviewFromThumb presenter;

    private final View.OnClickListener findMoreClickListener = v ->{
        ByCategoryFragment fragment = new ByCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString("cat", product.getType());

        fragment.setArguments(bundle);
        fragment.show(getParentFragmentManager(), "find more");
    };

    private final View.OnClickListener smoothScrollListener = v ->{
        productMainInfoList.smoothScrollToPosition(0);
    };

    private final View.OnClickListener backListener = v ->{
        dismiss();
    };

    private final View.OnClickListener onPreviewClickListener = v ->{
        presenter.zoomOut();
    };

    private final Runnable loadComments = () -> {
        CommentService.service.getComments(product.getId(), 0 , true).enqueue(new Callback<List<CommentBoxDTO>>() {
            @Override
            public void onResponse(Call<List<CommentBoxDTO>> call, Response<List<CommentBoxDTO>> response) {
                viewModel.setComments(response.body());

                totalComments = Integer.parseInt(response.headers().get("total"));

                ((ProductMainInfoListAdapter)productMainInfoList.getAdapter()).setComments(response.body(), totalComments);
            }

            @Override
            public void onFailure(Call<List<CommentBoxDTO>> call, Throwable t) {

            }
        });
    };

    private final ShowPreviewClickListener onCommentPictureClickListener = (v, url) ->{
        presenter.zoomIn(v);
        Glide.with(v.getContext()).load(url).into(v);
    };

    private int totalComments;

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
            dialog.getWindow().getAttributes().windowAnimations = R.style.leftAnim3;
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);

        productMainInfoList = view.findViewById(R.id.productMainInfoList);
        titleTxt = view.findViewById(R.id.titleTxt);

        topBarContainer = view.findViewById(R.id.topBarContainer);

        findMoreBtn = view.findViewById(R.id.findMoreBtn);
        scrollToTopBtn = view.findViewById(R.id.scrollToTopBtn);
        backBtn = view.findViewById(R.id.posterTxt);
        searchBtn = view.findViewById(R.id.searchBtn);

        previewImg = view.findViewById(R.id.previewImg);
        this.container = view.findViewById(R.id.container);

        presenter = new ShowPreviewFromThumb(previewImg, this.container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null){
            totalComments = savedInstanceState.getInt("totalComments");
        }

        product = getArguments().getParcelable("product");

        initViewData();
        setUpViewModel();
    }

    private void initViewData(){
        productMainInfoList.setLayoutManager(new LinearLayoutManager(getContext()));
        productMainInfoList.setAdapter(new ProductMainInfoListAdapter(product, onCommentPictureClickListener));
        productMainInfoList.addOnScrollListener(new ProductPageScrollListener() {
            @Override
            public void atTopOfPage() {
                scrollToTopBtn.hide();
                topBarContainer.transitionToStart();
            }

            @Override
            public void onOverScroll() {
                scrollToTopBtn.show();
                topBarContainer.transitionToEnd();
            }
        });

        titleTxt.setText(product.getTitle());

        findMoreBtn.setText("Xem thêm về \"" + product.getType() + "\"");


        scrollToTopBtn.hide();

        backBtn.setOnClickListener(backListener);
        findMoreBtn.setOnClickListener(findMoreClickListener);
        scrollToTopBtn.setOnClickListener(smoothScrollListener);
        searchBtn.setOnClickListener(onSearchClickListener);
        previewImg.setOnClickListener(onPreviewClickListener);
    }

    private void setUpViewModel(){
        viewModel = new ViewModelProvider(this).get(ProductFragmentViewModel.class);

        viewModel.observeProducts().observe(getViewLifecycleOwner(), new Observer<List<CommentBoxDTO>>() {
            @Override
            public void onChanged(List<CommentBoxDTO> comments) {
                if(comments == null){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(loadComments, 400);

                    return;
                }

                ((ProductMainInfoListAdapter)productMainInfoList.getAdapter()).setComments(comments, totalComments);
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("totalComments", totalComments);
    }
}
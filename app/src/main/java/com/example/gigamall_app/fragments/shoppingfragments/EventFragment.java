package com.example.gigamall_app.fragments.shoppingfragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.eventadapters.EventListAdapter;
import com.example.gigamall_app.fragments.post_related.CommentFragment;
import com.example.gigamall_app.entities.PostEntity;
import com.example.gigamall_app.interfaces.OnCommentClickListener;
import com.example.gigamall_app.interfaces.OnEndReachedListener;
import com.example.gigamall_app.services.PostService;
import com.example.gigamall_app.viewmodels.EventFragmentViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventFragment extends Fragment {
    private RecyclerView eventList;

    private EventFragmentViewModel viewModel;
    private final OnCommentClickListener onCommentClickListener = post -> {
        CommentFragment dialog = new CommentFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("post", post);

        dialog.setArguments(bundle);
        dialog.show(getParentFragmentManager(), "comment dialog");
    };

    private boolean isLastPage;
    private boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.event_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        eventList = view.findViewById(R.id.eventList);
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList.setAdapter(new EventListAdapter(null, onCommentClickListener, displayMetrics.widthPixels));

        eventList.addOnScrollListener(new OnEndReachedListener() {
            @Override
            public void onEndReached() {
                isLoading = true;

                Handler handler = new Handler(Looper.getMainLooper());

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(viewModel.getPosts() == null) {
                            return; // not initialised yet!
                        }

                        PostService.service.fetchRandomPosts(viewModel.listIds()).enqueue(new Callback<List<PostEntity>>() {
                            @Override
                            public void onResponse(Call<List<PostEntity>> call, Response<List<PostEntity>> response) {
                                ((EventListAdapter)eventList.getAdapter()).removeFooter();

                                ((EventListAdapter)eventList.getAdapter()).addPosts(response.body());

                                if(response.body().size() != 0){
                                    ((EventListAdapter)eventList.getAdapter()).addFooter();
                                }
                                else{
                                    isLastPage = true;
                                }

                                isLoading = false;
                            }

                            @Override
                            public void onFailure(Call<List<PostEntity>> call, Throwable t) {

                            }
                        });
                    }
                }, 250);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        viewModel = new ViewModelProvider(this).get(EventFragmentViewModel.class);

        if(viewModel.getPosts() == null) {
            PostService.service.fetchRandomPosts(viewModel.listIds()).enqueue(new Callback<List<PostEntity>>() {
                @Override
                public void onResponse(Call<List<PostEntity>> call, Response<List<PostEntity>> response) {
                    viewModel.setPosts(response.body());
                    ((EventListAdapter)eventList.getAdapter()).addPosts(response.body());
                    ((EventListAdapter)eventList.getAdapter()).addFooter();
                }

                @Override
                public void onFailure(Call<List<PostEntity>> call, Throwable t) {

                }
            });
        }
        else {
            ((EventListAdapter)eventList.getAdapter()).addPosts(viewModel.getPosts());

            if(!isLastPage) {
                ((EventListAdapter)eventList.getAdapter()).addFooter();
            }
        }

        viewModel.getTop6PostsHolder().observe(getViewLifecycleOwner(), new Observer<List<PostEntity>>() {
            @Override
            public void onChanged(List<PostEntity> posts) {
                if(posts == null){
                    PostService.service.fetchTop6().enqueue(new Callback<List<PostEntity>>() {
                        @Override
                        public void onResponse(Call<List<PostEntity>> call, Response<List<PostEntity>> response) {
                            viewModel.setTop6Posts(response.body());
                        }

                        @Override
                        public void onFailure(Call<List<PostEntity>> call, Throwable t) {

                        }
                    });
                }
                else{
                    ((EventListAdapter)eventList.getAdapter()).setTop6(posts);
                }
            }
        });
    }
}
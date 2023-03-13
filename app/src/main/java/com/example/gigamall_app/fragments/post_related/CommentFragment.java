package com.example.gigamall_app.fragments.post_related;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;
import com.example.gigamall_app.adapters.CommentListAdapter;
import com.example.gigamall_app.adapters.eventadapters.MediaAndPostInfoListAdapter;
import com.example.gigamall_app.dtos.CommentBoxDTO;
import com.example.gigamall_app.entities.PostEntity;
import com.example.gigamall_app.interfaces.ShowPreviewClickListener;
import com.example.gigamall_app.services.CommentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends DialogFragment {
    private RecyclerView commentList;
    private PostEntity post;

    private final ShowPreviewClickListener showPreviewClickListener = new ShowPreviewClickListener() {
        @Override
        public void onShowPreview(ImageView imageView, String url) {

        }
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
            dialog.getWindow().getAttributes().windowAnimations = R.style.leftAnim3;
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        post = getArguments().getParcelable("post");
        return inflater.inflate(R.layout.comment_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commentList = view.findViewById(R.id.commentList);
        commentList.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList.setAdapter(new ConcatAdapter(
                new MediaAndPostInfoListAdapter(post),
                new CommentListAdapter(showPreviewClickListener, post.getId())));

        CommentService.service.getComments(post.getId(), 0, false).enqueue(new Callback<List<CommentBoxDTO>>() {
            @Override
            public void onResponse(Call<List<CommentBoxDTO>> call, Response<List<CommentBoxDTO>> response) {
                ConcatAdapter adapter = (ConcatAdapter) commentList.getAdapter();

                int total = Integer.parseInt(response.headers().get("total"));

                CommentListAdapter commentListAdapter = (CommentListAdapter) adapter.getAdapters().get(1);
                commentListAdapter.setComments(response.body(), total);
            }

            @Override
            public void onFailure(Call<List<CommentBoxDTO>> call, Throwable t) {

            }
        });
    }
}
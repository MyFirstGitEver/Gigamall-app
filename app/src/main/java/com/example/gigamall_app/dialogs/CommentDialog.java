package com.example.gigamall_app.dialogs;

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
import com.example.gigamall_app.entities.PostEntity;
import com.example.gigamall_app.interfaces.ShowPreviewClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CommentDialog extends DialogFragment {
    private RecyclerView commentList;
    private PostEntity post;

    private final ShowPreviewClickListener showPreviewClickListener = new ShowPreviewClickListener() {
        @Override
        public void onShowPreview(ImageView imageView, String url) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        post = getArguments().getParcelable("post");
        return inflater.inflate(R.layout.comment_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        commentList = view.findViewById(R.id.commentList);
        commentList.setLayoutManager(new LinearLayoutManager(getContext()));
        commentList.setAdapter(new ConcatAdapter(
                new CommentListAdapter(showPreviewClickListener, post.getId()),
                new MediaAndPostInfoListAdapter(post)));
    }
}
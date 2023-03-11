package com.example.gigamall_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.interfaces.ShowPreviewClickListener;
import com.example.gigamall_app.tools.viewholders.NullViewHolder;
import com.example.gigamall_app.R;
import com.example.gigamall_app.tools.Tools;
import com.example.gigamall_app.dtos.CommentBoxDTO;
import com.example.gigamall_app.dtos.LoadMoreDTO;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.services.CommentService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductMainInfoListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ProductEntity product;
    private List<Object> comments;

    private static final int INFO = 0;
    private static final int LOADING = 1;
    private static final int COMMENT = 2;
    private static final int LOAD_MORE = 3;

    private final ShowPreviewClickListener showPreviewClickListener;

    public ProductMainInfoListAdapter(ProductEntity product, ShowPreviewClickListener showPreviewClickListener){
        this.product = product;
        this.showPreviewClickListener = showPreviewClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == INFO){
            return new ProductInfoViewHolder(inflater.inflate(R.layout.product_info_part, parent, false));
        }
        else if(viewType == LOADING){
            return new NullViewHolder(inflater.inflate(R.layout.a_loading, parent, false));
        }
        else if(viewType == COMMENT){
            return new CommentViewHolder(inflater.inflate(R.layout.a_comment, parent, false));
        }
        else{
            return new LoadMoreViewHolder(inflater.inflate(R.layout.a_load_more, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return INFO;
        }

        if(comments == null){
            return LOADING;
        }
        else if(comments.get(position - 1) instanceof CommentBoxDTO){
            return COMMENT;
        }
        else{
            return LOAD_MORE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        if(holder instanceof NullViewHolder){
            return;
        }
        else if(holder instanceof ProductInfoViewHolder){
            ProductInfoViewHolder productInfoViewHolder = (ProductInfoViewHolder) holder;
            productInfoViewHolder.desTxt.setText(product.getDescription());
            productInfoViewHolder.priceTxt.setText(product.getPrice() + "$");
            productInfoViewHolder.starTxt.setText("Đánh giá: " + product.getStar());

            Glide.with(holder.itemView.getContext()).load(product.getUrl()).into(productInfoViewHolder.productImg);
        }
        else if(holder instanceof CommentViewHolder){
            CommentBoxDTO dto = (CommentBoxDTO) comments.get(position - 1);
            CommentViewHolder commentHolder = (CommentViewHolder) holder;

            commentHolder.userNameTxt.setText(
                    dto.getUser().getUserDisplayName() + "(" + dto.getContentInStar() + " \uD83C\uDF1F)");
            commentHolder.contentTxt.setText(dto.getContentInText());
            commentHolder.timeTxt.setText(Tools.calculateTimeStamp(dto.getCommentDate()));
            commentHolder.container.setPadding(dto.getLevel() * 60, 0, 0, 0);

            Glide.with(commentHolder.itemView.getContext()).load(dto.getUser().getUrl()).into(commentHolder.userImg);
            commentHolder.attachedImg.setOnClickListener(v ->{
                String url = ((CommentBoxDTO)comments.get(holder.getBindingAdapterPosition() - 1)).getAttatchedUrl();
                showPreviewClickListener.onShowPreview((ImageView) v, url);
            });

            reuseUpdate(dto, commentHolder, context);

            // position is the position of recycler items
            // using index of comments array

            commentHolder.seeMoreBtn.setOnClickListener(v -> {
                onSeeMoreClick(
                        new LoadMoreDTO(dto.getId(), dto.getLevel() + 1, 0, dto.getChildCount()),
                        holder.getBindingAdapterPosition(),
                        false);

                dto.setId(-1);

                commentHolder.seeMoreBtn.setVisibility(View.GONE);
            });
        }
        else{
            // position is the position of recycler items
            // using index of comments array

            LoadMoreDTO dto = (LoadMoreDTO) comments.get(position - 1);
            LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
            loadMoreViewHolder.loadMoreBtn.setOnClickListener(v ->
                    onSeeMoreClick(dto,
                            holder.getBindingAdapterPosition(),
                            true));
            loadMoreViewHolder.loadMoreBtn.setPadding(dto.getLevel() * 60, 0, 0, 0);
            loadMoreViewHolder.loadMoreBtn.setText(context.getResources().getString(
                    R.string.see_more_text, dto.getChildCount()));
        }
    }

    @Override
    public int getItemCount() {
        if(comments == null){
            return 2;
        }
        else{
            return 1 + comments.size();
        }
    }

    public void setComments(List<CommentBoxDTO> comments, int total){
        this.comments = new ArrayList<>();

        this.comments.addAll(comments);
        this.comments.add(new LoadMoreDTO(product.getId(), 0, 1, total));

        notifyDataSetChanged();
    }

    private void onSeeMoreClick(LoadMoreDTO dto, int position, boolean removeSeeMoreButton){
        if(dto.getLevel() == 0){
            CommentService.service.getCommentsOfProductInPage(dto.getId(), dto.getPage()).enqueue(new Callback<List<CommentBoxDTO>>() {
                @Override
                public void onResponse(Call<List<CommentBoxDTO>> call, Response<List<CommentBoxDTO>> response) {
                    addLoadedComments(response, dto, position, removeSeeMoreButton);
                }

                @Override
                public void onFailure(Call<List<CommentBoxDTO>> call, Throwable t) {

                }
            });

            return;
        }

        CommentService.service.getReplies(dto.getId(), dto.getPage()).enqueue(new Callback<List<CommentBoxDTO>>() {
            @Override
            public void onResponse(Call<List<CommentBoxDTO>> call, Response<List<CommentBoxDTO>> response) {
                addLoadedComments(response, dto, position, removeSeeMoreButton);
            }

            @Override
            public void onFailure(Call<List<CommentBoxDTO>> call, Throwable t) {

            }
        });
    }

    private void reuseUpdate(CommentBoxDTO dto, CommentViewHolder commentHolder, Context context){
        if(dto.getChildCount() != 0){ // having some replies?
            commentHolder.seeMoreBtn.setVisibility(View.VISIBLE);
            commentHolder.seeMoreBtn.setText(context.getResources().getString(
                    R.string.see_more_text, dto.getChildCount()));

            if(dto.getId() == -1){ // used?
                commentHolder.seeMoreBtn.setVisibility(View.GONE);
            }
            else{
                commentHolder.seeMoreBtn.setVisibility(View.VISIBLE);
            }
        }
        else{
            commentHolder.seeMoreBtn.setVisibility(View.GONE);
        }

        if(dto.getAttatchedUrl() != null && !dto.getAttatchedUrl().equals("")){
            Glide.with(context).load(dto.getAttatchedUrl()).into(commentHolder.attachedImg);
            commentHolder.imgContainer.setVisibility(View.VISIBLE);
        }
        else{
            commentHolder.imgContainer.setVisibility(View.GONE);
        }
    }

    private void addLoadedComments(
            Response<List<CommentBoxDTO>> response,
            LoadMoreDTO dto,
            int position,
            boolean removeSeeMoreButton){

        if(removeSeeMoreButton){
            comments.remove(position - 1);
            notifyItemRemoved(position);
            position--;
        }

        if(response.body().size() == 0){
            return;
        }

        comments.add(position, new LoadMoreDTO(
                dto.getId(), dto.getLevel(), dto.getPage() + 1, dto.getChildCount()));
        comments.addAll(position, response.body());

        notifyItemRangeInserted(position + 1, response.body().size() + 1);
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg, attachedImg;
        CardView imgContainer;
        TextView userNameTxt, contentTxt, timeTxt;
        AppCompatButton seeMoreBtn;
        ConstraintLayout container;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.userImg);
            attachedImg = itemView.findViewById(R.id.attatchedImg);

            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);

            seeMoreBtn = itemView.findViewById(R.id.seeMoreBtn);

            container = itemView.findViewById(R.id.container);
            imgContainer = itemView.findViewById(R.id.imgContainer);
        }
    }

    public class ProductInfoViewHolder extends RecyclerView.ViewHolder {
        TextView desTxt, priceTxt, starTxt;
        ImageView productImg;

        public ProductInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            priceTxt = itemView.findViewById(R.id.priceTxt);
            desTxt = itemView.findViewById(R.id.desTxt);
            starTxt = itemView.findViewById(R.id.starCountTxt);
            productImg = itemView.findViewById(R.id.productImg);
        }
    }

    public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        AppCompatButton loadMoreBtn;

        public LoadMoreViewHolder(@NonNull View itemView) {
            super(itemView);

            loadMoreBtn = itemView.findViewById(R.id.loadMoreBtn);
        }
    }
}

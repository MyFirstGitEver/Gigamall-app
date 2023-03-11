package com.example.gigamall_app.tools.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gigamall_app.R;

public class DetailsViewHolder extends RecyclerView.ViewHolder {
    public ConstraintLayout container;
    public ImageView productImg;
    public TextView titleTxt, priceTxt;
    public AppCompatButton seeMoreBtn;

    public DetailsViewHolder(@NonNull View itemView) {
        super(itemView);

        container = itemView.findViewById(R.id.container);
        productImg = itemView.findViewById(R.id.productImg);

        titleTxt = itemView.findViewById(R.id.desTxt);
        priceTxt = itemView.findViewById(R.id.priceTxt);

        seeMoreBtn = itemView.findViewById(R.id.seeMoreBtn);
    }
}
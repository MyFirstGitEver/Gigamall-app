package com.example.gigamall_app.adapters.arrayadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.gigamall_app.R;
import com.example.gigamall_app.entities.ProductEntity;
import com.example.gigamall_app.services.ProductService;
import com.example.gigamall_app.tools.SearchTextManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SuggestionAdapter extends ArrayAdapter<Object> {
    private final List<Object> hints;

    public SuggestionAdapter(@NonNull Context context, int resource, @NonNull List<Object> hints) {
        super(context, resource, hints);

        this.hints = hints;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                if(charSequence == null){
                    FilterResults results = new FilterResults();

                    results.values = hints;
                    results.count = hints.size();

                    return results;
                }

                FilterResults results = new FilterResults();

                List<Object> objs = new ArrayList<>();

                List<ProductEntity> products = new ArrayList<>();
                try {
                    products = ProductService.service.search(charSequence.toString(), 0).execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                objs.addAll(products);
                objs.addAll(SearchTextManager.getHints(getContext()));

                results.values = objs;
                results.count = objs.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();

                addAll((List<Object>)filterResults.values);
            }
        };
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.a_suggestion, parent, false);

        ImageView productImg = view.findViewById(R.id.productImg);
        TextView titleTxt = view.findViewById(R.id.titleTxt);

        Object obj = getItem(position);

        if(obj instanceof  String){
            productImg.setImageResource(R.drawable.ic_baseline_history_24);
            productImg.getLayoutParams().width = 40;
            productImg.getLayoutParams().height = 40;
            productImg.requestLayout();

            titleTxt.setText((String)obj);

            return view;
        }

        ProductEntity product = (ProductEntity) obj;

        Glide.with(parent.getContext()).load(product.getUrl()).into(productImg);
        titleTxt.setText(product.getTitle());

        return view;
    }
}
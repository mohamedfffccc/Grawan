package com.openshop.gazapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.category.CategoryData;
import com.openshop.gazapp.view.activity.home.ProductsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductViewHolder> {



    private Context context;
    private List<CategoryData> articleslist = new ArrayList<>();


    public CategoryAdapter(List<CategoryData> articleslist, Context context) {
        this.articleslist = articleslist;
        this.context = context;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Glide.with(context).load(articleslist.get(position).getImage()).into(holder.categoryIvImage);
        holder.categoryTvName.setText(articleslist.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent products_i = new Intent(context, ProductsActivity.class);
                products_i.putExtra("category" , articleslist.get(position));
                context.startActivity(products_i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return articleslist.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.category_iv_image)
        ImageView categoryIvImage;
        @BindView(R.id.category_tv_name)
        TextView categoryTvName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

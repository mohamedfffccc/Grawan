package com.openshop.gazapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.local.room.OrderItem;
import com.openshop.gazapp.data.local.room.RoomDao;
import com.openshop.gazapp.data.model.products.ProductData;
import com.openshop.gazapp.view.activity.home.ProductDetailsActivity;
import com.openshop.gazapp.view.activity.home.ProductsActivity;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openshop.gazapp.data.local.room.RoomManger.getInistance;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {



    private Context context;
    private List<ProductData> articleslist;
    private RoomDao roomDao;
    ProductsActivity activity;
    private int i;
    private OrderItem item;


    public ProductAdapter(List<ProductData> articleslist, Context context, ProductsActivity activity) {
        this.articleslist = articleslist;
        this.activity = activity;
        this.context = context;
        roomDao = getInistance(context).roomDao();


    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Glide.with(context).load(articleslist.get(position).getImage()).into(holder.productIvImage);
        holder.productTvName.setText(articleslist.get(position).getName());
        holder.productTvPrice.setText(articleslist.get(position).getPrice() +  " ريال لكل  " + articleslist.get(position).getUnit().getName());
        holder.productIvImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details_i = new Intent(context, ProductDetailsActivity.class);
                details_i.putExtra("product", articleslist.get(position));
                context.startActivity(details_i);
            }
        });

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             holder.addToCart.setVisibility(View.GONE);
                //TODO add item to room
                AddToCart(position);
//             holder.addOrRemove.setVisibility(View.VISIBLE);
            }
        });

    }

    //TODO add to cart
    private void AddToCart(int i) {

        roomDao = getInistance(context).roomDao();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {

                item = new OrderItem(articleslist.get(i).getId(), articleslist.get(i).getCategoryId(), articleslist.get(i).getName()
                        , articleslist.get(i).getImage(), Double.parseDouble(articleslist.get(i).getPrice()), 1);
                roomDao.addItem(item);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
                    }

                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return articleslist.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.product_iv_image)
        ImageView productIvImage;
        @BindView(R.id.product_tv_name)
        TextView productTvName;

        @BindView(R.id.product_add_to_cart)
        Button addToCart;
        @BindView(R.id.product_tv_price)
        TextView productTvPrice;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

package com.openshop.gazapp.adapter;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.local.room.OrderItem;
import com.openshop.gazapp.data.local.room.RoomDao;
import com.openshop.gazapp.view.activity.auth.AuthActivity;
import com.openshop.gazapp.view.activity.home.CartActivity;
import com.openshop.gazapp.view.activity.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.clean;
import static com.openshop.gazapp.data.local.room.RoomManger.getInistance;

/**
 * Created by medo on 13/11/2016.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CategoriesViewHolder> {



    private Context context;
    private CartActivity activity;
    private List<OrderItem> orderlist = new ArrayList<>();
    private Typeface type;
    RoomDao roomDao;
    private Dialog dialog;

    public CartAdapter(Context context, CartActivity activity, List<OrderItem> orderlist) {
        this.context = context;
        this.activity = activity;
        this.orderlist = orderlist;
        roomDao = getInistance(context).roomDao();
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        OrderItem orderitem = orderlist.get(position);
        Glide.with(context).load(orderitem.getImageItem()).into(holder.itemCartImgItemLogo);
        holder.itemCartTvNameItme.setText(orderitem.getNameItem());
        holder.itemCartTvPriceItem.setText( String.valueOf(orderitem.getPrice())+ "ريال");
        holder.itemCartQuantityItem.setText(String.valueOf(orderitem.getQuantity()));
        holder.itemCartBtnRemoveItemFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO delete
                showDialoge(position);

            }
        });
        holder.itemCartBtnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add item
                 addItem(position , holder);

            }
        });
        holder.itemCartBtnRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO reove item
                 removeItem(position ,  holder);

            }
        });

    }
    //TODO delete item from cart
    public void deleteItem(int index)
    {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                roomDao.removeItem(orderlist.get(index));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        orderlist.remove(index);
                        notifyDataSetChanged();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.updateUi(orderlist);
                            }
                        });
                    }
                });
            }
        });
    }
    //TODO add item
    public void addItem(int index , @NonNull CategoriesViewHolder holder)
    {
        int i = orderlist.get(index).getQuantity() + 1;
        holder.itemCartQuantityItem.setText(i + "");
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                orderlist.get(index).setQuantity(i);
                roomDao.update(orderlist.get(index));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.updateUi(orderlist);
                        notifyDataSetChanged();

                    }
                });

            }
        });
    }
    //TODO reove item
    public void removeItem(int index , @NonNull CategoriesViewHolder holder)
    {
        int i = orderlist.get(index).getQuantity() ;
        if (i>1) {
            holder.itemCartQuantityItem.setText(String.valueOf(i-1));
        }

        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                orderlist.get(index).setQuantity(i-1);


                roomDao.update(orderlist.get(index));

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.updateUi(orderlist);
                        notifyDataSetChanged();

                    }
                });
//
            }
        });
    }


    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cart_item_iv_image)
        ImageView itemCartImgItemLogo;
        @BindView(R.id.cart_item_tv_name)
        TextView itemCartTvNameItme;
        @BindView(R.id.cart_item_tv_price)
        TextView itemCartTvPriceItem;
        @BindView(R.id.cart_item__item_count)
        TextView itemCartQuantityItem;
        @BindView(R.id.cart_item_btn_add_item)
        ImageView itemCartBtnAddItem;

        @BindView(R.id.cart_item_btn_remove_item)
        ImageView itemCartBtnRemoveItem;
        @BindView(R.id.cart_item_btn_delete)
        ImageView itemCartBtnRemoveItemFromCart;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
    //TODO delete
    private void showDialoge(int index) {
        dialog = new Dialog(context, R.style.customDialogTheme);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.order_delete_dialog, null);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        TextView yes = (TextView) v.findViewById(R.id.delete_dialog_btn_yes);
        TextView no = (TextView) v.findViewById(R.id.delete_dialog_btn_no);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(index);
                dialog.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




        dialog.show();

    }

}

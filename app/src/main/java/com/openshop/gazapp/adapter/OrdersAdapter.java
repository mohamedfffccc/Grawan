package com.openshop.gazapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.orders.OrderData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ProductViewHolder> {



    private Context context;
    private List<OrderData> articleslist = new ArrayList<>();


    public OrdersAdapter(List<OrderData> articleslist, Context context) {
        this.articleslist = articleslist;
        this.context = context;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.orderitemTvNumber.setText(context.getString(R.string.order_nunber) + ":" + articleslist.get(position).getId());
        for (int i = 0; i < articleslist.get(position).getActions().size(); i++) {
            if (articleslist.get(position).getActions().get(i).getStatus().getId() == 3) {
                holder.orderitemTvAcceptDate.setText(articleslist.get(position).getActions().get(i).getCreatedAt());
                holder.orderItemLineAccepted.setBackgroundColor(context.getColor(R.color.darkblue));
            }
            if (articleslist.get(position).getActions().get(i).getStatus().getId() == 4) {
                //   holder.orderitemTvAcceptDate.setText(articleslist.get(position).getActions().get(i).getCreatedAt());
                holder.orderitemTvTrackDate.setText(articleslist.get(position).getActions().get(i).getCreatedAt());
                holder.orderItemIvTracked.setImageResource(R.drawable.b_tracking);
                holder.orderItemLineTracked.setBackgroundColor(context.getColor(R.color.darkblue));

            }
            if (articleslist.get(position).getActions().get(i).getStatus().getId() == 5) {
                holder.orderitemTvDeliverDate.setText(articleslist.get(position).getActions().get(i).getCreatedAt());
                holder.orderItemIvDelivered.setImageResource(R.drawable.b_delivered);
            }
            if (articleslist.get(position).getActions().get(i).getStatus().getId() == 2) {
                holder.orderItemTvAcepted.setText(context.getString(R.string.order_rejected) );
                holder.orderitemTvAcceptDate.setText(articleslist.get(position).getActions().get(i).getCreatedAt());
                holder.orderItemIvAcepted.setImageResource(R.drawable.ic_error_black_24dp);
                holder.orderItemLinTracked.setVisibility(View.GONE);
                holder.orderItemLinDelivered.setVisibility(View.GONE);
                holder.orderItemLineAccepted.setVisibility(View.GONE);
                holder.orderItemLineTracked.setVisibility(View.GONE);
                holder.orderitemTvTrackDate.setVisibility(View.GONE);
                holder.orderitemTvDeliverDate.setVisibility(View.GONE);

            }

        }


    }

    @Override
    public int getItemCount() {
        return articleslist.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.orderitem_tv_number)
        TextView orderitemTvNumber;
        @BindView(R.id.order_item_iv_acepted)
        ImageView orderItemIvAcepted;
        @BindView(R.id.order_item_tv_acepted)
        TextView orderItemTvAcepted;
        @BindView(R.id.order_item_lin_acepted)
        LinearLayout orderItemLinAcepted;
        @BindView(R.id.orderitem_tv_accept_date)
        TextView orderitemTvAcceptDate;
        @BindView(R.id.order_item_iv_tracked)
        ImageView orderItemIvTracked;
        @BindView(R.id.order_item_tv_tracked)
        TextView orderItemTvTracked;
        @BindView(R.id.order_item_lin_tracked)
        LinearLayout orderItemLinTracked;
        @BindView(R.id.orderitem_tv_track_date)
        TextView orderitemTvTrackDate;
        @BindView(R.id.order_item_iv_delivered)
        ImageView orderItemIvDelivered;
        @BindView(R.id.order_item_tv_delivered)
        TextView orderItemTvDelivered;
        @BindView(R.id.order_item_lin_delivered)
        LinearLayout orderItemLinDelivered;
        @BindView(R.id.orderitem_tv_deliver_date)
        TextView orderitemTvDeliverDate;
        @BindView(R.id.order_item_line_accepted)
        View orderItemLineAccepted;
        @BindView(R.id.order_item_line_tracked)
        View orderItemLineTracked;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

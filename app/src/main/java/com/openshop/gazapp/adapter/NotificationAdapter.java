package com.openshop.gazapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.NotificationModel;
import com.openshop.gazapp.data.model.notification.NotificationData;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ProductViewHolder> {


    private Context context;
    private List<NotificationData> articleslist = new ArrayList<>();
    private Date date;


    public NotificationAdapter(List<NotificationData> articleslist, Context context) {
        this.articleslist = articleslist;
        this.context = context;

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

     holder.notificationItemTvTitle.setText(articleslist.get(position).getContent());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            date = sdf.parse(articleslist.get(position).getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime();
        String timeago = prettyTime.format(date);
        holder.notificationItemTvPeriod.setText(timeago);
        switch (articleslist.get(position).getType())
        {
            case 5 :
                holder.notificationItemIvType.setImageResource(R.drawable.b_delivered);
                break;
                //delivered
            case 4:
                holder.notificationItemIvType.setImageResource(R.drawable.b_tracking);

                break;
                //with delivery
            case 3 :
                holder.notificationItemIvType.setImageResource(R.drawable.tick);

                break;
                //accepted
            case 2 :
                holder.notificationItemTvTitle.setText(articleslist.get(position).getTitle()+ "       رقم  " + articleslist.get(position).getOrder().getId());
                holder.notificationItemIvType.setImageResource(R.drawable.ic_error_black_24dp);

                break;
            case 1 :
                holder.notificationItemIvType.setImageResource(R.drawable.bell);
                break;
                //rejected

        }




    }

    @Override
    public int getItemCount() {
        return articleslist.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_item_iv_type)
        ImageView notificationItemIvType;
        @BindView(R.id.notification_item_tv_title)
        TextView notificationItemTvTitle;
        @BindView(R.id.notification_item_tv_period)
        TextView notificationItemTvPeriod;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

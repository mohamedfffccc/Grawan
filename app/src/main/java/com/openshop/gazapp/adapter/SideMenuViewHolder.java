package com.openshop.gazapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;


/**
 * Created by mdev3 on 9/6/17.
 */

public class SideMenuViewHolder extends  RecyclerView.ViewHolder {
    public final TextView titleTV;
    public final ImageView iconMV;
    public final RelativeLayout itemLayout;
// public final CardView cardView;

    public SideMenuViewHolder(View view) {
        super(view);
        titleTV = (TextView) view.findViewById(R.id.title);
        iconMV = (ImageView) view.findViewById(R.id.icon);

        itemLayout = (RelativeLayout) view.findViewById(R.id.ItemLayout);
        //  cardView = (CardView)view.findViewById(R.id.card_view);
    }
}
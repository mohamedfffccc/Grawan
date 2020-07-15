package com.openshop.gazapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.advertisement.Datum;


import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {
    Context context;
    public ArrayList<Datum> data;

    LayoutInflater mLayoutInflater;

    public SlideAdapter(Context context , ArrayList<Datum> data) {
        this.context = context;
        this.data=data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {

            View itemView = mLayoutInflater.inflate(R.layout.item_slider_offer, container, false);

            ImageView imageView = itemView.findViewById(R.id.iv_slider_image);
            TextView tv_title = itemView.findViewById(R.id.tv_slider_title);
            TextView tv_desc = itemView.findViewById(R.id.tv_slider_desc);

            Glide.with(context).load(data.get(position).getImage())
                    .into(imageView);
            tv_title.setText(data.get(position).getTitle());
            tv_desc.setText(data.get(position).getDescription());

            container.addView(itemView);

            return itemView;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}

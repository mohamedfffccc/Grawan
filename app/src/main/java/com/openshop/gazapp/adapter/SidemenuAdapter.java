package com.openshop.gazapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.SideMenuModel;

import java.util.List;


/**
 * Created by mdev3 on 9/6/17.
 */

public class SidemenuAdapter extends RecyclerView.Adapter<SideMenuViewHolder> {
    private List<SideMenuModel> mValues;
    private Context context;
    private OnSideMenuItemClickListener onSideMenuItemClickListener;

    public SidemenuAdapter(Context context, List<SideMenuModel> items, OnSideMenuItemClickListener onSideMenuItemClickListener) {
        this.context = context;
        mValues = items;
        this.onSideMenuItemClickListener = onSideMenuItemClickListener;
    }

    @Override
    public SideMenuViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.side_menu_item, parent, false);

        return new SideMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SideMenuViewHolder holder, final int position) {
        holder.titleTV.setText(mValues.get(position).getTitle());
        holder.iconMV.setImageResource(mValues.get(position).getImage());
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSideMenuItemClickListener.onItemSelectListener(position, null);

                for(int i = 0; i< mValues.size() ; i++){

                    if(i ==position){
                        SideMenuModel sideMenuModel = mValues.get(i);
                        sideMenuModel.setSelected(true);
                        mValues.set(i, sideMenuModel);
                    }else{
                        SideMenuModel sideMenuModel = mValues.get(i);
                        sideMenuModel.setSelected(false);
                        mValues.set(i, sideMenuModel);
                    }
                }
                notifyDataSetChanged();
            }
        });
//        if(mValues.get(position).isSelected()){
//            holder.itemLayout.setBackgroundResource(R.drawable.seelcted_bg);
//        }else{
//            holder.itemLayout.setBackgroundResource(R.drawable.unselected_bg);
//        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface OnSideMenuItemClickListener {
        void onItemSelectListener(int index, Object obj);


    }
}
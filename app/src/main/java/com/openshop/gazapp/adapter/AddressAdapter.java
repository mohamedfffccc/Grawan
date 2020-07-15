package com.openshop.gazapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.addresses.AddressData;
import com.openshop.gazapp.data.model.login.Address;
import com.openshop.gazapp.view.activity.home.AddressActivity;

import java.util.ArrayList;
import java.util.List;

import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;

public class AddressAdapter extends BaseAdapter {
    List<AddressData> data;
    Context context;
    AddressActivity activity;
    String [] address_split;

    public AddressAdapter(List<AddressData> data, Context context, AddressActivity activity) {
        this.data = data;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.address_item , null);
        TextView name = (TextView) view.findViewById(R.id.address_item_tv_name);
        TextView address = (TextView) view.findViewById(R.id.address_item_tv_address);

        name.setText(loadUserData(activity , USER_DATA).getName());
//        address_split = data.get(position).getAddress().split("،");
//        city.setText(address_split[3] + "," + address_split[4]);


        address.setText(data.get(position).getAddress());

        return view;
    }
}

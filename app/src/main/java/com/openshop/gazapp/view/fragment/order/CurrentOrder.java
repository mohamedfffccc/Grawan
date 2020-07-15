package com.openshop.gazapp.view.fragment.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.OrdersAdapter;
import com.openshop.gazapp.data.model.addresses.Addresses;
import com.openshop.gazapp.data.model.orders.OrderData;
import com.openshop.gazapp.data.model.orders.Orders;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;


public class CurrentOrder extends Fragment {


    @BindView(R.id.activity_orders_rv_order_list)
    RecyclerView activityOrdersRvOrderList;
    public String state;
    OrdersAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<OrderData> list_order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, root);
        //TODO set rv
        linearLayoutManager=new LinearLayoutManager(getActivity());
        activityOrdersRvOrderList.setLayoutManager(linearLayoutManager);
        list_order = new ArrayList<>();
       getCurrent();

        return root;

    }

    private void getCurrent() {
        showProgressDialog(getActivity() , getActivity().getString(R.string.wait));
        getClient().getCurrentOrfders(LoadData(getActivity() , API_TOKEN)).enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                dismissProgressDialog();
                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus()==1)
                    {
                        list_order.addAll(response.body().getData());
                        adapter = new OrdersAdapter(list_order,getActivity());
                        activityOrdersRvOrderList.setAdapter(adapter);
                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {

            }
        });
    }


}

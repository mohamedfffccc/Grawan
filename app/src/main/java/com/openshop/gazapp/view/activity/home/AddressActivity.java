package com.openshop.gazapp.view.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.AddressAdapter;
import com.openshop.gazapp.adapter.ProductAdapter;
import com.openshop.gazapp.data.model.addresses.AddressData;
import com.openshop.gazapp.data.model.addresses.Addresses;
import com.openshop.gazapp.data.model.login.Address;
import com.openshop.gazapp.data.model.login.City;
import com.openshop.gazapp.data.model.login.Region;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
import static com.openshop.gazapp.view.activity.home.LocationActivity.ADDRESS;
import static com.openshop.gazapp.view.activity.home.LocationActivity.CITY;
import static com.openshop.gazapp.view.activity.home.LocationActivity.REGION;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.activity_address_btn_back)
    ImageView activityAddressBtnBack;
    @BindView(R.id.activity_address_rel_title)
    RelativeLayout activityAddressRelTitle;
    @BindView(R.id.activity_address_lv_addresses)
    ListView activityAddressLvAddresses;
    @BindView(R.id.activity_address_btn_confirm_order)
    Button activityAddressBtnConfirmOrder;
    @BindView(R.id.activity_address_btn_add_new_address)
    Button activityAddressBtnAddNewAddress;
//    String my_address;
     List<AddressData> address_list;
     AddressAdapter adapter;
    private boolean location=false;
    public int address_id;
    boolean clicked = false;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        //TODO get addresses
        getAddresses();
        activityAddressLvAddresses.setSelector(R.drawable.list_item_selector);


        activityAddressLvAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                address_id = address_list.get(position).getId();
                clicked=true;

            }
        });


    }

    @OnClick({R.id.activity_address_btn_back, R.id.activity_address_btn_confirm_order, R.id.activity_address_btn_add_new_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_address_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_address_btn_confirm_order:
                //TODO payment methods activity
                if (clicked==true)
                {
                    Intent payment_i = new Intent(this , PaymentActivity.class);
                    payment_i.putExtra("address_id" , String.valueOf(address_id));
                    Log.d("add" , String.valueOf(address_id));
                    startActivity(payment_i);


                }
                else if (clicked==false)
                {
                    Toast.makeText(this, "برجاء اختيار عنوان", Toast.LENGTH_SHORT).show();
                }
                
               

                break;
            case R.id.activity_address_btn_add_new_address:
                location=true;
                startActivity(new Intent(this , LocationActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (location==true) {
//            AddressData address = new AddressData(ADDRESS);
//            address_list.add(address);
           getAddresses();
            adapter.notifyDataSetChanged();
        }

    }
    //TODO get user addresses
    public void getAddresses()
    {
        address_list=new ArrayList<>();
        String token = LoadData(this , API_TOKEN);
        Log.d("TOKEN" , token);
//        showProgressDialog(AddressActivity.this , AddressActivity.this.getString(R.string.wait));
        getClient().getAddresses(token).enqueue(new Callback<Addresses>() {
            @Override
            public void onResponse(Call<Addresses> call, Response<Addresses> response) {
//                dismissProgressDialog();
                Toast.makeText(AddressActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus()==1) {
                        address_list.addAll(response.body().getData());
                        adapter = new AddressAdapter(address_list, AddressActivity.this, AddressActivity.this);
                        activityAddressLvAddresses.setAdapter(adapter);
                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Addresses> call, Throwable t) {
                Toast.makeText(AddressActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}

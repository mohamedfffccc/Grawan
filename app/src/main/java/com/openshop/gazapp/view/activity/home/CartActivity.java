package com.openshop.gazapp.view.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.openshop.gazapp.R;
import com.openshop.gazapp.adapter.CartAdapter;
import com.openshop.gazapp.data.local.room.OrderItem;
import com.openshop.gazapp.data.local.room.RoomDao;
import com.openshop.gazapp.data.model.DeliveryCost;
import com.openshop.gazapp.data.model.delivery.Delivery;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.room.RoomManger.getInistance;

public class CartActivity extends AppCompatActivity {

    @BindView(R.id.activity_cart_rv_items)
    RecyclerView activityCartRvItems;
    List<OrderItem> data;
    CartAdapter adapter;
    RoomDao roomDao;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.activity_cart_btn_back)
    ImageView activityCartBtnBack;
    @BindView(R.id.activity_cart_rel_title)
    RelativeLayout activityCartRelTitle;
    @BindView(R.id.activity_cart_tv_order_total)
    TextView activityCartTvOrderTotal;
    @BindView(R.id.activity_cart_tv_order_price)
    TextView activityCartTvOrderPrice;
    @BindView(R.id.activity_cart_tv_delivery_cost)
    TextView activityCartTvDeliveryCost;
    @BindView(R.id.activity_cart_tv_total)
    TextView activityCartTvTotal;
    @BindView(R.id.activity_cart_lin_total)
    LinearLayout activityCartLinTotal;
    @BindView(R.id.activity_cart_btn_confirm)
    Button activityCartBtnConfirm;
    @BindView(R.id.activity_cart_lin_cost)
    LinearLayout activityCartLinCost;
    private double total_order, total;
    private Dialog dialog;
    Double delivery_cost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        roomDao = getInistance(this).roomDao();
        linearLayoutManager = new LinearLayoutManager(this);
        activityCartRvItems.setLayoutManager(linearLayoutManager);
        getCost();


    }

    private void getData() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                data = roomDao.getAll();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CartAdapter(CartActivity.this, CartActivity.this, data);
                        activityCartRvItems.setAdapter(adapter);
                        for (int i = 0; i < data.size(); i++) {
                            total_order += (data.get(i).getQuantity() * data.get(i).getPrice());
                            Log.d("faasasf", String.valueOf(total_order));

                        }
                        if (data.size() == 0) {
                            showDialoge();
                            activityCartLinCost.setVisibility(View.GONE);
                            activityCartLinTotal.setVisibility(View.GONE);
                            activityCartTvOrderTotal.setVisibility(View.GONE);

                        }
                        activityCartTvOrderPrice.setText(total_order + " ريال ");
                        total = total_order + delivery_cost;
                        activityCartTvTotal.setText(total + " ريال ");


                    }
                });
            }
        });
    }

    //TODO Updete UI
    public void updateUi(List<OrderItem> data) {
        total_order = 0;
        for (int i = 0; i < data.size(); i++) {
            total_order += (data.get(i).getQuantity() * data.get(i).getPrice());
        }
        activityCartTvOrderPrice.setText(total_order + " ريال ");
        total = total_order + delivery_cost;
        activityCartTvTotal.setText(total + " ريال ");
    }


    @OnClick({R.id.activity_cart_btn_back, R.id.activity_cart_btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_cart_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_cart_btn_confirm:
                startActivity(new Intent(this, AddressActivity.class));
                break;
        }
    }

    private void showDialoge() {
        dialog = new Dialog(this, R.style.customDialogTheme);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_no_cart_data, null);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        Button add = (Button) v.findViewById(R.id.no_data_dialog_btn_add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
            }
        });


        dialog.show();

    }
    //TODO get delivery
    public void  getCost()
    {
        showProgressDialog(CartActivity.this , this.getString(R.string.wait));
        getClient().getDeliveryCost().enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                dismissProgressDialog();
                Toast.makeText(CartActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus()==1)
                    {
                        delivery_cost=Double.parseDouble(response.body().getData());
                        activityCartTvDeliveryCost.setText(response.body().getData() + " ريال ");
                        getData();
                    }

                }
                catch (Exception e)
                {

                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {

            }
        });
    }

}

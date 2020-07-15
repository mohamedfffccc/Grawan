package com.openshop.gazapp.view.activity.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.local.room.OrderItem;
import com.openshop.gazapp.data.local.room.RoomDao;
import com.openshop.gazapp.data.model.clientorder.ClientOrder;
import com.openshop.gazapp.data.model.login.Login;
import com.openshop.gazapp.data.model.products.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;
import static com.openshop.gazapp.data.local.room.RoomManger.getInistance;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.activity_payment_btn_back)
    ImageView activityPaymentBtnBack;
    @BindView(R.id.activity_payment_rel_title)
    RelativeLayout activityPaymentRelTitle;
    @BindView(R.id.activity_payment_tv_message)
    TextView activityPaymentTvMessage;
    @BindView(R.id.activity_payment_lin_methods)
    RelativeLayout activityPaymentLinMethods;
    @BindView(R.id.activity_payment_btn_visa)
    RadioButton activityPaymentBtnVisa;
    @BindView(R.id.activity_payment_btn_cash)
    RadioButton activityPaymentBtnCash;
    @BindView(R.id.activity_payment_rel_methods)
    RelativeLayout activityPaymentRelMethods;
    @BindView(R.id.activity_payment_ed_username)
    TextInputEditText activityPaymentEdUsername;
    @BindView(R.id.activity_payment_txt_username)
    TextInputLayout activityPaymentTxtUsername;
    @BindView(R.id.activity_payment_ed_card_number)
    TextInputEditText activityPaymentEdCardNumber;
    @BindView(R.id.activity_payment_txt_card_number)
    TextInputLayout activityPaymentTxtCardNumber;
    @BindView(R.id.activity_payment_ed_date)
    TextInputEditText activityPaymentEdDate;
    @BindView(R.id.activity_payment_txt_date)
    TextInputLayout activityPaymentTxtDate;
    @BindView(R.id.activity_payment_ed_cvv)
    TextInputEditText activityPaymentEdCvv;
    @BindView(R.id.activity_payment_txt_cvv)
    TextInputLayout activityPaymentTxtCvv;
    @BindView(R.id.activity_payment_btn_confirm)
    Button activityPaymentBtnConfirm;
    @BindView(R.id.activity_payment_rg_methods)
    RadioGroup activityPaymentRgMethods;
    int payment_method_id;
    @BindView(R.id.activity_payment_rel_card_info)
    RelativeLayout activityPaymentRelCardInfo;
    private RoomDao roomDao;
    private String address_id;
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<String> quantities = new ArrayList<>();
    HashMap<String,String> Map= new HashMap<String,String>();

    private List<OrderItem> data;
    private Dialog dialog;
    int order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        address_id = getIntent().getExtras().getString("address_id");
        Log.d("address" , address_id+"");
        activityPaymentRgMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.activity_payment_btn_visa:
                        payment_method_id = 2;
                        activityPaymentRelCardInfo.setVisibility(View.VISIBLE);
                        break;
                    case R.id.activity_payment_btn_cash:
                        payment_method_id = 1;
                        activityPaymentRelCardInfo.setVisibility(View.GONE);
                }
            }
        });
        activityPaymentEdDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (s.length() % 3) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if ('/' == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                if (s.length() > 0 && (s.length() % 3) == 0) {
                    char c = s.charAt(s.length() - 1);
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf("/")).length <= 2) {
                        s.insert(s.length() - 1, String.valueOf("/"));
                    }
                }

            }
        });

    }
    public void getDataFromRoom() {

        roomDao = getInistance(this).roomDao();

        ids = new ArrayList<>();
        quantities = new ArrayList<>();
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                data = roomDao.getAll();
                for (int i = 0; i < data.size(); i++) {
                    ids.add(data.get(i).item_id);
                    quantities.add(String.valueOf(data.get(i).quantity));
                    Map.put("api_token",LoadData(PaymentActivity.this , API_TOKEN));
                    Map.put("payment_way_id",String.valueOf(payment_method_id));
                    Map.put("address_id",String.valueOf(address_id));
                    Map.put("more_details","jhjj");

                    Map.put("products["+i+"][product_id]",String.valueOf(data.get(i).item_id));
                    Map.put("products["+i+"][quantity]",String.valueOf(data.get(i).quantity));

                }

            }
        });

    }

    //Check Payment
    public void CheckPayment(){
        if (activityPaymentBtnCash.isChecked()){
            payment_method_id=1;
        }else if (activityPaymentBtnVisa.isChecked()){
            payment_method_id=2;
        }

    }

    //Confirm Order
    public void confirmOrder() {

        getDataFromRoom();
        CheckPayment();


        showProgressDialog(PaymentActivity.this, getString(R.string.wait));
        getClient().confirmOrder(Map ).enqueue(new Callback<ClientOrder>() {
            @Override
            public void onResponse(Call<ClientOrder> call, Response<ClientOrder> response) {
                dismissProgressDialog();
                Toast.makeText(PaymentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                try {
                    if (response.body().getStatus() == 1) {
                        order_id=response.body().getData().getId();
                        //TODO done dialoge

                        showDialoge();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                startActivity(new Intent(PaymentActivity.this , HomeActivity.class));


                            }
                        },3000);


                        Executors.newSingleThreadExecutor().execute(new Runnable() {
                            @Override
                            public void run()
                            {
                                roomDao.delAll();
                            }
                        });

                    } else if (response.body().getStatus() == 0) {

                        Toast.makeText(PaymentActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<ClientOrder> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(PaymentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showDialoge() {
        dialog = new Dialog(this, R.style.customDialogTheme);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialoge_done, null);
        dialog.setContentView(v);
        TextView tv_message = (TextView) v.findViewById(R.id.dialoge_tv_done);
        Button done = (Button) v.findViewById(R.id.dialoge_btn_done);
        tv_message.setText(this.getString(R.string.your_order_sent) + order_id + this.getString(R.string.successfully));
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentActivity.this , HomeActivity.class));
            }
        });




        dialog.show();

    }


    @OnClick({R.id.activity_payment_btn_back, R.id.activity_payment_btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_payment_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_payment_btn_confirm:
                confirmOrder();
                break;
        }
    }


}

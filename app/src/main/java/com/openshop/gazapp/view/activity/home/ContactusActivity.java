package com.openshop.gazapp.view.activity.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.login.Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;

public class ContactusActivity extends AppCompatActivity {

    @BindView(R.id.activity_contactus_btn_back)
    ImageView activityContactusBtnBack;
    @BindView(R.id.activity_contactus_rel_title)
    RelativeLayout activityContactusRelTitle;
    @BindView(R.id.activity_contactus_iv_logo)
    ImageView activityContactusIvLogo;
    @BindView(R.id.activity_contactus_ed_username)
    TextInputEditText activityContactusEdUsername;
    @BindView(R.id.activity_contactus_txt_username)
    TextInputLayout activityContactusTxtUsername;
    @BindView(R.id.activity_contactus_ed_phone)
    TextInputEditText activityContactusEdPhone;
    @BindView(R.id.activity_contactus_txt_phone)
    TextInputLayout activityContactusTxtPhone;
    @BindView(R.id.activity_contactus_ed_text)
    TextInputEditText activityContactusEdText;
    @BindView(R.id.activity_contactus_txt_text)
    TextInputLayout activityContactusTxtText;
    @BindView(R.id.activity_contactus_btn_edit)
    Button activityContactusBtnEdit;
    String name, phone, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        ButterKnife.bind(this);
    }


    

    private void init() {
        name = activityContactusEdUsername.getText().toString();
        phone = activityContactusEdPhone.getText().toString();
        message = activityContactusEdText.getText().toString();
    }

    public void sendMessage(String name, String phone, String message) {
        showProgressDialog(ContactusActivity.this, ContactusActivity.this.getString(R.string.wait));
        getClient().sendMessage(name, phone, message).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus() == 1) {
                        Toast.makeText(ContactusActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.activity_contactus_btn_back, R.id.activity_contactus_btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_contactus_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_contactus_btn_edit:
                init();
                sendMessage(name, phone, message);
                break;
        }
    }
}

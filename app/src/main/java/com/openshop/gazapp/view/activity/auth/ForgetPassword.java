package com.openshop.gazapp.view.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.login.Login;
import com.openshop.gazapp.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;

public class ForgetPassword extends BaseActivity {

    @BindView(R.id.activity_forget_password_btn_back)
    ImageView activityForgetPasswordBtnBack;
    @BindView(R.id.activity_forget_password_img_logo)
    ImageView activityForgetPasswordImgLogo;
    @BindView(R.id.activity_forget_password_tv_message)
    public TextView activityForgetPasswordTvMessage;
    @BindView(R.id.activity_forget_password_rel1)
    RelativeLayout activityForgetPasswordRel1;
    @BindView(R.id.password_container)
    RelativeLayout Container;
    @BindView(R.id.fragment_forget_password_btn_send)
    public Button fragmentForgetPasswordBtnSend;
    @BindView(R.id.fragment_forget_password_ed_email)
    TextInputEditText fragmentForgetPasswordEdEmail;
    @BindView(R.id.fragment_forget_password1_txt_email)
    TextInputLayout fragmentForgetPassword1TxtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.activity_forget_password_btn_back, R.id.fragment_forget_password_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_forget_password_btn_back:
                super.superBackPressed();
                break;
            case R.id.fragment_forget_password_btn_send:
                resetPassword(fragmentForgetPasswordEdEmail.getText().toString());
                break;
        }
    }

    public void resetPassword(String emailOrPhone) {
        showProgressDialog(this, this.getString(R.string.wait));
        getClient().resetPassword(emailOrPhone).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dismissProgressDialog();
                Toast.makeText(ForgetPassword.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus() == 1) {
                        Intent new_password = new Intent(ForgetPassword.this , ForgetPassword_2.class);
                        new_password.putExtra("email" , fragmentForgetPasswordEdEmail.getText().toString());
                        startActivity(new_password);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.superBackPressed();
    }
}

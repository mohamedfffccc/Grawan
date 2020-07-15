package com.openshop.gazapp.view.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.login.Login;
import com.openshop.gazapp.view.activity.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.CODE;

public class NewPassword extends AppCompatActivity {

    @BindView(R.id.activity_forget_password_btn_back)
    ImageView activityForgetPasswordBtnBack;
    @BindView(R.id.activity_forget_password_img_logo)
    ImageView activityForgetPasswordImgLogo;
    @BindView(R.id.activity_forget_password_tv_message)
    TextView activityForgetPasswordTvMessage;
    @BindView(R.id.activity_forget_password_rel1)
    RelativeLayout activityForgetPasswordRel1;
    @BindView(R.id.activity_new_password_ed_new_password)
    TextInputEditText activityNewPasswordEdNewPassword;
    @BindView(R.id.activity_new_password_txt_new_password)
    TextInputLayout activityNewPasswordTxtNewPassword;
    @BindView(R.id.activity_new_password_ed_confirm_new_password)
    TextInputEditText activityNewPasswordEdConfirmNewPassword;
    @BindView(R.id.activity_new_password_txt_confirm_new_password)
    TextInputLayout activityNewPasswordTxtConfirmNewPassword;
    @BindView(R.id.activity_new_password_btn_confirm)
    Button activityNewPasswordBtnConfirm;
    @BindView(R.id.password_container)
    RelativeLayout passwordContainer;
    private String code;
    private String new_password;
    private String new_password_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        ButterKnife.bind(this);
    }

    private void init() {
         code = getIntent().getExtras().getString(CODE);
         Log.d("the code" , code);
         new_password = activityNewPasswordEdNewPassword.getText().toString();
         new_password_confirm = activityNewPasswordEdConfirmNewPassword.getText().toString();
    }

    @OnClick({R.id.activity_forget_password_btn_back, R.id.activity_new_password_btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_forget_password_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_new_password_btn_confirm:
                init();

                createNewPassword(new_password , new_password_confirm , code);
                break;
        }
    }
    public void createNewPassword(String new_password , String new_password_confirm , String code)
    {
        showProgressDialog(this, this.getString(R.string.wait));
        getClient().newPassword(new_password,new_password_confirm,code).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dismissProgressDialog();
                Toast.makeText(NewPassword.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus() == 1) {
                        startActivity(new Intent(NewPassword.this, AuthActivity.class));
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }
}

package com.openshop.gazapp.view.activity.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
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
import static com.openshop.gazapp.data.local.GrawanConstants.CODE;

public class ForgetPassword_2 extends AppCompatActivity {

    @BindView(R.id.activity_forget_password_btn_back)
    ImageView activityForgetPasswordBtnBack;
    @BindView(R.id.activity_forget_password_img_logo)
    ImageView activityForgetPasswordImgLogo;
    @BindView(R.id.activity_forget_password_tv_message)
    TextView activityForgetPasswordTvMessage;
    @BindView(R.id.activity_forget_password_rel1)
    RelativeLayout activityForgetPasswordRel1;

    @BindView(R.id.activity_forget_password_tv_timer)
    TextView activityForgetPasswordTvTimer;
    @BindView(R.id.firstPinView)
    PinView firstPinView;
    @BindView(R.id.activity_forget_password_lin_code)
    LinearLayout activityForgetPasswordLinCode;
    @BindView(R.id.activity_forget_password_btn_confirm)
    Button activityForgetPasswordBtnConfirm;
    @BindView(R.id.activity_forget_password_tv_reset)
    TextView activityForgetPasswordTvReset;
    @BindView(R.id.password_container)
    RelativeLayout passwordContainer;
    private CountDownTimer timer;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_2);
        ButterKnife.bind(this);
        startTimer(5);
        email=getIntent().getExtras().getString("email");
    }

    public void startTimer(int m) {
        activityForgetPasswordTvTimer.setText(String.valueOf(m));
        timer = new CountDownTimer(m * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = (millisUntilFinished / 1000) % 60;
                long minute = (millisUntilFinished / 1000) / 60;
                activityForgetPasswordTvTimer.setText(minute + ":" + seconds);

            }

            @Override
            public void onFinish() {

                activityForgetPasswordBtnConfirm.setVisibility(View.GONE);
                firstPinView.setVisibility(View.GONE);
                activityForgetPasswordTvTimer.setText(R.string.code_expired);
                activityForgetPasswordTvTimer.setTextColor(ForgetPassword_2.this.getColor(R.color.darkred));


            }
        }.start();
    }

    @OnClick({R.id.activity_forget_password_btn_back, R.id.activity_forget_password_btn_confirm, R.id.activity_forget_password_tv_reset})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_forget_password_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_forget_password_btn_confirm:

                //TODO get to new password
                String code = firstPinView.getText().toString();
                Log.d(CODE , code);
                Intent new_password_intent = new Intent(this, NewPassword.class);
                new_password_intent.putExtra(CODE , code);
                startActivity(new_password_intent);
                break;
            case R.id.activity_forget_password_tv_reset:
                //TODO resend code
                activityForgetPasswordBtnConfirm.setVisibility(View.VISIBLE);
                firstPinView.setVisibility(View.VISIBLE);

                timer.cancel();
                resetPassword();


                break;
        }
    }
    public void resetPassword() {
        showProgressDialog(this, this.getString(R.string.wait));
        getClient().resetPassword(email).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dismissProgressDialog();
                Toast.makeText(ForgetPassword_2.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus() == 1) {
                        startTimer(5);
                        activityForgetPasswordTvTimer.setTextColor(ForgetPassword_2.this.getColor(R.color.darkblue));
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

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
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;

public class ChangePasswordActivity extends AppCompatActivity {


    String token, new_password, new_password_confirm, old_password;
    @BindView(R.id.activity_change_password_btn_back)
    ImageView activityChangePasswordBtnBack;
    @BindView(R.id.activity_change_password_ed_current_password)
    TextInputEditText activityChangePasswordEdCurrentPassword;
    @BindView(R.id.activity_change_password_ed_new_password)
    TextInputEditText activityChangePasswordEdNewPassword;
    @BindView(R.id.activity_change_password_ed_confirm_new_password)
    TextInputEditText activityChangePasswordEdConfirmNewPassword;
    @BindView(R.id.activity_change_password_btn_edit)
    Button activityChangePasswordBtnEdit;
    @BindView(R.id.activity_profile_rel_title)
    RelativeLayout activityProfileRelTitle;
    @BindView(R.id.activity_change_password_txt_current_password)
    TextInputLayout activityChangePasswordTxtCurrentPassword;
    @BindView(R.id.activity_change_password_txt_new_password)
    TextInputLayout activityChangePasswordTxtNewPassword;
    @BindView(R.id.activity_change_password_txt_confirm_new_password)
    TextInputLayout activityChangePasswordTxtConfirmNewPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
    }

    public void getData() {
        token = LoadData(this, API_TOKEN);
        old_password = activityChangePasswordEdCurrentPassword.getText().toString();
        new_password = activityChangePasswordEdNewPassword.getText().toString();
        new_password_confirm = activityChangePasswordEdConfirmNewPassword.getText().toString();

    }

    public void changePassword() {
        getData();
        showProgressDialog(this, this.getString(R.string.wait));
        getClient().changePassword(old_password, new_password, new_password_confirm, token).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                dismissProgressDialog();
                Toast.makeText(ChangePasswordActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus() == 1) {
                        //     SaveData(this , USER_PASSWORD , );
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
    }


    @OnClick({R.id.activity_change_password_btn_back, R.id.activity_change_password_btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_change_password_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_change_password_btn_edit:
                changePassword();
                break;
        }
    }
}

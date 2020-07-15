package com.openshop.gazapp.view.activity.home;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.setting.Setting;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;

public class RulesActivity extends AppCompatActivity {

    @BindView(R.id.activity_about_btn_back)
    ImageView activityAboutBtnBack;
    @BindView(R.id.activity_rules_rel_title)
    RelativeLayout activityRulesRelTitle;
    @BindView(R.id.activity_rules_tv_about)
    TextView activityRulesTvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        ButterKnife.bind(this);
        getInfo();
    }
    private void getInfo() {
        showProgressDialog(this , RulesActivity.this.getString(R.string.wait));
        getClient().getAppSetting().enqueue(new Callback<Setting>() {
            @Override
            public void onResponse(Call<Setting> call, Response<Setting> response) {
                dismissProgressDialog();
                try {
                    if (response.body().getStatus()==1) {
                        activityRulesTvAbout.setText(response.body().getData().getTermsConditions());
                    }

                }
                catch (Exception e)
                {
                    dismissProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<Setting> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.activity_about_btn_back)
    public void onViewClicked() {
        super.onBackPressed();
    }
}

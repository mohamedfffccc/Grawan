package com.openshop.gazapp.view.activity.home;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.profile.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.convertFileToMultipart;
import static com.openshop.gazapp.data.helper.HelperMethod.convertToRequestBody;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.openGallery;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_PASSWORD;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;

public class ProfileActivity extends AppCompatActivity {
    String profile_url;

    @BindView(R.id.activity_profile_btn_back)
    ImageView activityProfileBtnBack;
    @BindView(R.id.activity_profile_rel_title)
    RelativeLayout activityProfileRelTitle;
    @BindView(R.id.activity_profile_tv_welcome)
    TextView activityProfileTvWelcome;
    @BindView(R.id.activity_profile_ed_username)
    TextInputEditText activityProfileEdUsername;
    @BindView(R.id.activity_profile_txt_username)
    TextInputLayout activityProfileTxtUsername;
    @BindView(R.id.activity_profile_ed_phone)
    TextInputEditText activityProfileEdPhone;
    @BindView(R.id.activity_profile_txt_phone)
    TextInputLayout activityProfileTxtPhone;
    @BindView(R.id.activity_profile_ed_phone_home)
    TextInputEditText activityProfileEdPhoneHome;
    @BindView(R.id.activity_profile_txt_phone_home)
    TextInputLayout activityProfileTxtPhoneHome;
    @BindView(R.id.activity_profile_ed_password)
    TextInputEditText activityProfileEdPassword;
    @BindView(R.id.activity_profile_txt_password)
    TextInputLayout activityProfileTxtPassword;
    @BindView(R.id.activity_profile_btn_edit)
    Button activityProfileBtnEdit;
    @BindView(R.id.activity_profile_iv_profile)
    CircleImageView activityProfileIvProfile;
    //TODO
    RequestBody token, user_name, phone_number, home_number;
    MultipartBody.Part image;
    @BindView(R.id.activity_profile_btn_change_password)
    TextView activityProfileBtnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setProfile();
    }

    private void setProfile() {
        activityProfileBtnChangePassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        activityProfileEdUsername.setText(loadUserData(this, USER_DATA).getName());
        try {
            if (loadUserData(this, USER_DATA).getImage() != null) {
                Glide.with(this).load(loadUserData(this, USER_DATA).getImage()).into(activityProfileIvProfile);
            }
        } catch (Exception e) {

        }
        activityProfileEdPhone.setText(loadUserData(this, USER_DATA).getPhone());
        activityProfileEdPhoneHome.setText(loadUserData(this, USER_DATA).getHomePhone());
        activityProfileEdPassword.setText(LoadData(ProfileActivity.this, USER_PASSWORD));

    }

    @OnClick({R.id.activity_profile_btn_change_password,R.id.activity_profile_btn_back, R.id.activity_profile_btn_edit, R.id.activity_profile_iv_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_profile_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_profile_btn_edit:
                startActivity(new Intent(this , EditProfileActivity.class));
                break;
            case R.id.activity_profile_iv_profile:
//                profile_url = openGallery(this, activityProfileIvProfile);
                break;
            case R.id.activity_profile_btn_change_password :
                //TODO change password activity
                startActivity(new Intent(this,ChangePasswordActivity.class));
                break;
        }
    }





    @OnClick(R.id.activity_profile_btn_change_password)
    public void onViewClicked() {
    }
}

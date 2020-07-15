package com.openshop.gazapp.view.activity.home;

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
import static com.openshop.gazapp.data.helper.HelperMethod.image_url;
import static com.openshop.gazapp.data.helper.HelperMethod.openGallery;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_PASSWORD;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.saveUserData;

public class EditProfileActivity extends AppCompatActivity {

    @BindView(R.id.activity_edit_prof_btn_back)
    ImageView activityEditProfBtnBack;
    @BindView(R.id.activity_edit_prof_rel_title)
    RelativeLayout activityEditProfRelTitle;
    @BindView(R.id.activity_edit_prof_tv_welcome)
    TextView activityEditProfTvWelcome;
    @BindView(R.id.activity_edit_prof_iv_edit_prof)
    CircleImageView activityEditProfIvEditProf;
    @BindView(R.id.activity_edit_prof_ed_username)
    TextInputEditText activityEditProfEdUsername;
    @BindView(R.id.activity_edit_prof_txt_username)
    TextInputLayout activityEditProfTxtUsername;
    @BindView(R.id.activity_edit_prof_ed_phone)
    TextInputEditText activityEditProfEdPhone;
    @BindView(R.id.activity_edit_prof_txt_phone)
    TextInputLayout activityEditProfTxtPhone;
    @BindView(R.id.activity_edit_prof_ed_phone_home)
    TextInputEditText activityEditProfEdPhoneHome;
    @BindView(R.id.activity_edit_prof_txt_phone_home)
    TextInputLayout activityEditProfTxtPhoneHome;
    @BindView(R.id.activity_edit_prof_btn_edit)
    Button activityEditProfBtnEdit;
    //TODO
    RequestBody token, user_name, phone_number, home_number;
    MultipartBody.Part image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        setProfile();
    }

    @OnClick({R.id.activity_edit_prof_btn_back, R.id.activity_edit_prof_btn_edit , R.id.activity_edit_prof_iv_edit_prof})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_edit_prof_btn_back:
                super.onBackPressed();
                break;
            case R.id.activity_edit_prof_btn_edit:
                edit();

                break;
            case R.id.activity_edit_prof_iv_edit_prof:
                openGallery(this, activityEditProfIvEditProf);

                break;
        }
    }
    private void setProfile() {
        activityEditProfEdUsername.setText(loadUserData(this, USER_DATA).getName());
        try {
            if (loadUserData(this, USER_DATA).getImage() != null) {
                Glide.with(this).load(loadUserData(this, USER_DATA).getImage()).into(activityEditProfIvEditProf);
            }
        } catch (Exception e) {

        }
        activityEditProfEdPhone.setText(loadUserData(this, USER_DATA).getPhone());
        activityEditProfEdPhoneHome.setText(loadUserData(this, USER_DATA).getHomePhone());

    }
    public void convertData() {
        token = convertToRequestBody(LoadData(this, API_TOKEN));
        user_name = convertToRequestBody(activityEditProfEdUsername.getText().toString());
        phone_number = convertToRequestBody(activityEditProfEdPhone.getText().toString());
        home_number = convertToRequestBody(activityEditProfEdPhoneHome.getText().toString());
        image = convertFileToMultipart(image_url, "image");


    }
    public void edit() {
        convertData();
        showProgressDialog(this, this.getString(R.string.wait));
        getClient().editProfile(token, user_name, phone_number, home_number, image).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                dismissProgressDialog();
                Toast.makeText(EditProfileActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus() == 1) {
                        saveUserData(EditProfileActivity.this ,USER_DATA ,  response.body().getData());

                    }
                } catch (Exception e) {
                    dismissProgressDialog();
                    Toast.makeText(EditProfileActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dismissProgressDialog();
                Toast.makeText(EditProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}

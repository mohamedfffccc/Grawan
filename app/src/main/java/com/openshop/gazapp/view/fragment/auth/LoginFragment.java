package com.openshop.gazapp.view.fragment.auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.login.Address;
import com.openshop.gazapp.data.model.login.Login;
import com.openshop.gazapp.data.model.setting.Setting;
import com.openshop.gazapp.view.activity.auth.ForgetPassword;
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
import static com.openshop.gazapp.data.local.GrawanConstants.API_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.FIREBASE_TOKEN;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_PASSWORD;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.LoadData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.SaveData;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.saveList;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.saveUserData;

public class LoginFragment extends Fragment {

    @BindView(R.id.fragment_login_ed_username)
    TextInputEditText fragmentLoginEdUsername;
    @BindView(R.id.fragment_login_txt_username)
    TextInputLayout fragmentLoginTxtUsername;
    @BindView(R.id.fragment_login_ed_password)
    TextInputEditText fragmentLoginEdPassword;
    @BindView(R.id.fragment_login_txt_password)
    TextInputLayout fragmentLoginTxtPassword;
    @BindView(R.id.fragment_login_tv_forget)
    TextView fragmentLoginTvForget;
    @BindView(R.id.fragment_login_btn_login)
    Button fragmentLoginBtnLogin;
    private String token;
    String api_token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);
        fragmentLoginTvForget.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        Toast.makeText(getActivity(), LoadData(getActivity() , FIREBASE_TOKEN), Toast.LENGTH_SHORT).show();

        return root;
    }

    @OnClick({R.id.fragment_login_tv_forget, R.id.fragment_login_btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_login_tv_forget:
                startActivity(new Intent(getActivity() , ForgetPassword.class));
                break;
            case R.id.fragment_login_btn_login:
                //login
                userLogin(fragmentLoginEdUsername.getText().toString() , fragmentLoginEdPassword.getText().toString());
                break;
        }
    }
    //TODO login

    private void userLogin(String user_name , String password) {

        if (user_name.isEmpty()){
            fragmentLoginEdUsername.setError(getActivity().getString(R.string.please_enter_email));
        }

        else if (password.isEmpty()){
            fragmentLoginEdPassword.setError(getActivity().getString(R.string.please_enter_password));

        } else if (password.length() < 6){
            fragmentLoginEdPassword.setError(getActivity().getString(R.string.pass_error));

        }
        else {
            fragmentLoginEdUsername.setError(null);
            fragmentLoginEdPassword.setError(null);
            showProgressDialog(getActivity() , getActivity().getString(R.string.wait));
            getClient().getClientLogin(user_name,password).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    try {
                        if (response.body().getStatus()==1) {
                            //TODO save data
                            saveUserData(getActivity(),USER_DATA, response.body().getData().getClient());
                            SaveData(getActivity() , API_TOKEN , response.body().getData().getApiToken());
                            SaveData(getActivity() , USER_PASSWORD , fragmentLoginEdPassword.getText().toString());
                            registerFirebase(response.body().getData().getClient().getId());
//                             saveList(getActivity(), "addresses", response.body().getData().getClient().getAddresses());
                            getActivity().startActivity(new Intent(getActivity() , HomeActivity.class));
                        }
                    }
                    catch (Exception e)
                    {
                        dismissProgressDialog();
                    //    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    dismissProgressDialog();
                   // Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public void  registerFirebase(int id)
    {
        api_token =  LoadData(getActivity() , API_TOKEN);
        token=LoadData(getActivity() , FIREBASE_TOKEN);
//        Toast.makeText(getActivity(),   "\n" + api_token, Toast.LENGTH_SHORT).show();


        getClient().registerToken(token,"android" , id).enqueue(new Callback<Setting>() {
            @Override
            public void onResponse(Call<Setting> call, Response<Setting> response) {
          //      Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();



                try {
                    Log.d("FIREBASE" , response.body().getMsg());

                }
                catch (Exception e)
                {
                    Log.d("FIREBASE" , e.getMessage());

                }
                }



            @Override
            public void onFailure(Call<Setting> call, Throwable t) {
                Log.d("FIREBASE" , t.getMessage());


            }
        });
    }
}

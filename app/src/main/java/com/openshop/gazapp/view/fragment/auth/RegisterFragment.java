package com.openshop.gazapp.view.fragment.auth;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.data.model.login.Login;
import com.openshop.gazapp.view.activity.auth.AuthActivity;
import com.openshop.gazapp.view.activity.home.LocationActivity;
import com.openshop.gazapp.view.activity.home.PaymentActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.openshop.gazapp.data.api.RetrofitClient.getClient;
import static com.openshop.gazapp.data.helper.HelperMethod.dismissProgressDialog;
import static com.openshop.gazapp.data.helper.HelperMethod.replaceFragment;
import static com.openshop.gazapp.data.helper.HelperMethod.showProgressDialog;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;
import static com.openshop.gazapp.view.activity.home.LocationActivity.ADDRESS;
import static com.openshop.gazapp.view.activity.home.LocationActivity.LATITUDE;
import static com.openshop.gazapp.view.activity.home.LocationActivity.LONGITUDE;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {


    @BindView(R.id.fragment_register_ed_username)
    TextInputEditText fragmentRegisterEdUsername;
    @BindView(R.id.fragment_register_txt_username)
    TextInputLayout fragmentRegisterTxtUsername;
    @BindView(R.id.fragment_register_ed_mobilephone)
    TextInputEditText fragmentRegisterEdMobilephone;
    @BindView(R.id.fragment_register_txt_mobile)
    TextInputLayout fragmentRegisterTxtMobile;
    @BindView(R.id.fragment_register_ed_home_phone)
    TextInputEditText fragmentRegisterEdHomePhone;
    @BindView(R.id.fragment_register_txt_home_phone)
    TextInputLayout fragmentRegisterTxtHomePhone;
    @BindView(R.id.fragment_register_ed_password)
    TextInputEditText fragmentRegisterEdPassword;
    @BindView(R.id.fragment_register_txt_password)
    TextInputLayout fragmentRegisterTxtPassword;
    @BindView(R.id.fragment_register_ed_confirm_password)
    TextInputEditText fragmentRegisterEdConfirmPassword;
    @BindView(R.id.fragment_register_txt_confirm_password)
    TextInputLayout fragmentRegisterTxtConfirmPassword;
    @BindView(R.id.fragment_register_cb_agree_terms)
    CheckBox fragmentRegisterCbAgreeTerms;
    @BindView(R.id.fragment_login_btn_login)
    Button fragmentLoginBtnLogin;
    @BindView(R.id.fragment_register_ed_email)
    TextInputEditText fragmentRegisterEdEmail;
    @BindView(R.id.fragment_register_txt_email)
    TextInputLayout fragmentRegisterTxtEmail;
    @BindView(R.id.fragment_register_ed_address)
    TextInputEditText fragmentRegisterEdAddress;
    @BindView(R.id.fragment_register_txt_address)
    TextInputLayout fragmentRegisterTxtAddress;
    HashMap<String,String> map = new HashMap<>();
    private boolean islocation;
    AuthActivity activity;

    public RegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        fragmentRegisterCbAgreeTerms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        activity=(AuthActivity) getActivity();
        // Inflate the layout for this fragment
        return view;

    }

    private void init() {
        map.put("name",fragmentRegisterEdUsername.getText().toString());
        map.put("email",fragmentRegisterEdEmail.getText().toString());
        map.put("phone",fragmentRegisterEdMobilephone.getText().toString());
        map.put("home_phone",fragmentRegisterEdHomePhone.getText().toString());
        map.put("password",fragmentRegisterEdPassword.getText().toString());
        map.put("password_confirmation",fragmentRegisterEdConfirmPassword.getText().toString());
        map.put("latitude",String.valueOf(LATITUDE));
        map.put("longitude",String.valueOf(LONGITUDE));
        map.put("address",ADDRESS);




    }
    public void register()
    {
        init();
        if (fragmentRegisterEdUsername.getText().toString().isEmpty())
    {
        fragmentRegisterEdUsername.setError(getActivity().getString(R.string.username_required));
    }
        else  if (fragmentRegisterEdEmail.getText().toString().isEmpty())
        {
            fragmentRegisterEdEmail.setError(getActivity().getString(R.string.email_required));
        }
        else  if (!fragmentRegisterEdEmail.getText().toString().contains("@"))
        {
            fragmentRegisterEdEmail.setError(getActivity().getString(R.string.enter_valid_email));
        }
        else  if (fragmentRegisterEdMobilephone.getText().toString().isEmpty())
        {
            fragmentRegisterEdMobilephone.setError(getActivity().getString(R.string.phone_required));
        } else  if (fragmentRegisterEdHomePhone.getText().toString().isEmpty())
        {
            fragmentRegisterEdHomePhone.setError(getActivity().getString(R.string.home_required));
        } else  if (fragmentRegisterEdPassword.getText().toString().length()<6)
        {
            fragmentRegisterEdPassword.setError(getActivity().getString(R.string.pass_error));
        } else  if (!fragmentRegisterEdConfirmPassword.getText().toString().equals(fragmentRegisterEdPassword.getText().toString()))
        {
            fragmentRegisterEdConfirmPassword.setError(getActivity().getString(R.string.confirm_error));
        } else  if (fragmentRegisterEdAddress.getText().toString().isEmpty())
        {
            fragmentRegisterEdAddress.setError(getActivity().getString(R.string.address_required));
        }
        else
        {
            showProgressDialog(getActivity(), getString(R.string.wait));
            getClient().register(map).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    dismissProgressDialog();
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();

                    try {
                        if (response.body().getStatus()==1) {
                            replaceFragment(getActivity().getSupportFragmentManager(), R.id.auth_container, new LoginFragment());

                            TabLayout.Tab tab = activity.activityAuthTabAuthentication.getTabAt(0);
                            tab.select();

                        }
                    }
                    catch (Exception e)
                    {
                        dismissProgressDialog();
                        Log.d("MSG" , e.getMessage());
                      //  Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    dismissProgressDialog();
                //    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }



    }


    @OnClick({R.id.fragment_register_ed_address, R.id.fragment_login_btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fragment_register_ed_address:
                getActivity().startActivity(new Intent(getActivity() , LocationActivity.class));
                islocation=true;
                break;
            case R.id.fragment_login_btn_login:
                if(fragmentRegisterCbAgreeTerms.isChecked()==true) {
                    register();
                }
                else  if (fragmentRegisterCbAgreeTerms.isChecked()==false)
                {
              Toast.makeText(activity, this.getString(R.string.agree_terms), Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (islocation==true) {
            fragmentRegisterEdAddress.setText(ADDRESS);

        }
        islocation=false;
    }
}

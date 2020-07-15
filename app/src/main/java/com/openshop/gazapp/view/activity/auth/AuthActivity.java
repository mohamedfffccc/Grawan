package com.openshop.gazapp.view.activity.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;
import com.openshop.gazapp.R;
import com.openshop.gazapp.view.activity.home.HomeActivity;
import com.openshop.gazapp.view.fragment.auth.LoginFragment;
import com.openshop.gazapp.view.fragment.auth.RegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openshop.gazapp.data.helper.HelperMethod.checkWriteExternalPermission;
import static com.openshop.gazapp.data.helper.HelperMethod.onPermission;
import static com.openshop.gazapp.data.helper.HelperMethod.replaceFragment;
import static com.openshop.gazapp.data.local.GrawanConstants.USER_DATA;
import static com.openshop.gazapp.data.local.SharedPreferencesManger.loadUserData;

public class AuthActivity extends AppCompatActivity {


    @BindView(R.id.activity_auth_tab_authentication)
   public TabLayout activityAuthTabAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        onPermission(this);
        replaceFragment(getSupportFragmentManager(), R.id.auth_container, new LoginFragment());
        activityAuthTabAuthentication.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        replaceFragment(getSupportFragmentManager(), R.id.auth_container, new LoginFragment());
                        break;
                    case 1:
                        replaceFragment(getSupportFragmentManager(), R.id.auth_container, new RegisterFragment());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //TODO set default selection
        TabLayout.Tab tab = activityAuthTabAuthentication.getTabAt(0);
        tab.select();
//        try {
////            if (loadUserData(this , USER_DATA)!=null)
////            {
////                startActivity(new Intent(this , HomeActivity.class));
////            }
////
////        }
////        catch (Exception e)
////        {
////
////        }


    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}

package com.openshop.gazapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.openshop.gazapp.view.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

   public BaseFragment baseFragment;


    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onback();
        finish();
    }
}

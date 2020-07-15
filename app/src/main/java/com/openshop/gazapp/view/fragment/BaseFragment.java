package com.openshop.gazapp.view.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openshop.gazapp.view.activity.BaseActivity;


public abstract class BaseFragment extends Fragment {
public BaseActivity baseActivity;
public void SETupACTIVITY()
{
    baseActivity = (BaseActivity) getActivity();
    baseActivity.baseFragment=this;
}
public void onback()
{
    baseActivity.superBackPressed();
}

    public abstract void onBack();
}

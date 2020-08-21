package com.sdlifes.sdlifes.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jwenfeng.library.pulltorefresh.PullToRefreshLayout;

import www.xcd.com.mylibrary.base.fragment.BaseFragment;

/**
 * Created by gs on 2018/2/3.
 */

public abstract class SimpleTopbarFragment extends BaseFragment{

    protected PullToRefreshLayout refresh;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }


}

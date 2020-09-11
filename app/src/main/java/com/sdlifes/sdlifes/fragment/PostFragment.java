package com.sdlifes.sdlifes.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.application.SimpleTopbarFragment;

import java.util.Map;



public class PostFragment extends SimpleTopbarFragment {


    @Override
    protected Object getTopbarTitle() {
        return "";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_post;
    }

    private boolean isPrepared;

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        //填充各控件的数据
        initData();
    }

    private void initData() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) { //隐藏时所作的事情
            lazyLoad();
            isVisible = false;
        }else {
            isVisible = true;
        }
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {

        isPrepared = true;
        lazyLoad();

    }


    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {

    }
}

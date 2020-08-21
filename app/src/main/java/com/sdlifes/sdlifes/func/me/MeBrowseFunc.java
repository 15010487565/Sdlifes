package com.sdlifes.sdlifes.func.me;

import android.app.Activity;
import android.content.Intent;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.BrowseActivity;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * Created by gs on 2020/8/10.
 * 浏览
 */
public class MeBrowseFunc extends BaseFunc {


    public MeBrowseFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.me_browse;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.me_browse;
    }
    @Override
    public void onclick() {
        getActivity().startActivity(new Intent(getActivity(), BrowseActivity.class));
    }
}


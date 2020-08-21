package com.sdlifes.sdlifes.func.me;

import android.app.Activity;
import android.content.Intent;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.AboutActivity;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * Created by gs on 2020/8/10.
 */
public class MeAboutFunc extends BaseFunc {


    public MeAboutFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.me_about;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.me_about;
    }
    @Override
    public void onclick() {
        getActivity().startActivity(new Intent(getActivity(), AboutActivity.class));
    }
}


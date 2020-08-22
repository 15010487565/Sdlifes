package com.sdlifes.sdlifes.func.me;

import android.app.Activity;
import android.content.Intent;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.SettingActivity;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * Created by gs on 2020/8/10.
 */
public class MeSettingFunc extends BaseFunc {


    public MeSettingFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.me_setting;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.setting;
    }
    @Override
    public void onclick() {
        getActivity().startActivity(new Intent(getActivity(), SettingActivity.class));
    }
}

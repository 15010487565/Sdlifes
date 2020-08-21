package com.sdlifes.sdlifes.func.me;

import android.app.Activity;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.MainActivity;
import com.sdlifes.sdlifes.dialog.QQDialogFragment;

import www.xcd.com.mylibrary.func.BaseFunc;

/**
 * Created by gs on 2020/8/10.
 * 联系
 */
public class MeQQFunc extends BaseFunc {


    public MeQQFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.me_relation;
    }

    @Override
    public int getFuncIcon() {
        return 0;
    }

    @Override
    public int getFuncName() {
        return R.string.me_relation;
    }
    @Override
    public void onclick() {
        QQDialogFragment qqDialogFr = new QQDialogFragment();
        qqDialogFr.show(((MainActivity)getActivity()).getSupportFragmentManager(),"UserInfoNewActivity");

    }
}


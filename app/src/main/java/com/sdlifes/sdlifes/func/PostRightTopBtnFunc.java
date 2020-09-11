package com.sdlifes.sdlifes.func;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.PostActivity;

import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


public class PostRightTopBtnFunc extends BaseTopTextViewFunc {

    public PostRightTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.post;
    }

    @Override
    protected int getFuncTextRes() {
        return R.string.post_A;
    }

    @Override
    public View initFuncView(LayoutInflater inflater) {
        // 获得layout
        View funcView = inflater.inflate(www.xcd.com.mylibrary.R.layout.button_topbar_post, null);
        // 获得func id
        funcView.setId(getFuncId());
        // TextView
        TextView textView = (TextView) funcView.findViewById(www.xcd.com.mylibrary.R.id.func_text);

        if (getFuncTextRes() > 0) {
            if (getFuncBgTextRes()>0){
                textView.setBackgroundResource(getFuncBgTextRes());
            }
            textView.setText(getFuncTextRes());
        } else {
            textView.setText(getFuncText());
        }
        return funcView;

    }

    @Override
    public void onclick(View v) {

        ((PostActivity) getActivity()).editor();
    }

}

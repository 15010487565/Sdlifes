package com.sdlifes.sdlifes.func;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.me.BrowseActivity;

import www.xcd.com.mylibrary.func.BaseTopTextViewFunc;


public class BrowseRightTopBtnFunc extends BaseTopTextViewFunc {

    private boolean isEditor = false;
    TextView textView;
    public BrowseRightTopBtnFunc(Activity activity) {
        super(activity);
    }

    @Override
    public int getFuncId() {
        return R.id.editor;
    }

    @Override
    protected int getFuncTextRes() {
        return R.string.editor;
    }

    @Override
    public void onclick(View v) {
        isEditor = !isEditor;
        textView = getTextView();
        if (isEditor){
            textView.setText("取消");
        }else {
            textView.setText("编辑");
        }
        ((BrowseActivity) getActivity()).editor(isEditor);

    }

}

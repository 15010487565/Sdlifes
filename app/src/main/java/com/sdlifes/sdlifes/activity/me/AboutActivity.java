package com.sdlifes.sdlifes.activity.me;

import android.os.Bundle;
import android.widget.TextView;

import com.sdlifes.sdlifes.BuildConfig;
import com.sdlifes.sdlifes.R;

import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class AboutActivity extends SimpleTopbarActivity {

    @Override
    protected Object getTopbarTitle() {
        return R.string.me_about;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText("版本号：v"+BuildConfig.VERSION_NAME);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {

    }

}

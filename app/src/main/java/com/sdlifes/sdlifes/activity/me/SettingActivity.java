package com.sdlifes.sdlifes.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.LoginActivity;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;
import com.sdlifes.sdlifes.util.GlideCacheUtil;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.utils.ShareHelper;

public class SettingActivity extends SimpleTopbarActivity {

    private TextView tv_Cache,tv_CacheSize;
//    private TextView tvSettingExit;
    @Override
    protected Object getTopbarTitle() {
        return R.string.setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        tv_Cache = (TextView) findViewById(R.id.tv_Cache);
        tv_Cache.setOnClickListener(this);
        tv_CacheSize = (TextView) findViewById(R.id.tv_CacheSize);
        tv_CacheSize.setText( GlideCacheUtil.getInstance().getCacheSize(this));

       findViewById(R.id.tv_SettingExit).setOnClickListener(this);

        findViewById(R.id.tv_setting_serve).setOnClickListener(this);

       findViewById(R.id.tv_setting_privacy).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_Cache:
                GlideCacheUtil.getInstance().clearImageAllCache(this,tv_CacheSize);
                break;

            case R.id.tv_setting_serve:
                ActivityUtils.startWebViewActivity(this, UrlAddr.CLAUSE,"",true);
                break;
            case R.id.tv_setting_privacy:
                ActivityUtils.startWebViewActivity(this,UrlAddr.PRIVACY,"",true);
                break;
            case R.id.tv_SettingExit:
                ShareHelper.cleanUserId();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}

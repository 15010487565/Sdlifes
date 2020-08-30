package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;

/**
 * Created by Duke on 2015/11/6.
 */
public class WelcomeActivity extends NoTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, LaunchActivity.class);
        startActivity(intent);
        finish();
    }
}


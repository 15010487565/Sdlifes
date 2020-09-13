package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;

public class PostDetailsActivity extends NoTitleActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView topbar_title = findViewById(R.id.topbar_title);
        topbar_title.setText("#\t"+title);

        String content = intent.getStringExtra("content");
        TextView tv_post_details = findViewById(R.id.tv_post_details);
        tv_post_details.setText(content);

        findViewById(R.id.topbar_func_icon).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_func_icon:
                finish();
                break;
        }
    }
}

package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.application.BaseApplication;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;

public class UserStateActivity extends SimpleTopbarActivity implements View.OnClickListener {

    @Override
    protected Object getTopbarTitle() {
        return "选择状态";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_state);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_pt).setOnClickListener(this);
        findViewById(R.id.ll_ready).setOnClickListener(this);
        findViewById(R.id.ll_baby).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,UserStateSaveActivity.class);

        switch (view.getId()){
            case R.id.ll_pt://怀孕中
                intent.putExtra("state",1);

                break;
            case R.id.ll_ready://备孕中
                intent.putExtra("state",0);

                break;
            case R.id.ll_baby://宝宝已出生
                intent.putExtra("state",2);

                break;
        }
        //RegisterActivity;MeFragment
        String source = intent.getStringExtra("source");
        intent.putExtra("source",source);
        startActivity(intent);
        finish();

    }
    /**
     * 连续按两次返回键就退出
     */
    @Override
    public void onBackPressed() {

            BaseApplication.getInstance().exitApp();

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(EventBusMsg event) {
//        String msg = event.getMsg();
//        if (Constant.CLOSE_ACTION.equals(msg)){
//            finish();
//        }
//    }
}

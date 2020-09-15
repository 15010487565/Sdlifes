package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.Constant;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.EventBusMsg;
import com.sdlifes.sdlifes.util.HtmlUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class DetailsActivity extends NoTitleActivity implements
        View.OnClickListener, HttpInterface {
    TextView tvAttention;
    String focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        tvAttention = findViewById(R.id.tv_attention);
        tvAttention.setOnClickListener(this);

        TextView tvTitle = findViewById(R.id.tv_title);
        WebView webView = findViewById(R.id.webView);
        TextView tv_details = findViewById(R.id.tv_details);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        tvTitle.setText(title);

        String content = intent.getStringExtra("content");
        if (content.contains("<p>")){
            HtmlUtils.getHtmlData(content,webView);
        }else {
            tv_details.setText(content);
        }


        focus = intent.getStringExtra("focus");
        /**
         * 是否关注 0未关注 1已关注
         */
        if("1".equals(focus)){
            tvAttention.setText("已关注");
        }else {
            tvAttention.setText("+ 关注");
        }

        String userId = ShareHelper.getUserId();
        String id = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(userId)&&!TextUtils.isEmpty(id)){
            Map<String, String> params = new HashMap<String, String>();
            params.put("userid", userId);
            params.put("newsid", id);
            //增加浏览记录
            OkHttpHelper.postAsyncHttp(this,1003,
                    params, UrlAddr.BROWSE_SAVE,this);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_attention:
                String userId = ShareHelper.getUserId();
                if (TextUtils.isEmpty(userId)){
                    startActivity(new Intent(this,LoginActivity.class));

                }else {
                    String id = getIntent().getStringExtra("id");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userId);
                    params.put("newsid", id);
                    /**
                     * 是否关注 0未关注 1已关注
                     */
                    if("1".equals(focus)){//移除关注
                        OkHttpHelper.postAsyncHttp(this,1001,
                                params, UrlAddr.ATTENTION_REMOVE,this);
                    }else {//添加关注
                        OkHttpHelper.postAsyncHttp(this,1000,
                                params, UrlAddr.ATTENTION_SAVE,this);
                    }

                }
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode){
            case 1000:
                tvAttention.setText("已关注");
                focus = "1";
//                ToastUtil.showToast(returnMsg);
                refreshAtt();
                break;
            case 1001:
                tvAttention.setText("+ 关注");
                focus = "0";
//                ToastUtil.showToast(returnMsg);
                refreshAtt();
                break;

        }
    }

    private void refreshAtt() {
        EventBusMsg messageEvent = new EventBusMsg(Constant.REFRESH_ATTENTION);
        EventBus.getDefault().post(messageEvent);
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        ToastUtil.showToast(errorExcep);
    }
}

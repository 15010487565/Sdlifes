package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ImageUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;

/**
 * Created by chen on 2018/10/16.
 * <p>
 * 引导页或者启动页过后的广告页面  点击跳过或者自动3秒后跳到首页 不缓存图片
 */
public class LaunchActivity extends AppCompatActivity implements HttpInterface {

    ImageView imageView;
    int id;
    String url;
    private MyHandler handler;


    private static class MyHandler extends Handler {

        private WeakReference<LaunchActivity> atyInstance;

        public MyHandler(LaunchActivity aty) {
            this.atyInstance = new WeakReference<LaunchActivity>(aty);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LaunchActivity aty = atyInstance == null ? null : atyInstance.get();            //如果Activity被释放回收了，则不处理这些消息
            if (aty == null || aty.isFinishing()) {
                return;
            }

            Intent intent = new Intent(aty, MainActivity.class);
            aty.startActivity(intent);
            aty.finish();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(outMetrics);
        int widthPixel = outMetrics.widthPixels;
        int heightPixel = outMetrics.heightPixels;
        ImageUtils.setWidthPixel(widthPixel);
        ImageUtils.setHeightPixel(heightPixel);
        imageView = (ImageView) findViewById(R.id.bg_welcome);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchActivity.this, WebViewActivity.class);
                intent.putExtra("Url", url);
                intent.putExtra("AdId", String.valueOf(id));
                startActivity(intent);
                handler.removeCallbacksAndMessages(null);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                Uri uri = Uri.parse(pic);
//                intent.setData(uri);

//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id", String.valueOf(id));
//                OkHttpHelper.getAsyncHttp(LaunchActivity.this,1000,
//                        params, UrlAddr.AD_ADD,null);
            }
        });
        findViewById(R.id.tv_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        OkHttpHelper.getRestfulHttp(
                this, 1000,
                UrlAddr.USERINFO_LAUNCH_AD, this);

        handler = new MyHandler(this);
        handler.sendEmptyMessageDelayed(1, 4000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (handler == null) {
            handler = new MyHandler(this);
            handler.sendEmptyMessageDelayed(1, 3000);
        } else {
            handler.sendEmptyMessageDelayed(1, 3000);
        }

    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        try {
            JSONObject jsonObject = new JSONObject(returnData);
            JSONObject data = jsonObject.getJSONObject("data");
            String pic = data.optString("pic");
            ImageUtils.setImage(imageView, pic);
            url = data.optString("url");
            id = data.optInt("id");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {

    }

}

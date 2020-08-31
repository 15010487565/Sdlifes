package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.func.WhiteBackTopBtnFunc;
import www.xcd.com.mylibrary.help.OkHttpHelper;

public class WebViewVideoActivity extends SimpleTopbarActivity {

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    WebView webview;
    String url;

    @Override
    protected Class<?> getTopbarLeftFunc() {
        return WhiteBackTopBtnFunc.class;
    }

    @Override
    protected Object getTopbarTitle() {
        return "详情";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_video);

    }
    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        webview = findViewById(R.id.webView);
        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        url = url.replaceAll("&apos;","'");
        url = url.replaceAll("&quot;","\"");
        url = url.replaceAll("&gt;",">");
        url = url.replaceAll("&lt;","<");
        url = url.replaceAll("&amp;","&");
//        Log.e("TAG_H5","url="+url);
        WebSettings setting = webview.getSettings();
        setting.setDefaultTextEncodingName("utf-8");
        setting.setJavaScriptEnabled(true);
        setting.setAllowFileAccess(true);// 设置允许访问文件数据
        setting.setSupportZoom(true);// 支持放大网页功能
        setting.setBuiltInZoomControls(true);// 支持缩小网页功能
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.clearCache(true);
        setting.setLoadWithOverviewMode(true);
        setting.setUseWideViewPort(true);
        webview.requestFocusFromTouch();
        //设置处理JavaScript的引擎
        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType, String capture) {
                if (mUploadMessage != null)
                    return;
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(intent, "完成操作需要使用"), FILECHOOSER_RESULTCODE);
            }
        };
        webview.setWebChromeClient(webChromeClient);
        /***打开本地缓存提供JS调用**/
        setting.setDomStorageEnabled(true);
        setting.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        setting.setAppCachePath(appCachePath);
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(true);
//        setting.setJavaScriptEnabled(true);
        setting.setBlockNetworkImage(false);
        // initComponent();
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.e("TAG_shouldOver","URL="+url);
//                view.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        Log.e("TAG_WEB","URL="+url);
        String AdId = intent.getStringExtra("AdId");
        String title = intent.getStringExtra("title");
        TextView tv_video_title = findViewById(R.id.tv_video_title);
        tv_video_title.setText(TextUtils.isEmpty(title)?"":title);
        TextView tv_video_details = findViewById(R.id.tv_video_details);
        int ostate = intent.getIntExtra("ostate",0);//是否跳转 0 不跳转 1跳转
        if (ostate == 1){
            tv_video_details.setVisibility(View.VISIBLE);
        }else {
            tv_video_details.setVisibility(View.GONE);
        }
        tv_video_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ourl = intent.getStringExtra("ourl");
                ActivityUtils.startWebViewActivity(WebViewVideoActivity.this
                        , ourl
                        ,AdId);
            }
        });
        webview.loadUrl(url);
        boolean isAgreement = intent.getBooleanExtra("isAgreement", false);
        if (!isAgreement){
            if (!TextUtils.isEmpty(AdId)){
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", AdId);
                OkHttpHelper.getAsyncHttp(this,1000,
                        params, UrlAddr.AD_ADD,null);
            }
        }

    }
}

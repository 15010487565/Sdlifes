package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.UrlAddr;

import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;

public class WebViewActivity extends AppCompatActivity {

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;
    WebView webview;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        afterSetContentView();
    }

    protected void afterSetContentView() {

        webview = findViewById(R.id.webView);
        Intent intent = getIntent();
        url = intent.getStringExtra("Url");
        Log.e("TAG_H5","url="+url);
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
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                webview.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        Log.e("TAG_WEB","URL="+url);
        webview.loadUrl(url);
        String AdId = intent.getStringExtra("AdId");
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", AdId);
        OkHttpHelper.getAsyncHttp(this,1000,
                params, UrlAddr.AD_ADD,null);
    }
}

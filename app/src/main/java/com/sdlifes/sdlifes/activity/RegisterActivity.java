package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.LoginModel;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;
import com.sdlifes.sdlifes.util.PhoneUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;


public class RegisterActivity extends NoTitleActivity
        implements View.OnClickListener, HttpInterface, TextWatcher {

    private EditText etPhone, etPassword, etPassword1;
    private TextView btnRregister;
    private ImageView ivFinish;

    private EditText mEtVercode;
    public TextView mTvSendVercode;
    private Handler mHandler;
    public boolean mCanSendVercode = true;
    private ImageView iv_agree;
    private TextView ctv;
    private boolean isAgree =false;

    public class LoopHandler extends Handler {

        private final WeakReference<RegisterActivity> mWeakRefContext;

        public LoopHandler(RegisterActivity context) {
            mWeakRefContext = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                RegisterActivity ctx = mWeakRefContext.get();
                if (msg.what > 0) {
                    ctx.mTvSendVercode.setText(String.format(ctx.getString(R.string.x_sencend_resend), msg.what));
                    sendEmptyMessageDelayed(--msg.what, 1000);
                    ctx.mCanSendVercode = false;
                } else {
//                    ctx.mTvSendVercode.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.black_99));
                    ctx.mTvSendVercode.setText("获取验证码");
                    ctx.mCanSendVercode = true;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etPhone = findViewById(R.id.et_phone);
        etPhone.addTextChangedListener(this);
        etPassword = findViewById(R.id.et_password);
        etPassword.addTextChangedListener(this);
        etPassword1 = findViewById(R.id.et_password1);
        etPassword1.addTextChangedListener(this);

        mHandler = new LoopHandler(this);
        mEtVercode = (EditText) findViewById(R.id.et_code);
        mTvSendVercode = (TextView) findViewById(R.id.send_ver_code);
        mTvSendVercode.setOnClickListener(this);
//        mHandler.sendEmptyMessage(60);

        btnRregister = findViewById(R.id.btn_register);
        btnRregister.setOnClickListener(this);
        btnRregister.setEnabled(false);

        ivFinish = findViewById(R.id.iv_Finish);
        ivFinish.setOnClickListener(this);
        iv_agree = findViewById(R.id.iv_agree);
        iv_agree.setOnClickListener(this);
        ctv = findViewById(R.id.ctv_agree);
        String str = "我已阅读并同意服务条款和隐私政策";
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(str);
        // 单独设置点击事件
        ClickableSpan clickableSpanOne = new ClickableSpan() {
            @Override
            public void onClick(View view) {//服务条款
                ActivityUtils.startWebViewActivity(RegisterActivity.this,UrlAddr.CLAUSE,"",true);

            }
            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setColor(Color.parseColor("#E13122"));
                // 设置下划线 true显示、false不显示
                paint.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanOne, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 在设置点击事件、同时设置字体颜色
        ClickableSpan clickableSpanTwo = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                ActivityUtils.startWebViewActivity(RegisterActivity.this,UrlAddr.PRIVACY,"",true);

            }

            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setColor(Color.parseColor("#E13122"));
                // 设置下划线 true显示、false不显示
                paint.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanTwo, 12, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 不设置点击不生效
        ctv.setMovementMethod(LinkMovementMethod.getInstance());
        ctv.setText(spannableBuilder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_register://注册
                String phone = etPhone.getText().toString();
                if (!PhoneUtil.isAvailablePhone(this, phone)) {
                    return;
                }
                String code = mEtVercode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast("验证码不能为空！");
                    return;
                }
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast("密码不能为空！");
                    return;
                }
                String password1 = etPassword1.getText().toString();
                if (!password.equals(password1)) {
                    ToastUtil.showToast("两次输入密码不一致！");
                    return;
                }
                if (!isAgree) {
                    ToastUtil.showToast("请确认已阅读服务条款和隐私政策！");
                    return;
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("code", code);
                params.put("pwd", password);
                OkHttpHelper.postAsyncHttp(this, 1001, params, UrlAddr.REGISTER, this);
                break;
            case R.id.iv_Finish:
                finish();
                break;
            case R.id.send_ver_code:
                String phone1 = etPhone.getText().toString();
                if (!PhoneUtil.isAvailablePhone(this, phone1)) {
                    return;
                }
                if (mCanSendVercode) {
                    mHandler.sendEmptyMessage(60);
//                    Map<String, String> params1 = new HashMap<String, String>();
//                    params1.put("phone", phone1);
                    OkHttpHelper.getRestfulHttp(this, 1000, UrlAddr.REGISTER_SMS + phone1, this);
                }
                break;
            case R.id.iv_agree:
                if (!isAgree){
                    iv_agree.setBackgroundResource(R.drawable.select);
                    isAgree = true;
                }else {
                    iv_agree.setBackgroundResource(R.drawable.unselect);
                    isAgree = false;
                }
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode) {
            case 1000:
//                ToastUtil.showToast(returnMsg);
                break;
            case 1001:
                LoginModel login = JSON.parseObject(returnData,LoginModel.class);
                LoginModel.DataBean loginData = login.getData();
                int userId = loginData.getId();
                ShareHelper.savePrfparams("userid",String.valueOf(userId));
                ShareHelper.savePrfparams("nickname",loginData.getNickname());
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        ToastUtil.showToast(errorExcep);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        String code = mEtVercode.getText().toString();
        String password1 = etPassword1.getText().toString();
        if (!TextUtils.isEmpty(phone) &&
                !TextUtils.isEmpty(code) &&
                !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(password1)) {
            btnRregister.setEnabled(true);
        } else {
            btnRregister.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}

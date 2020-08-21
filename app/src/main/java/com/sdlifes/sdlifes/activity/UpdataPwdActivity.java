package com.sdlifes.sdlifes.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.PhoneUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ToastUtil;

public class UpdataPwdActivity extends AppCompatActivity  implements View.OnClickListener, HttpInterface, TextWatcher {

    private EditText etPhone, etPassword,etPassword1;
    private TextView btnUpdataPwd;
    private ImageView ivFinish;

    private EditText mEtVercode;
    public TextView mTvSendVercode;
    private Handler mHandler;
    public boolean mCanSendVercode = true;
    private CheckedTextView ctv;

    public class LoopHandler extends Handler {

        private final WeakReference<UpdataPwdActivity> mWeakRefContext;

        public LoopHandler(UpdataPwdActivity context) {
            mWeakRefContext = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                UpdataPwdActivity ctx = mWeakRefContext.get();
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
        setContentView(R.layout.activity_updata_pwd);

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

        btnUpdataPwd = findViewById(R.id.btn_submit);
        btnUpdataPwd.setOnClickListener(this);
        btnUpdataPwd.setEnabled(false);

        ivFinish = findViewById(R.id.iv_Finish);
        ivFinish.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_submit://修改密码
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("pwd", password);
                OkHttpHelper.postAsyncHttp(this,1001,params, UrlAddr.REPWD,this);

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
                    Map<String, String> params1 = new HashMap<String, String>();
                    params1.put("phone", phone1);
                    OkHttpHelper.postAsyncHttp(this,1000,params1, UrlAddr.REPWD_SMS,this);
                }
                break;

        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode){
            case 1000:
                ToastUtil.showToast(returnMsg);
                break;
            case 1001:
                finish();
                ToastUtil.showToast(returnMsg);
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
                !TextUtils.isEmpty(password)&&
                !TextUtils.isEmpty(password1)) {
            btnUpdataPwd.setEnabled(true);
        } else {
            btnUpdataPwd.setEnabled(false);
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

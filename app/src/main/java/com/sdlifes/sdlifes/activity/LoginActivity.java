package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.dialog.AgreementDialogFragment;
import com.sdlifes.sdlifes.model.LoginModel;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.PhoneUtil;

import java.util.HashMap;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;


public class LoginActivity extends NoTitleActivity
        implements View.OnClickListener, HttpInterface, TextWatcher
        {

    private TextView btnPwd;
    private TextView tvRegister;
    private TextView btnLogin;

    private EditText etPhone, etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnPwd = findViewById(R.id.btn_forget_pwd);
        btnPwd.setOnClickListener(this);
        tvRegister = findViewById(R.id.tv_Register);
        tvRegister.setOnClickListener(this);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        btnLogin.setEnabled(false);

        etPhone = findViewById(R.id.et_phone);
        etPhone.addTextChangedListener(this);
        etPassword = findViewById(R.id.et_password);
        etPassword.addTextChangedListener(this);

        ImageView ivFinish = findViewById(R.id.iv_Finish);
        ivFinish.setOnClickListener(this);


//        etPhone.setText("15010487565");
//        etPassword.setText("123456");
    }



    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){

            case R.id.iv_Finish:

                finish();
                break;

            case R.id.btn_forget_pwd://忘记密码
                intent = new Intent(LoginActivity.this,UpdataPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_Register://手机号注册
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login://登录

                String phone = etPhone.getText().toString();
                if (!PhoneUtil.isAvailablePhone(this, phone)) {
                    return;
                }

                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(password)){
                    ToastUtil.showToast("请输入密码！");
                    return;

                }


                Map<String, String> params = new HashMap<String, String>();
                params.put("phone", phone);
                params.put("pwd", password);
                OkHttpHelper.postAsyncHttp(this,1000,params, UrlAddr.LOGIN,this);

                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        LoginModel login = JSON.parseObject(returnData,LoginModel.class);
        LoginModel.DataBean loginData = login.getData();
        int userId = loginData.getId();
        ShareHelper.savePrfparams("userid",String.valueOf(userId));
        ShareHelper.savePrfparams("nickname",loginData.getNickname());
//        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        ToastUtil.showToast(errorExcep);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(password)){
            btnLogin.setEnabled(true);
        }else {
            btnLogin.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


}

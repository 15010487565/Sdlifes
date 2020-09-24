package com.sdlifes.sdlifes.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.LaunchActivity;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Data:  2020/4/27
 * Auther: xcd
 * Description:
 */
public class LogoutDialogFragment extends DialogFragment implements View.OnClickListener, HttpInterface {

    private View frView;
    private Window window;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        frView = inflater.inflate(R.layout.dialog_fr_post, null);

        return frView;
    }

    @Override
    public void onStart() {
        super.onStart();
        window = getDialog().getWindow();
        TextView tv_hint = frView.findViewById(R.id.tv_hint);
        tv_hint.setText("注销账号后将无法使用此账号登录，请慎重选择！");
        frView.findViewById(R.id.tv_cancel).setOnClickListener(this);

        TextView tv_abandon = frView.findViewById(R.id.tv_abandon);
        tv_abandon.setText("确定");
        tv_abandon.setOnClickListener(this);

        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.height = getResources().getDisplayMetrics().heightPixels;
        window.setAttributes(params);
        final Dialog dialog = getDialog();

        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

            window.setLayout((int) (ImageUtils.getWidthPixel()*0.75), -2);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_abandon:
                String phone = ShareHelper.getPhone();
                OkHttpHelper.getRestfulHttp(
                        getActivity(), 1000,
                        UrlAddr.LOGOUT + (TextUtils.isEmpty(phone) ? "0" : phone), this);
            break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        Intent intent = new Intent(getActivity(), LaunchActivity.class);
        startActivity(intent);
        ShareHelper.cleanUserId();
        ToastUtil.showToast(returnMsg);
    }

    @Override
    public void onErrorResult(int requestCode, String returnMsg) {

    }
}

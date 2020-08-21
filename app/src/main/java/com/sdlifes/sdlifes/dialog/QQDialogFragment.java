package com.sdlifes.sdlifes.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ImageUtils;

import org.json.JSONObject;

import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Data:  2020/4/27
 * Auther: xcd
 * Description:
 */
public class QQDialogFragment extends DialogFragment implements View.OnClickListener, HttpInterface {

    private View frView;
    private Window window;
    private TextView iv_qqhint;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        frView = inflater.inflate(R.layout.dialog_fr_qq, null);

        return frView;
    }

    @Override
    public void onStart() {
        super.onStart();
        window = getDialog().getWindow();
        frView.findViewById(R.id.iv_Close).setOnClickListener(this);
        frView.findViewById(R.id.iv_Copy).setOnClickListener(this);
        WindowManager.LayoutParams params = window.getAttributes();
//        params.gravity = Gravity.BOTTOM;
        // 如果不设置宽度,那么即使你在布局中设置宽度为 match_parent 也不会起作用
        params.height = getResources().getDisplayMetrics().heightPixels;
        window.setAttributes(params);
        final Dialog dialog = getDialog();
        // 下面这些设置必须在此方法(onStart())中才有效
        iv_qqhint = (TextView) frView.findViewById(R.id.iv_qqhint);
        OkHttpHelper.getRestfulHttp(
                getActivity(),1000,
                UrlAddr.RELATION,this);
        if (dialog != null) {
//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

            window.setLayout((int) (ImageUtils.getWidthPixel()*0.75), -2);

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_Close:
                dismiss();
                break;
            case R.id.iv_Copy:
                // 获取系统剪贴板
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, qq);
                // 把数据集设置（复制）到剪贴板
                clipboard.setPrimaryClip(clipData);
                ToastUtil.showToast("已复制至剪贴板！");
            break;
        }
    }
    String qq;
    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        try {
            JSONObject jsonObject = new JSONObject(returnData);
            JSONObject data = jsonObject.getJSONObject("data");
            qq = data.optString("qq");
            String time = data.optString("time");
            String str1 = "HI，亲爱的宝妈: \n" +"客服人员的工作时间为每天";
            String str2 = "哦~\n如有紧急情况，请添加官方QQ群：";
            Spannable string = new SpannableString(str1+time+str2+qq);
            string.setSpan(new ForegroundColorSpan(Color.RED), str1.length(), (str1+time).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            string.setSpan(new ForegroundColorSpan(Color.RED), (str1+time+str2).length(), string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            iv_qqhint.setText(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        ToastUtil.showToast(errorExcep);
    }
}

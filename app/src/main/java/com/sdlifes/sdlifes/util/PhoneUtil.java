package com.sdlifes.sdlifes.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import www.xcd.com.mylibrary.utils.ToastUtil;

/**
 * Created by Jackson on 2015/10/15.
 * Version : 1
 * Details :
 */
public class PhoneUtil {

    /**
     * @param context
     * @param phone   手机号
     * @param isChina 是中国区号
     * @return true代表手机号可用，false代表不可用
     * 中国区号时：判断第一位是1，总位数为11位
     * 其他区号时：判断不为空
     */
    public static boolean isAvailablePhone(Context context, String phone) {

        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast("手机号不能为空！");
            return false;
        }
        String regexPhone = "^1\\d{10}$";
        boolean isAvailablePhone = phone.matches(regexPhone);
        if (!isAvailablePhone) {
            Toast.makeText(context, "请输入正确手机号", Toast.LENGTH_SHORT).show();
        }

        return isAvailablePhone;
    }
}

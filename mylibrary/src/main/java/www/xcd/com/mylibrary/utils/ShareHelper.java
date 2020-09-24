package www.xcd.com.mylibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;

import www.xcd.com.mylibrary.base.application.XCDApplication;

/**
 * Created by Android on 2017/5/19.
 */

public class ShareHelper {

    private static SharedPreferences mSharePreferences;

    public static SharedPreferences getSharePreferences() {
        mSharePreferences = XCDApplication.getAppContext().getSharedPreferences(XCDApplication.getAppContext().getPackageName() + "_preferences", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        return mSharePreferences;
    }

    public static void savePrfparams( String key, int value) {
        SharedPreferences preferences = getSharePreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void savePrfparams( String key, String value) {
        SharedPreferences preferences = getSharePreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getUserId() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("userid", null);
    }
    public static String getPhone() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("phone", null);
    }

    public static void cleanUserId() {
        savePrfparams("userid","");
        savePrfparams("headImage","");
        savePrfparams("nickname","");
        savePrfparams("desc","");
        savePrfparams("sex",0);
        savePrfparams("birthday","");
        savePrfparams("region","");
    }

    public static String getHeadImage() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("headImage", null);
    }
    public static String getNickName() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("nickname", null);
    }
    public static String getDesc() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("desc", null);
    }
    public static int getSex() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getInt("sex", 0);
    }
    public static String getbirthday() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("birthday", null);
    }
    public static String getRegion() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("region", null);
    }
    public static void setAgreementFlag() {
        final SharedPreferences preferences = getSharePreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("AgreementFlag", true);
        editor.commit();
    }

    public static boolean getAgreementFlag() {
        SharedPreferences preferences = getSharePreferences();
        return preferences.getBoolean("AgreementFlag", false);
    }
    public static int getState() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getInt("state", 0);
    }
    //上次月经开始时间
    public static String getM_lasttime() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("m_lasttime",null);
    }
    //月经一般持续天数
    public static int getM_day() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getInt("m_days",0);
    }
    //月经周期天数
    public static int getM_interval() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getInt("m_interval",0);
    }
    //预产期
    public static String getP_etime() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("p_etime",null);
    }

    // 宝宝昵称
    public static String getB_name() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("b_name",null);
    }
    // 宝宝生日
    public static String getB_birthday() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getString("b_birthday",null);
    }
    // 宝宝性别 1男 2女
    public static int getB_sex() {
        final SharedPreferences preferences = getSharePreferences();
        return preferences.getInt("b_sex",0);
    }
}

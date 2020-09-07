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

    public static String getPrfparams(String key) {
        SharedPreferences preferences = getSharePreferences();
        return preferences.getString(key, null);
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
}

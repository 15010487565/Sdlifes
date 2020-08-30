package com.sdlifes.sdlifes.application;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tencent.bugly.crashreport.CrashReport;

import www.xcd.com.mylibrary.base.application.XCDApplication;
import www.xcd.com.mylibrary.utils.AppManager;


/**
 * application基类
 *
 * @author litfb
 * @version 1.0
 * @date 2014年4月10日
 */
public class BaseApplication extends XCDApplication {

    private static BaseApplication instance;
    private String tokenSSO;//一键登录token
    private String resultSSO;//一键登录返回值

    public String getTokenSSO() {
        return tokenSSO;
    }

    public void setTokenSSO(String tokenSSO) {
        this.tokenSSO = tokenSSO;
    }

    public String getResultSSO() {
        return resultSSO;
    }

    public void setResultSSO(String resultSSO) {
        this.resultSSO = resultSSO;
    }

    public static BaseApplication getInstance() {
        if (instance == null) {
            instance = new BaseApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashReport.initCrashReport(getApplicationContext(), "efa397f523", false);
    }

    public String getVersionName() throws Exception {
        // 获取packagemanager的实例  
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息  
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            // 杀死进程前需要关闭app 相关的服务，activity
            AppManager.getInstance().finishAllActivity();
            //停止所有服务
            ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}

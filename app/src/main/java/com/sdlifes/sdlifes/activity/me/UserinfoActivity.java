package com.sdlifes.sdlifes.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.huantansheng.easyphotos.EasyPhotos;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.func.userinfo.AccountBirthdayFunc;
import com.sdlifes.sdlifes.func.userinfo.AccountGenderFunc;
import com.sdlifes.sdlifes.func.userinfo.AccountHeadFunc;
import com.sdlifes.sdlifes.func.userinfo.AccountIntroductionFunc;
import com.sdlifes.sdlifes.func.userinfo.AccountNameFunc;
import com.sdlifes.sdlifes.func.userinfo.AccountRegionFunc;
import com.sdlifes.sdlifes.model.UserinfoModel;
import com.sdlifes.sdlifes.network.Constant;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.EventBusMsg;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import www.xcd.com.mylibrary.PhotoActivity;
import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.utils.ShareHelper;

public class UserinfoActivity extends PhotoActivity {

    private static Class<?>[] systemFuncArray = {
            AccountHeadFunc.class,//头像
            AccountNameFunc.class,//昵称
            AccountIntroductionFunc.class,//介绍
            AccountGenderFunc.class,//性别
            AccountBirthdayFunc.class,//性别
            AccountRegionFunc.class,//地区
    };
    /**
     * 功能对象
     */
    private Hashtable<Integer, BaseFunc> htFunc = new Hashtable<>();
    /**
     * 获得系统功能列表
     */
    protected Class<?>[] getSystemFuncArray() {
        return systemFuncArray;
    }

    private LinearLayout systemFuncView;
    private LinearLayout systemFuncList;

    /**
     * 相机权限
     */
//    public static final int CAMERA_REQUESTCODE = 20001;
//    public static final String[] CAMERAPERMISSION = {
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ,Manifest.permission.READ_EXTERNAL_STORAGE
//            ,Manifest.permission.CAMERA
//    };
    private String path = "";

    @Override
    protected Object getTopbarTitle() {
        return R.string.me_updata_userinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        systemFuncView = findViewById(R.id.me_system_func_view);
        systemFuncList = findViewById(R.id.me_system_func_list);
        // 初始化系统功能
        initSystemFunc();
//        AccountRegionFunc baseFunc = (AccountRegionFunc) htFunc.get(R.id.account_birthday);
//        baseFunc.bindView();
    }
    /**
     * 初始化系统功能
     */
    protected void initSystemFunc() {
        Class<?>[] systemFuncs = getSystemFuncArray();
        // 功能列表为空,隐藏区域
        if (systemFuncs == null || systemFuncs.length == 0) {
            systemFuncView.setVisibility(View.GONE);
            return;
        }
        // 初始化功能
        boolean isSeparator = false;
        for (Class<?> systemFunc : systemFuncs) {
            // view
            View funcView = getFuncView(systemFunc, isSeparator);
            if (funcView != null) {
                // 点击事件
                funcView.setOnClickListener(this);
                // 加入页面
                systemFuncList.addView(funcView);
                isSeparator = true;
            }
        }
        // 设置列表显示
        systemFuncList.setVisibility(View.VISIBLE);
    }

    /**
     * 获得功能View
     *
     * @return
     */
    private View getFuncView(Class<?> funcClazz, boolean isSeparator) {
        BaseFunc func = BaseFunc.newInstance(funcClazz, this);
        if (func == null) {
            return null;
        }
        htFunc.put(func.getFuncId(), func);
        // view
        return func.initFuncView(isSeparator);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            default:
                // func
                BaseFunc func = htFunc.get(v.getId());
                // 处理点击事件
                if (func != null) {
                    func.onclick();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {

            case 101: {
                //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
                if (resultPaths != null && resultPaths.size() > 0) {
                    path = resultPaths.get(0);
                    uploadPhoto(path);
                }
            }
            break;
            case 1:
                String key = data.getStringExtra("key");
                String content = data.getStringExtra("content");
                Log.e("TAG_","key="+key+";content="+content);
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", ShareHelper.getUserId());
                params.put(key, content);
                OkHttpHelper.postAsyncHttp(this,1001,params, UrlAddr.USERINFO_EDIT,this);

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void uploadPhoto(String imgPath) {
        OkHttpHelper.postAsyncImage(this,1000,imgPath, UrlAddr.UPLOADIMG,this);
    }

    public void updataSex(int sex) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", ShareHelper.getUserId());
        params.put("sex", String.valueOf(sex));
        OkHttpHelper.postAsyncHttp(this,1001,params, UrlAddr.USERINFO_EDIT,this);
    }
    public void updataBirthday(String date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", ShareHelper.getUserId());
        params.put("birthday", date);
        OkHttpHelper.postAsyncHttp(this,1001,params, UrlAddr.USERINFO_EDIT,this);
    }
    public void updataRegion(String region) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", ShareHelper.getUserId());
        params.put("region", region);
        OkHttpHelper.postAsyncHttp(this,1001,params, UrlAddr.USERINFO_EDIT,this);
    }


    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        switch (requestCode){
            case 1000:
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(returnData);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String img = data.optString("img");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", ShareHelper.getUserId());
                    params.put("img", img);
                    OkHttpHelper.postAsyncHttp(this,1001,params, UrlAddr.USERINFO_EDIT,this);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 1001:
                UserinfoModel userinfoModel = JSON.parseObject(returnData, UserinfoModel.class);
                UserinfoModel.DataBean data = userinfoModel.getData();

                String img = data.getImg();
                ShareHelper.savePrfparams("headImage",img);
                AccountHeadFunc accountHeadFunc = (AccountHeadFunc) htFunc.get(R.id.account_head);
                accountHeadFunc.refreshHead(img);

                String nickname = data.getNickname();
                Log.e("TAG_","nickname="+nickname);

                ShareHelper.savePrfparams("nickname",nickname);
                AccountNameFunc nameFunc = (AccountNameFunc) htFunc.get(R.id.account_name);
                nameFunc.resetRightName(nickname);

                String desc = data.getDesc();//描述
                ShareHelper.savePrfparams("desc",desc);
                AccountIntroductionFunc introductionFunc = (AccountIntroductionFunc) htFunc.get(R.id.account_introduction);
                introductionFunc.resetRightDesc(desc);

                int sex = data.getSex();//性别 0 未知 1男 2女
                ShareHelper.savePrfparams("sex",sex);
                AccountGenderFunc genderFunc = (AccountGenderFunc) htFunc.get(R.id.account_gender);
                genderFunc.resetRightGender(sex);

                String birthday = data.getBirthday();
                ShareHelper.savePrfparams("birthday",birthday);
                AccountBirthdayFunc birthdayFunc = (AccountBirthdayFunc) htFunc.get(R.id.account_birthday);
                birthdayFunc.resetRightName(birthday);

                String region = data.getRegion();
                ShareHelper.savePrfparams("region",region);
                AccountRegionFunc regionFunc = (AccountRegionFunc) htFunc.get(R.id.account_region);
                regionFunc.refreshRigion(region);

                EventBusMsg messageEvent = new EventBusMsg(Constant.REFRESH_USERINFO);
                EventBus.getDefault().post(messageEvent);
//                ToastUtil.showToast(returnMsg);
                break;
        }
    }

}

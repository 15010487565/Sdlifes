package com.sdlifes.sdlifes.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.LoginActivity;
import com.sdlifes.sdlifes.activity.me.UserinfoActivity;
import com.sdlifes.sdlifes.application.SimpleTopbarFragment;
import com.sdlifes.sdlifes.func.me.MeAboutFunc;
import com.sdlifes.sdlifes.func.me.MeAttentionFunc;
import com.sdlifes.sdlifes.func.me.MeBrowseFunc;
import com.sdlifes.sdlifes.func.me.MeQQFunc;
import com.sdlifes.sdlifes.func.me.MeSettingFunc;
import com.sdlifes.sdlifes.model.UserAdModel;
import com.sdlifes.sdlifes.model.UserinfoModel;
import com.sdlifes.sdlifes.network.Constant;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.EventBusMsg;
import com.sdlifes.sdlifes.util.GlideImageLoader;
import com.sdlifes.sdlifes.util.ImageUtils;
import com.sdlifes.sdlifes.view.CircleImageView;
import com.to.aboomy.banner.Banner;
import com.to.aboomy.banner.IndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.func.BaseFunc;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.utils.ShareHelper;


/**
 * 2020年8月10日16:49:28
 * 我的
 */

public class MeFragment extends SimpleTopbarFragment {

    private static Class<?>[] systemFuncArray = {
            MeAttentionFunc.class,//关注
            MeBrowseFunc.class,//浏览
            MeQQFunc.class,//联系
            MeSettingFunc.class,//设置
            MeAboutFunc.class,//关于
    };
    /**
     * 功能对象
     */
    private Hashtable<Integer, BaseFunc> htFunc = new Hashtable<>();
    /**
     * 系统功能View
     */
    private LinearLayout systemFuncView;
    private LinearLayout systemFuncList;
    private CircleImageView userPhoto;
    private Banner banner;
    private RelativeLayout btnUser;
    private TextView tvExit, userName;

    /**
     * 获得系统功能列表
     */
    protected Class<?>[] getSystemFuncArray() {
        return systemFuncArray;
    }

    @Override
    protected Object getTopbarTitle() {
        return "个人中心";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    private boolean isPrepared;

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        //填充各控件的数据
        initData();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) { //隐藏时所作的事情
            lazyLoad();
            isVisible = false;
        }else {
            isVisible = true;
        }
    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {

        TextView tvUserID = view.findViewById(R.id.user_id);
        tvUserID.setText("ID："+ShareHelper.getUserId());
        btnUser = view.findViewById(R.id.btn_user);
        btnUser.setOnClickListener(this);
        tvExit = view.findViewById(R.id.tv_exit);
        tvExit.setOnClickListener(this);

        systemFuncView = view.findViewById(R.id.me_system_func_view);
        systemFuncList = view.findViewById(R.id.me_system_func_list);
        userPhoto = view.findViewById(R.id.user_photo);
        String headImage = ShareHelper.getHeadImage();
        ImageUtils.setImage(userPhoto,headImage,3000,R.mipmap.head_portrait);
        userName = view.findViewById(R.id.user_name);

        banner = view.findViewById(R.id.banner);
//        String url = "http://pic31.nipic.com/20130722/9252150_095713523386_2.jpg";
//        ImageUtils.setImage(ivMeAD,url);
        // 初始化系统功能
        initSystemFunc();
        isPrepared = true;
        lazyLoad();

    }

    private void initData() {

        String userId = ShareHelper.getUserId();
        if (TextUtils.isEmpty(userId)){
            startActivity(new Intent(getActivity(),LoginActivity.class));
            getActivity().finish();
        }else {
            OkHttpHelper.getRestfulHttp(
                    getActivity(),1000,
                    UrlAddr.USERINFO+userId,this);

            OkHttpHelper.getRestfulHttp(
                    getActivity(),1001,
                    UrlAddr.USERINFO_AD,this);

        }

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
        BaseFunc func = BaseFunc.newInstance(funcClazz, getFragmentActivity());
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
            case R.id.btn_user:
                startActivity(new Intent(getActivity(), UserinfoActivity.class));
                break;
            case R.id.tv_exit:
                ShareHelper.cleanUserId();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
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
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
            switch (requestCode) {
                case 1000:
                    UserinfoModel userinfoModel = JSON.parseObject(returnData, UserinfoModel.class);
                    UserinfoModel.DataBean data = userinfoModel.getData();
                    String img = data.getImg();
                    ShareHelper.savePrfparams("headImage",img);
                    ImageUtils.setImage(userPhoto, img, 3000, R.mipmap.head_portrait);

                    String nickname = data.getNickname();
                    ShareHelper.savePrfparams("nickname",nickname);
                    userName.setText(TextUtils.isEmpty(nickname)?"请设置昵称":nickname);

                    String desc = data.getDesc();//描述
                    ShareHelper.savePrfparams("desc",desc);
                    int sex = data.getSex();//性别 0 未知 1男 2女
                    ShareHelper.savePrfparams("sex",sex);
                    String birthday = data.getBirthday();
                    ShareHelper.savePrfparams("birthday",birthday);
                    String region = data.getRegion();
                    ShareHelper.savePrfparams("region",region);
                    break;
                case 1001:
                    UserAdModel userAdModel = JSON.parseObject(returnData, UserAdModel.class);
                    List<UserAdModel.DataBean> list = userAdModel.getData();
                    //使用内置Indicator
                    IndicatorView qyIndicator = new IndicatorView(getActivity())
                            .setIndicatorColor(Color.DKGRAY)
                            .setIndicatorSelectorColor(Color.WHITE);
                    banner.setIndicator(qyIndicator)
                            .setHolderCreator(new GlideImageLoader())
                            .setPages(list);
                    break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusMsg event) {
        String msg = event.getMsg();
        if (Constant.REFRESH_USERINFO.equals(msg)){
            initData();
        }
    }
}

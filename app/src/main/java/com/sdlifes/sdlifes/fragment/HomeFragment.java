package com.sdlifes.sdlifes.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.gxz.PagerSlidingTabStrip;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.SearchActivity;
import com.sdlifes.sdlifes.application.SimpleTopbarFragment;
import com.sdlifes.sdlifes.model.HomeModel;
import com.sdlifes.sdlifes.network.Constant;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.EventBusMsg;
import com.sdlifes.sdlifes.view.VerticalScrollTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.utils.ShareHelper;


/**
 * Created by gs on 2018/10/16.
 */

public class HomeFragment extends SimpleTopbarFragment {


    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private ArrayList<Fragment> mFragments;
    private MyPagerAdapter pagerAdapter;
    private String[] mTitles;
    private String[] mTitlesId;
//    private TextView tvHomeSerch;
    private VerticalScrollTextView scroll_HomeSerch;
    private boolean isPrepared;
    int id = 16;//初始频道id
    @Override
    protected Object getTopbarTitle() {
        return R.string.home;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) { //隐藏时所作的事情
            if (scroll_HomeSerch !=null)
            scroll_HomeSerch.stopScroll();
        }else {
            if (scroll_HomeSerch !=null)
            scroll_HomeSerch.startScroll(2000);
        }
    }

//
//@Override
//    protected void lazyLoad() {
//        if (!isPrepared || !isVisible) {
//            return;
//        }
//        //填充各控件的数据
//        initData();
//    }
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//
//        if (!hidden) { //隐藏时所作的事情
//            lazyLoad();
//            isVisible = false;
//        }else {
//            isVisible = true;
//        }
//    }

    @Override
    protected void initView(LayoutInflater inflater, View view) {

        RelativeLayout reTopPar = view.findViewById(R.id.topbat_parent);
        reTopPar.setVisibility(View.GONE);



//        tvHomeSerch = view.findViewById(R.id.tv_HomeSerch);
        scroll_HomeSerch = view.findViewById(R.id.scroll_HomeSerch);
        scroll_HomeSerch.setClickLisener(new VerticalScrollTextView.ItemClickLisener() {
            @Override
            public void onClick(int position) {
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
//        scroll_HomeSerch.setVisibility(View.GONE);
        view.findViewById(R.id.ll_search).setOnClickListener(this);
        view.findViewById(R.id.iv_serch).setOnClickListener(this);

        tabs = view.findViewById(R.id.magic_indicator);
        //设置Tab文字的左右间距,传入的是dp
        tabs.setTabPaddingLeftRight(10);

        pager = (ViewPager) view.findViewById(R.id.vp);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                id = Integer.valueOf(mTitlesId[position]);
                Log.d("TAG_切换", "id===" + id);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
//        isPrepared = true;
//        lazyLoad();
        initData();
    }

    private void initData() {
        String userId = ShareHelper.getUserId();
        OkHttpHelper.getRestfulHttp(
                getActivity(), 1000,
                UrlAddr.HOME + id + "/" + (TextUtils.isEmpty(userId) ? "0" : userId), this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.iv_serch://搜索
                getActivity().startActivity(new Intent(getActivity(), SearchActivity.class));
                break;

//            case R.id.iv_more://频道
//                getActivity().startActivityForResult(new Intent(getActivity(), ChannelActivity.class),10001);
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10001:
                initData();
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {

        switch (requestCode) {
            case 1000:
                HomeModel homeModel = JSON.parseObject(returnData, HomeModel.class);
                HomeModel.DataBean data = homeModel.getData();
                List<String> searchArr = data.getSearchArr();
//                StringBuffer buffer = new StringBuffer();
//                if (searchArr != null && searchArr.size() > 0) {
//                    for (int i = 0; i < searchArr.size(); i++) {
//                        if (i > 0){
//                            buffer.append("\t|\t");
//                        }
//                        String s = searchArr.get(i);
//                        buffer.append(s);
//
//                    }
//                }
//                Log.e("TAG_搜索","");
                if (searchArr !=null && searchArr.size() > 0) {
//                    scroll_HomeSerch.setVisibility(View.VISIBLE);
//                    tvHomeSerch.setVisibility(View.GONE);
                    scroll_HomeSerch.setList(searchArr);
                    scroll_HomeSerch.startScroll(2000);
                } else {
//                    scroll_HomeSerch.setVisibility(View.GONE);
//                    tvHomeSerch.setVisibility(View.VISIBLE);
                    scroll_HomeSerch.setText("搜索");
                }
                mFragments = new ArrayList<>();
                categoryArr = data.getCategoryArr();
//                mTitles = categoryArr.toArray(new String[categoryArr.size()]);
                mTitles = new String[categoryArr.size()];
                mTitlesId = new String[categoryArr.size()];
                int temp = 0;
                for (int i = 0; i < categoryArr.size(); i++) {
                    HomeModel.DataBean.CategoryArrBean categoryArrBean = categoryArr.get(i);
                    mTitles[i] = categoryArrBean.getName();
                    mTitlesId[i] = String.valueOf(categoryArrBean.getCategoryid());
                    mFragments.add(HomeCatFragment.getInstance(mTitlesId[i],1));
                    if (categoryArrBean.getCategoryid() == id) {
                        temp = i;
                    }
                }
                try {
                    pagerAdapter = new MyPagerAdapter(getFragmentManager());
                    pager.setAdapter(pagerAdapter);
                    tabs.setViewPager(pager);
                    pager.setCurrentItem(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        super.onErrorResult(errorCode, errorExcep);

    }

    List<HomeModel.DataBean.CategoryArrBean> categoryArr;

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
//            Log.e("TAG_tab1", "mFragments="+mFragments.size());
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            Log.e("TAG_tab1", "position="+position+";title=" + mTitles[position]);
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            try {
                Field mFragments = getClass().getSuperclass().getDeclaredField("mFragments");
                mFragments.setAccessible(true);
                ((ArrayList) mFragments.get(this)).clear();

                Field mSavedState = getClass().getSuperclass().getDeclaredField("mSavedState");
                mSavedState.setAccessible(true);
                ((ArrayList) mSavedState.get(this)).clear();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return super.instantiateItem(container, position);
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
        if (Constant.REFRESH_HOME.equals(msg)){
            id = event.getId();
            initData();
        }
    }



}

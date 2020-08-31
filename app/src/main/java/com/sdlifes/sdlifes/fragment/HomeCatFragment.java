package com.sdlifes.sdlifes.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.HomeAdapter;
import com.sdlifes.sdlifes.model.HomeModel;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;
import www.xcd.com.mylibrary.utils.ShareHelper;


/**
 * Created by gs on 2018/10/16.
 */

public class HomeCatFragment extends Fragment implements HttpInterface,
        BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {


    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeAdapter adapter;
    private String titleId;
    private int page = 1;
    private int size = 10;

    public static HomeCatFragment getInstance(String titleId,int page) {
        HomeCatFragment hcf = new HomeCatFragment();
        hcf.titleId = titleId;
        hcf.page = page;
        return hcf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmeng_cat, null);


        recyclerView = (RecyclerView) view.findViewById(R.id.rc_Home);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<HomeModel.DataBean.NewsArrBean> list = new ArrayList();
        adapter = new HomeAdapter(list);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1));


        initData();

        return view;
    }

    private void initData() {
        String userId = ShareHelper.getUserId();
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        OkHttpHelper.getAsyncHttp(
                getActivity(), 1000,params,
                UrlAddr.HOME + titleId + "/" + (TextUtils.isEmpty(userId) ? "0" : userId), this);
    }

    public RecyclerView.ItemDecoration getRecyclerViewDivider(@DrawableRes int drawableId) {
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(drawableId));
        return itemDecoration;
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {

        switch (requestCode) {
            case 1000:
                HomeModel homeModel = JSON.parseObject(returnData, HomeModel.class);
                HomeModel.DataBean data = homeModel.getData();

                List<HomeModel.DataBean.NewsArrBean> newsArr = data.getNewsArr();
                if (data != null) {
                    if (page == 1) {
                        if (newsArr.size() < size) {
                            adapter.setNewData(newsArr);
                            adapter.loadMoreEnd();
                        } else {
                            adapter.setNewData(newsArr);
                            adapter.loadMoreComplete();
                        }
                    } else {
                        if (newsArr.size() < size) {
                            adapter.addData(newsArr);
                            adapter.loadMoreEnd();
                        } else {
                            adapter.addData(newsArr);
                            adapter.loadMoreComplete();
                        }
                    }
                }

                break;
        }

    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        HomeAdapter homeAdapter = (HomeAdapter) adapter;
        List<HomeModel.DataBean.NewsArrBean> data = homeAdapter.getData();
        HomeModel.DataBean.NewsArrBean newsArrBean = data.get(position);
        //	type = 1 新闻 type = 2广告
        String type = newsArrBean.getType();
        if ("1".equals(type)) {
            int id = newsArrBean.getId();
            String title = newsArrBean.getTitle();
            String content = newsArrBean.getContent();
            String focus = newsArrBean.getFocus();
            ActivityUtils.startDetailsActivity(getActivity(),
                    String.valueOf(id),
                    title, content, focus);
        } else {
            int state = newsArrBean.getState();//	广告 ： 1 网页url地址 2 视频url地址
            if (state == 2){

                ActivityUtils.startWebViewVideoActivity(getActivity(), newsArrBean.getUrl(),newsArrBean.getTitle()
                        ,String.valueOf(newsArrBean.getId()),newsArrBean.getOstate(),newsArrBean.getOurl());

            }else {
                ActivityUtils.startWebViewActivity(getActivity(), newsArrBean.getUrl()
                        ,String.valueOf(newsArrBean.getId()));

            }
        }

    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        initData();
    }
}

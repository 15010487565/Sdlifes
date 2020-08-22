package com.sdlifes.sdlifes.activity.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.BrowseAdapter;
import com.sdlifes.sdlifes.func.BrowseRightTopBtnFunc;
import com.sdlifes.sdlifes.model.BrowseModel;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.utils.ShareHelper;

/**
 * 浏览
 */
public class BrowseActivity extends SimpleTopbarActivity implements
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemChildClickListener{

    private SwipeRefreshLayout ly_pull_refresh;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private BrowseAdapter adapter;
    private LinearLayout ll_Select;
    private int size = 10;
    private int page = 1;

    @Override
    protected Object getTopbarTitle() {
        return "浏览历史";
    }

    private static Class<?> rightFuncArray[] = {BrowseRightTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        ly_pull_refresh.setRefreshing(true);
        initdata();
    }

    private void initdata() {
        String userId = ShareHelper.getUserId();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userId);
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        OkHttpHelper.getAsyncHttp(this,1000,
                params, UrlAddr.BROWSE_LIST,this);
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();
        ly_pull_refresh = findViewById(R.id.ly_pull_refresh);
        ly_pull_refresh.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        ly_pull_refresh.setProgressViewOffset(true, -20, 100);
        ly_pull_refresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);
        recyclerView = (RecyclerView) findViewById(R.id.rc_Attent);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<BrowseModel.DataBean> list = new ArrayList();
        adapter = new BrowseAdapter(R.layout.item_browse,list);
        View empty = LayoutInflater.from(this).inflate(R.layout.layout_empty,null,false);
        adapter.setEmptyView(empty);
        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1));

        ll_Select = findViewById(R.id.ll_Select);
        ll_Select.setVisibility(View.GONE);
        findViewById(R.id.tv_DelAll).setOnClickListener(this);
        findViewById(R.id.tv_SelectAll).setOnClickListener(this);
        findViewById(R.id.tv_remove).setOnClickListener(this);
    }

    public void editor(boolean isEditor){
        if (!isEditor){
            List<BrowseModel.DataBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                BrowseModel.DataBean dataBean = data.get(i);
                dataBean.setCheck(false);
            }
        }
        adapter.setEditor(isEditor);
        ll_Select.setVisibility(isEditor?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        List<BrowseModel.DataBean> data = adapter.getData();
        switch (v.getId()){
            case R.id.tv_DelAll:{
                String userId = ShareHelper.getUserId();
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                OkHttpHelper.getAsyncHttp(this,1002,
                        params, UrlAddr.BROWSE_REMOVE_REMOVEALL,this);
            }


                break;

            case R.id.tv_SelectAll:

                for (int i = 0; i < data.size(); i++) {
                    BrowseModel.DataBean dataBean = data.get(i);
                    boolean check = dataBean.isCheck();
                    dataBean.setCheck(!check);
                }
                adapter.notifyDataSetChanged();

                break;
            case R.id.tv_remove:
                StringBuffer buffer = new StringBuffer();
                int con = 0;
                for (int i = 0; i < data.size(); i++) {
                    BrowseModel.DataBean dataBean = data.get(i);
                    boolean check = dataBean.isCheck();
                    if(check){
                        int id = dataBean.getId();
                        if (con == 0 ){
                            buffer.append(id);
                        }else {
                            buffer.append(",");
                            buffer.append(id);
                        }
                        con++;
                    }
                }
                if (!TextUtils.isEmpty(buffer.toString())){
                    String userId = ShareHelper.getUserId();
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", userId);
                    params.put("newsids",buffer.toString());
                    OkHttpHelper.getAsyncHttp(this,1001,
                            params, UrlAddr.BROWSE_REMOVE_SELECT,this);
                }

                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        ly_pull_refresh.setRefreshing(false);
        switch (requestCode){
            case 1000:
                BrowseModel browseModel = JSON.parseObject(returnData, BrowseModel.class);
                List<BrowseModel.DataBean> data = browseModel.getData();
                if (data !=null){
                    if (page == 1) {
                        if (data.size() < size) {
                            adapter.setNewData(data);
                            adapter.loadMoreEnd();
                        } else {
                            adapter.setNewData(data);
                            adapter.loadMoreComplete();
                        }
                    } else {
                        if (data.size() < size) {
                            adapter.addData(data);
                            adapter.loadMoreEnd();
                        } else {
                            adapter.addData(data);
                            adapter.loadMoreComplete();
                        }
                    }
                }

                break;
            case 1001:
                page = 1;
                initdata();
                break;
            case 1002:
                page = 1;
                initdata();
                ll_Select.setVisibility(View.GONE);
                BrowseRightTopBtnFunc topFunc = (BrowseRightTopBtnFunc) funcMap.get(R.id.editor);
                topFunc.getTextView().setText("编辑");
                break;
        }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        super.onErrorResult(errorCode, errorExcep);
        ly_pull_refresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BrowseAdapter browseAdapter = (BrowseAdapter) adapter;
        List<BrowseModel.DataBean> data = browseAdapter.getData();
        BrowseModel.DataBean dataBean = data.get(position);
        int id = dataBean.getId();
        String title = dataBean.getTitle();
        String content = dataBean.getContent();
        String focus = dataBean.getFocus();
        ActivityUtils.startDetailsActivity(this,
                String.valueOf(id),
                title,content,focus);
    }

    @Override
    public void onRefresh() {
        page = 1;
        initdata();
        Log.e("TAG_下拉刷新", "onRefresh");
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        initdata();
        Log.e("TAG_上拉加载", "onLoadMoreRequested");
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BrowseAdapter browseAdapter = (BrowseAdapter) adapter;
        List<BrowseModel.DataBean> data = browseAdapter.getData();
        if (data != null && data.size() > 0) {
            switch (view.getId()){
                case R.id.ll_Select:
                    BrowseModel.DataBean dataBean = data.get(position);
                    boolean check = dataBean.isCheck();
                    dataBean.setCheck(!check);
                    browseAdapter.notifyItemChanged(position);
                    break;
            }
        }
    }
}

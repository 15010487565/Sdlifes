package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.PostListAdapter;
import com.sdlifes.sdlifes.model.PostListModel;
import com.sdlifes.sdlifes.network.UrlAddr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.NoTitleActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.http.HttpInterface;

public class PostListActivity extends NoTitleActivity implements View.OnClickListener,

        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        HttpInterface {

    private SwipeRefreshLayout ly_pull_refresh;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PostListAdapter adapter;

    private int page = 1;
    private int size = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        TextView topbar_title = findViewById(R.id.topbar_title);
        topbar_title.setText(title);

        ly_pull_refresh = findViewById(R.id.ly_pull_refresh);
        ly_pull_refresh.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        ly_pull_refresh.setProgressViewOffset(true, -20, 100);
        ly_pull_refresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);

        recyclerView = (RecyclerView) findViewById(R.id.rc_post_list);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new PostListAdapter(R.layout.item_postlist, null);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1));

        findViewById(R.id.topbar_func_icon).setOnClickListener(this);

        initData();
    }

    private void initData() {
        String topicid = getIntent().getStringExtra("topicid");
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        params.put("id", topicid);
        OkHttpHelper.getAsyncHttp(
                this, 1000, params,
                UrlAddr.TOPIC_ARRAY, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topbar_func_icon:
                finish();
                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        ly_pull_refresh.setRefreshing(false);
        switch (requestCode) {
            case 1000:
                PostListModel postListModel = JSON.parseObject(returnData, PostListModel.class);
                List<PostListModel.DataBean> data = postListModel.getData();

                if (data != null) {

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
        }

    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        ly_pull_refresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        page = 1;
        initData();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        initData();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}

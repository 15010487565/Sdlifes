package com.sdlifes.sdlifes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.NoScrollGridView;
import com.sdlifes.sdlifes.adapter.PostMoreAdapter;
import com.sdlifes.sdlifes.model.PostMoreModel;
import com.sdlifes.sdlifes.network.UrlAddr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;

public class PostMoreActivity extends SimpleTopbarActivity 
//        implements
//        BaseQuickAdapter.RequestLoadMoreListener,
//        SwipeRefreshLayout.OnRefreshListener,
//        BaseQuickAdapter.OnItemClickListener 
{

    //    private SwipeRefreshLayout ly_pull_refresh;
//    private RecyclerView recyclerView;
//    private LinearLayoutManager layoutManager;
//    private PostMoreAdapter adapter;
    private NoScrollGridView rcTopicArr;
    private PostMoreAdapter postMoreAdapter;
    private int page = 1;
    private int size = 2000;

    @Override
    protected Object getTopbarTitle() {
        return "选择话题";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_selcet);
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)){
            resetTopbarTitle(title);
        }
    }

    @Override
    protected void afterSetContentView() {
        super.afterSetContentView();

        rcTopicArr = (NoScrollGridView) findViewById(R.id.nsgv_topicArr);
        postMoreAdapter = new PostMoreAdapter(this);
        rcTopicArr.setAdapter(postMoreAdapter);
        rcTopicArr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<PostMoreModel.DataBean> data = postMoreAdapter.getData();
                PostMoreModel.DataBean dataBean = data.get(i);
                int topicid = dataBean.getId();
                Intent intent = new Intent();
                intent.putExtra("topicid", String.valueOf(topicid));
                intent.putExtra("title", dataBean.getTitle());
                setResult(RESULT_OK, intent);
                finish();
                //
            }
        });
//        ly_pull_refresh = findViewById(R.id.ly_pull_refresh);
//        ly_pull_refresh.setOnRefreshListener(this);
//        //设置样式刷新显示的位置
//        ly_pull_refresh.setProgressViewOffset(true, -20, 100);
//        ly_pull_refresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);

//        recyclerView = (RecyclerView) findViewById(R.id.rc_selcet_post);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

//        adapter = new PostMoreAdapter(null);
//        adapter.setOnLoadMoreListener(this);
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(this);
//        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1));

        initData();
    }

    private void initData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        OkHttpHelper.getAsyncHttp(
                this, 1000, params,
                UrlAddr.TOPIC_LIST, this);
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
//        ly_pull_refresh.setRefreshing(false);
        switch (requestCode) {
            case 1000:
                PostMoreModel postMoreModel = JSON.parseObject(returnData, PostMoreModel.class);
                List<PostMoreModel.DataBean> data = postMoreModel.getData();

                if (data != null) {
                    postMoreAdapter.setData(data);
//                    if (page == 1) {
//                        if (data.size() < size) {
//                            adapter.setNewData(data);
//                            adapter.loadMoreEnd();
//                        } else {
//                            adapter.setNewData(data);
//                            adapter.loadMoreComplete();
//                        }
//                    } else {
//                        if (data.size() < size) {
//                            adapter.addData(data);
//                            adapter.loadMoreEnd();
//                        } else {
//                            adapter.addData(data);
//                            adapter.loadMoreComplete();
//                        }
//                    }
                }

                break;
        }

    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
//        ly_pull_refresh.setRefreshing(false);
    }

//    @Override
//    public void onRefresh() {
//        page = 1;
//        initData();
//    }
//
//    @Override
//    public void onLoadMoreRequested() {
//        page++;
//        initData();
//    }
//
//    @Override
//    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//    }
}

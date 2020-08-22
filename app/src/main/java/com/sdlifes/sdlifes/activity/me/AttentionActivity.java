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
import com.sdlifes.sdlifes.adapter.AttentionAdapter;
import com.sdlifes.sdlifes.func.AttentionRightTopBtnFunc;
import com.sdlifes.sdlifes.model.AttentionModel;
import com.sdlifes.sdlifes.network.Constant;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.util.ActivityUtils;
import com.sdlifes.sdlifes.util.EventBusMsg;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.base.activity.SimpleTopbarActivity;
import www.xcd.com.mylibrary.help.OkHttpHelper;
import www.xcd.com.mylibrary.utils.ShareHelper;

/**
 * 2020年8月11日16:10:41
 * 关注
 */
public class AttentionActivity extends SimpleTopbarActivity implements
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener ,
        BaseQuickAdapter.OnItemChildClickListener{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AttentionAdapter adapter;
    private SwipeRefreshLayout ly_pull_refresh;
    private LinearLayout ll_Select;

    private int size = 10;
    private int page = 1;
    @Override
    protected Object getTopbarTitle() {
        return "我的关注";
    }

    private static Class<?> rightFuncArray[] = {AttentionRightTopBtnFunc.class};

    @Override
    protected Class<?>[] getTopbarRightFuncArray() {
        return rightFuncArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        ly_pull_refresh.setRefreshing(true);
        initdata();

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

//        List<AttentionModel.DataBean> list = new ArrayList();
        adapter = new AttentionAdapter(R.layout.item_attenton,null);
        View empty = LayoutInflater.from(this).inflate(R.layout.layout_empty,null,false);
        adapter.setEmptyView(empty);
        adapter.setOnLoadMoreListener(this);

        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1));

        ll_Select = findViewById(R.id.ll_Select);
        ll_Select.setVisibility(View.GONE);
        findViewById(R.id.tv_All).setOnClickListener(this);
        findViewById(R.id.tv_remove).setOnClickListener(this);
    }

    private void initdata() {
        String userId = ShareHelper.getUserId();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userid", userId);
        params.put("page", String.valueOf(page));
        params.put("size", String.valueOf(size));
        OkHttpHelper.getAsyncHttp(this,1000,
                params, UrlAddr.ATTENTION_LIST,this);
    }

    public void editor(boolean isEditor){
        if (!isEditor){
            List<AttentionModel.DataBean> data = adapter.getData();
            for (int i = 0; i < data.size(); i++) {
                AttentionModel.DataBean dataBean = data.get(i);
                dataBean.setCheck(false);
            }
        }
        adapter.setEditor(isEditor);
        ll_Select.setVisibility(isEditor?View.VISIBLE:View.GONE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        List<AttentionModel.DataBean> data = adapter.getData();
        switch (v.getId()){
            case R.id.tv_All:

                for (int i = 0; i < data.size(); i++) {
                    AttentionModel.DataBean dataBean = data.get(i);
                    boolean check = dataBean.isCheck();
                    dataBean.setCheck(!check);
                }
                adapter.notifyDataSetChanged();

                break;
            case R.id.tv_remove:
               StringBuffer buffer = new StringBuffer();
               int con = 0;
                for (int i = 0; i < data.size(); i++) {
                    AttentionModel.DataBean dataBean = data.get(i);
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
                            params, UrlAddr.ATTENTION_REMOVE_All,this);
                }

                break;
        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        ly_pull_refresh.setRefreshing(false);
        switch (requestCode){
            case 1000:
                AttentionModel attentionModel = JSON.parseObject(returnData, AttentionModel.class);
                List<AttentionModel.DataBean> data = attentionModel.getData();
                if (data !=null){
//                    for (int i = 0; i < data.size(); i++) {
//                        AttentionModel.DataBean dataBean = data.get(i);
//                        List<String> pic = dataBean.getPic();
//                        String s = pic.get(0);
//                        if (i%3 == 1){
//                            pic.add(s);
//                            pic.add(s);
//                        }else if (i%3 == 2){
//                            pic.add(s);
//                        }
//                        dataBean.setPic(pic);
//                    }
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

        }
    }

    @Override
    public void onErrorResult(int errorCode, String errorExcep) {
        super.onErrorResult(errorCode, errorExcep);
        ly_pull_refresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        AttentionAdapter attentionAdapter = (AttentionAdapter) adapter;
        List<AttentionModel.DataBean> data = attentionAdapter.getData();
        AttentionModel.DataBean dataBean = data.get(position);
        int id = dataBean.getId();
        String title = dataBean.getTitle();
        String content = dataBean.getContent();
        String focus = dataBean.getFocus();
        ActivityUtils.startDetailsActivity(this,
                String.valueOf(id),
                title,content,focus);
    }
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        AttentionAdapter attentionAdapter = (AttentionAdapter) adapter;
        List<AttentionModel.DataBean> data = attentionAdapter.getData();
        if (data != null && data.size() > 0) {
                switch (view.getId()){
                case R.id.ll_Select:
                    AttentionModel.DataBean dataBean = data.get(position);
                    boolean check = dataBean.isCheck();
                    dataBean.setCheck(!check);
                    attentionAdapter.notifyItemChanged(position);
                    break;
            }
        }
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventBusMsg event) {
        String msg = event.getMsg();
        Log.e("tag_","shuaxin="+msg);
        if (Constant.REFRESH_ATTENTION.equals(msg)){
            page = 1;
            initdata();
        }
    }
}

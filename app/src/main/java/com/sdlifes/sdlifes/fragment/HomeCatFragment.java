package com.sdlifes.sdlifes.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.PostListActivity;
import com.sdlifes.sdlifes.adapter.HomeAdapter;
import com.sdlifes.sdlifes.adapter.NoScrollGridView;
import com.sdlifes.sdlifes.adapter.TopicArrGridAdapter;
import com.sdlifes.sdlifes.listener.AppBarStateChangeListener;
import com.sdlifes.sdlifes.model.HomeCatModel;
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
        BaseQuickAdapter.OnItemClickListener ,
        SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout ly_pull_refresh;
    private NoScrollGridView rcTopicArr;
    private TopicArrGridAdapter topicArrGridAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private HomeAdapter adapter;
    private String titleId;
    private int page = 1;
    private int size = 10;
    private boolean isOpen = false;//
    AppBarLayout appBarLayout;
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

        ly_pull_refresh = view.findViewById(R.id.ly_pull_refresh);
        ly_pull_refresh.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        ly_pull_refresh.setProgressViewOffset(true, -20, 100);
        ly_pull_refresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);


        rcTopicArr = (NoScrollGridView) view.findViewById(R.id.nsgv_topicArr);
        topicArrGridAdapter = new TopicArrGridAdapter(getActivity());
        rcTopicArr.setAdapter(topicArrGridAdapter);
        rcTopicArr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<HomeCatModel.DataBean.TopicArrBean> data = topicArrGridAdapter.getData();
                HomeCatModel.DataBean.TopicArrBean topicArrBean = data.get(i);
                int id = topicArrBean.getId();
                String title = topicArrBean.getTitle();
                Intent intent = new Intent(getActivity(), PostListActivity.class);
                intent.putExtra("topicid", String.valueOf(id));
                intent.putExtra("title", title);
                startActivity(intent);
                //
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.rc_Home_news);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        List<HomeCatModel.DataBean.NewsArrBean> list = new ArrayList();
        adapter = new HomeAdapter(list);
        adapter.setOnLoadMoreListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerViewListener());
        adapter.setOnItemClickListener(this);
        recyclerView.addItemDecoration(getRecyclerViewDivider(R.drawable.inset_recyclerview_divider_1));

        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.e("STATE", state.name());
                if (state == State.EXPANDED) {
                    //展开状态
                    isOpen = true;
                    ly_pull_refresh.setEnabled(true);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    isOpen = false;
                    ly_pull_refresh.setEnabled(false);
                } else {
                    //中间状态
                    isOpen = false;
                    ly_pull_refresh.setEnabled(false);
                }
            }
        });

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
        ly_pull_refresh.setRefreshing(false);
        switch (requestCode) {
            case 1000:
                HomeCatModel homeModel = JSON.parseObject(returnData, HomeCatModel.class);
                HomeCatModel.DataBean data = homeModel.getData();

                List<HomeCatModel.DataBean.NewsArrBean> newsArr = data.getNewsArr();
                if (data != null) {
                    List<HomeCatModel.DataBean.TopicArrBean> topicArr = data.getTopicArr();
                    topicArrGridAdapter.setData(topicArr);
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
        ly_pull_refresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        HomeAdapter homeAdapter = (HomeAdapter) adapter;
        List<HomeCatModel.DataBean.NewsArrBean> data = homeAdapter.getData();
        HomeCatModel.DataBean.NewsArrBean newsArrBean = data.get(position);
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
                List<String> pic = newsArrBean.getPic();
                String url = "";
                if (pic != null && pic.size() > 0) {
                    url = pic.get(0);
                }
                ActivityUtils.startWebViewVideoActivity(getActivity(), newsArrBean.getUrl(),newsArrBean.getTitle()
                        ,String.valueOf(newsArrBean.getId()),newsArrBean.getOstate(),newsArrBean.getOurl(),url);

            }else {
                ActivityUtils.startWebViewActivity(getActivity(), newsArrBean.getUrl()
                        ,String.valueOf(newsArrBean.getId()));

            }
        }

    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerViewB, int newState) {
            super.onScrollStateChanged(recyclerViewB, newState);

            //表示是否能向上滚动，true表示能滚动，false表示已经滚动到底部
            boolean canScrollVertically = recyclerViewB.canScrollVertically(1);
//            Log.e("TAG_首页上滑", "canScrollVertically=" + canScrollVertically + ";direction=" + direction);
            if (!canScrollVertically) {
                appBarLayout.setExpanded(false, true);
                return;
            }
            //表示是否能向下滚动，true表示能滚动，false表示已经滚动到顶部
            boolean scrollVertically = recyclerViewB.canScrollVertically(-1);

//            Log.e("TAG_首页下滑", "scrollVertically=" + scrollVertically + ";isOpen=" + isOpen);
            if (!isOpen && !scrollVertically && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    //expanded true:展开  false:收缩
                    appBarLayout.setExpanded(true);
                    return;

            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerViewB, int dx, int dy) {
            super.onScrolled(recyclerViewB, dx, dy);

        }
    }

    @Override
    public void onLoadMoreRequested() {
//        Log.e("TAG_","加载更多");
        page++;
        initData();
    }
    @Override
    public void onRefresh() {
//        Log.e("TAG_","onRefresh");
        page = 1;
        initData();
    }
}

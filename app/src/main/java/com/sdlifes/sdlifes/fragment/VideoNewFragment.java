package com.sdlifes.sdlifes.fragment;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.adapter.VideoNewAdapter;
import com.sdlifes.sdlifes.application.SimpleTopbarFragment;
import com.sdlifes.sdlifes.model.VideoModel;
import com.sdlifes.sdlifes.network.UrlAddr;
import com.sdlifes.sdlifes.video.SampleCoverVideo;
import com.sdlifes.sdlifes.view.OnViewPagerListener;
import com.sdlifes.sdlifes.view.VideoLinearLayoutManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import www.xcd.com.mylibrary.help.OkHttpHelper;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;


/**
 * Created by gs on 2018/10/16.
 */

public class VideoNewFragment extends SimpleTopbarFragment implements
        BaseQuickAdapter.RequestLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener {

    private RecyclerView videoRc;
    private VideoLinearLayoutManager mLinearLayoutManager;
    private VideoNewAdapter adapter;

    private SwipeRefreshLayout ly_pull_refresh;
    private int size = 10 ;
    private int page = 1 ;

    private boolean isPrepared;

    @Override
    protected Object getTopbarTitle() {
        return R.string.video;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }



    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        //填充各控件的数据
        initData();
    }

    private void initData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("count", String.valueOf(size));
        OkHttpHelper.getAsyncHttp(getActivity(),1000,
                params, UrlAddr.VIDEO,this);
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

        view.findViewById(R.id.topbat_parent).setVisibility(View.GONE);

        ly_pull_refresh = view.findViewById(R.id.ly_pull_refresh);
        ly_pull_refresh.setOnRefreshListener(this);
        //设置样式刷新显示的位置
        ly_pull_refresh.setProgressViewOffset(true, -20, 100);
        ly_pull_refresh.setColorSchemeResources(R.color.red, R.color.blue, R.color.black);
        videoRc = view.findViewById(R.id.video_recycler);
        mLinearLayoutManager = new VideoLinearLayoutManager(getActivity(), OrientationHelper.VERTICAL,false);
        mLinearLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {

                GSYVideoManager.onPause();
            }

            @Override
            public void onPageSelected(int position, boolean isNext) {

            }
        });
        videoRc.setLayoutManager(mLinearLayoutManager);

        adapter = new VideoNewAdapter(R.layout.item_video_new_fragment,null);
        View empty = LayoutInflater.from(getActivity()).inflate(R.layout.layout_empty,null,false);
        adapter.setEmptyView(empty);
        adapter.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
        videoRc.setAdapter(adapter);

        videoRc.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE ) {
                    // DES: 找出当前可视Item位置
                    RecyclerView.LayoutManager layoutManager = videoRc.getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        int mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                        int mLastVisiblePosition = linearManager.findLastVisibleItemPosition();
                        Log.e("TAG_resume","mFirstVisiblePosition="+mFirstVisiblePosition+
                                ";mLastVisiblePosition="+mLastVisiblePosition);
                        View view = mLinearLayoutManager.findViewByPosition(mFirstVisiblePosition);
                        SampleCoverVideo gsyVideoPlayer = view.findViewById(R.id.video_item_player);
                        gsyVideoPlayer.startPlayLogic();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        isPrepared = true;
        lazyLoad();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {


        }
    }

    @Override
    public void onSuccessResult(int requestCode, int returnCode, String returnMsg, String returnData, Map<String, String> paramsMaps) {
        ly_pull_refresh.setRefreshing(false);
       switch (requestCode){
           case 1000:
               VideoModel videoModel = JSON.parseObject(returnData, VideoModel.class);
               List<VideoModel.DataBean> data = videoModel.getData();
               if (data !=null){
                   if (page == 1){
                       adapter.setNewData(data);
                       adapter.loadMoreComplete();
                   }else {
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

    /**
     * PrepareView被点击
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SampleCoverVideo gsyVideoPlayer = view.findViewById(R.id.video_item_player);
        Log.e("TAG_resume","mLastPos="+position);

//        gsyVideoPlayer.startPlayLogic();
    }

    @Override
    public void onRefresh() {
        page = 1;
        initData();
        Log.e("TAG_下拉刷新", "onRefresh");
    }

    @Override
    public void onLoadMoreRequested() {
        page ++;
        initData();
        Log.e("TAG_上拉加载", "onLoadMoreRequested");
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
//        if (adapter != null) {
//            adapter.onDestroy();
//        }
    }

}

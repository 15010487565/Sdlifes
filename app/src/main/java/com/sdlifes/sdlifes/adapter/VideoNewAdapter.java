package com.sdlifes.sdlifes.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.VideoModel;
import com.sdlifes.sdlifes.video.SampleCoverVideo;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.util.List;

/**
 * Created by gs on 2020/8/21.
 */
public class VideoNewAdapter extends BaseQuickAdapter<VideoModel.DataBean, BaseViewHolder> {

    public VideoNewAdapter(int layoutResId, @Nullable List<VideoModel.DataBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, VideoModel.DataBean dataBean) {
        SampleCoverVideo gsyVideoPlayer = helper.getView(R.id.video_item_player);
//        ImageView view = mPrepareView.findViewById(R.id.thumb);
//        ImageUtils.setImage(view,"http://cdn.gaifan.cn/162/154/1589435996234.jpg");
        //使用lazy的set可以避免滑动卡的情况存在
        gsyVideoPlayer.setUpLazy(dataBean.getUrl(), true, null, null, "");
        gsyVideoPlayer.loadCoverImage(dataBean.getUrl(), R.mipmap.album_photo_default);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        int adapterPosition = helper.getAdapterPosition();
        Log.e("TAG_点击播放","adapterPosition="+adapterPosition);
        if(adapterPosition==0){
            gsyVideoPlayer.startPlayLogic();
        }

        final int[] flag = {0};
        gsyVideoPlayer.getStartButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("TAG_点击播放","onClick="+flag[0]);
                //需要切换缓存路径的
//                gsyVideoPlayer.setUp(url, true, new File(FileUtils.getTestPath(), ""));
                if (flag[0] == 0 ){
                    gsyVideoPlayer.startPlayLogic();
                    flag[0] = 1;
                }else if (flag[0] == 1 ){
                    GSYVideoManager.onPause();
                    flag[0] = 2;
                }else {
                    GSYVideoManager.onResume();
                    flag[0] = 1;
                }

            }
        });

    }
}
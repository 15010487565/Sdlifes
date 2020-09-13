package com.sdlifes.sdlifes.adapter;


import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.PostDetailsActivity;
import com.sdlifes.sdlifes.model.HomeCatModel;
import com.sdlifes.sdlifes.util.ActivityUtils;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.List;

import www.xcd.com.mylibrary.help.HelpUtils;

/**
 * Created by gs on 21/11/2018.
 */

public class HomeAdapter extends BaseQuickAdapter<HomeCatModel.DataBean.NewsArrBean, BaseViewHolder> {

    public HomeAdapter(List<HomeCatModel.DataBean.NewsArrBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<HomeCatModel.DataBean.NewsArrBean>() {
            @Override
            protected int getItemType(HomeCatModel.DataBean.NewsArrBean newsArrBean) {
                String type = newsArrBean.getType();
                if ("2".equals(type)) {//广告
                    return 2;
                } if ("3".equals(type)) {//话题
                    List<String> pic = newsArrBean.getPic();
                    if (pic.size() > 1) {
                        return 33;//话题多张图片
                    } else {
                        return 31;//话题一张图片
                    }
                } else {
                    List<String> pic = newsArrBean.getPic();
                    if (pic.size() > 1) {
                        return 13;//新闻多张图片
                    } else {
                        return 11;//新闻一张图片
                    }
                }

            }
        });
        getMultiTypeDelegate()
                .registerItemType(2, R.layout.item_ad)//广告
                .registerItemType(11, R.layout.item_home_adapter_single)//新闻一张
                .registerItemType(13, R.layout.item_home_adapter_more)//新闻多张
                .registerItemType(31, R.layout.item_home_adapter_single)//话题一张
                .registerItemType(33, R.layout.item_home_adapter_more)//话题多张
        ;
    }


    @Override
    protected void convert(BaseViewHolder helper, HomeCatModel.DataBean.NewsArrBean newsArrBean) {

        /**
         * type = 1 新闻 单张
         * type = 3 新闻 多张
         * type = 2广告
         */
        List<String> pic = newsArrBean.getPic();
        int viewType = helper.getItemViewType();
        switch (viewType) {

            case 11:{
                ImageView ivHome = helper.getView(R.id.iv_Home);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivHome.getLayoutParams();
                //设置宽度值
                int widthPixe = ImageUtils.getWidthPixel();
                int margin = HelpUtils.imageDip2px(mContext, 45);
                params.width = (int) ((widthPixe - margin) * 0.333333);
                Log.e("TAG_单张图片", "width=" + params.width);
                params.height = (int) (params.width * 0.666666);
                ivHome.setLayoutParams(params);
                ImageUtils.setImage(ivHome, pic.get(0), 0, R.mipmap.album_photo_default);

                TextView titleV = helper.getView(R.id.tv_title);
                LinearLayout.LayoutParams paramsV = (LinearLayout.LayoutParams) titleV.getLayoutParams();
                paramsV.width = (int) ((widthPixe - margin) * 0.666666);

                titleV.setLayoutParams(paramsV);
                titleV.setText(newsArrBean.getTitle());

                helper.setText(R.id.tv_src, newsArrBean.getSrc());
                helper.setText(R.id.tv_time, newsArrBean.getTime());
            }


                break;
            case 13:{
                helper.setText(R.id.tv_title, newsArrBean.getTitle());

                NoScrollGridView noScrollGridView = helper.getView(R.id.nsgv);
                noScrollGridView.setNumColumns(pic.size() >= 3 ? 3 : pic.size());
                GridAdapter gridAdapter = new GridAdapter(mContext);
                noScrollGridView.setAdapter(gridAdapter);
                onClick(newsArrBean, noScrollGridView);
                gridAdapter.setData(pic);
                helper.setText(R.id.tv_src, newsArrBean.getSrc());
                helper.setText(R.id.tv_time, newsArrBean.getTime());
            }

                break;
            case 31:{
                ImageView ivHome = helper.getView(R.id.iv_Home);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivHome.getLayoutParams();
                //设置宽度值
                int widthPixe = ImageUtils.getWidthPixel();
                int margin = HelpUtils.imageDip2px(mContext, 45);
                params.width = (int) ((widthPixe - margin) * 0.333333);
                Log.e("TAG_单张图片", "width=" + params.width);
                params.height = (int) (params.width * 0.666666);
                ivHome.setLayoutParams(params);
                ImageUtils.setImage(ivHome, pic.get(0), 0, R.mipmap.album_photo_default);

                TextView titleV = helper.getView(R.id.tv_title);
                LinearLayout.LayoutParams paramsV = (LinearLayout.LayoutParams) titleV.getLayoutParams();
                paramsV.width = (int) ((widthPixe - margin) * 0.666666);

                titleV.setLayoutParams(paramsV);
                titleV.setText(newsArrBean.getTopic());

                helper.setText(R.id.tv_src, newsArrBean.getSrc());
                helper.setText(R.id.tv_time, newsArrBean.getTime());

            }

                break;
            case 33:{
                helper.setText(R.id.tv_title, newsArrBean.getTopic());

                NoScrollGridView noScrollGridView = helper.getView(R.id.nsgv);
                noScrollGridView.setNumColumns(pic.size() >= 3 ? 3 : pic.size());
                GridAdapter gridAdapter = new GridAdapter(mContext);
                noScrollGridView.setAdapter(gridAdapter);
                onClick(newsArrBean, noScrollGridView);
                gridAdapter.setData(pic);
                helper.setText(R.id.tv_src, newsArrBean.getSrc());
                helper.setText(R.id.tv_time, newsArrBean.getTime());
            }

                break;
            case 2: {
                helper.setText(R.id.tv_title, newsArrBean.getTitle());

                NoScrollGridView noScrollGridView = helper.getView(R.id.nsgv);
                noScrollGridView.setNumColumns(pic.size() >= 3 ? 3 : pic.size());
                GridAdapter gridAdapter = new GridAdapter(mContext);
                noScrollGridView.setAdapter(gridAdapter);
                onClick(newsArrBean, noScrollGridView);
                gridAdapter.setData(pic);
                //0不置顶;1置顶
                String top = newsArrBean.getTop();
                TextView view = helper.getView(R.id.tv_top);
                if ("1".equals(top)){
                    view.setTextColor(ContextCompat.getColor(mContext,R.color.red));
                    view.setText("置顶");
                }else {
                    view.setTextColor(ContextCompat.getColor(mContext,R.color.black_66));
                    view.setText("广告");
                }
                helper.setText(R.id.tv_time, newsArrBean.getTime());
                break;
            }


        }

    }

    private void onClick(HomeCatModel.DataBean.NewsArrBean newsArrBean, NoScrollGridView noScrollGridView) {
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //	type = 1 新闻 type = 2广告
                String type = newsArrBean.getType();
                if ("1".equals(type)) {
                    int id = newsArrBean.getId();
                    String title = newsArrBean.getTitle();
                    String content = newsArrBean.getContent();
                    String focus = newsArrBean.getFocus();
                    ActivityUtils.startDetailsActivity(mContext,
                            String.valueOf(id),
                            title, content, focus);
                } else if ("2".equals(type)) {
                    int state = newsArrBean.getState();//	广告 ： 1 网页url地址 2 视频url地址
                    if (state == 2){
                        List<String> pic = newsArrBean.getPic();
                        String url = "";
                        if (pic != null && pic.size() > 0) {
                            url = pic.get(0);
                        }
                        ActivityUtils.startWebViewVideoActivity(mContext, newsArrBean.getUrl(),newsArrBean.getTitle()
                                ,String.valueOf(newsArrBean.getId()),newsArrBean.getOstate(),newsArrBean.getOurl(),url);

                    }else {
                        ActivityUtils.startWebViewActivity(mContext, newsArrBean.getUrl()
                                ,String.valueOf(newsArrBean.getId()));

                    }
                }else {
                    String content = newsArrBean.getContext();
                    String topic = newsArrBean.getTopic();
                    Intent intent = new Intent(mContext, PostDetailsActivity.class);
                    intent.putExtra("title",topic);
                    intent.putExtra("content",content);
                    mContext.startActivity(intent);
                }

            }
        });
    }
}


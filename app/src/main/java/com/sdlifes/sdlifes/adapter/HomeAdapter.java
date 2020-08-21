package com.sdlifes.sdlifes.adapter;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.WebViewActivity;
import com.sdlifes.sdlifes.model.HomeModel;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.util.List;

/**
 * Created by gs on 21/11/2018.
 */

public class HomeAdapter extends BaseQuickAdapter<HomeModel.DataBean.NewsArrBean, BaseViewHolder> {

    public HomeAdapter(List<HomeModel.DataBean.NewsArrBean> data) {
        super(data);
        setMultiTypeDelegate(new MultiTypeDelegate<HomeModel.DataBean.NewsArrBean>() {
            @Override
            protected int getItemType(HomeModel.DataBean.NewsArrBean newsArrBean) {

                return Integer.valueOf(newsArrBean.getType());
            }
        });
        getMultiTypeDelegate()
                .registerItemType(2, R.layout.item_ad)//广告
                .registerItemType(1, R.layout.item_home_adapter)//新闻

        ;
    }


    @Override
    protected void convert(BaseViewHolder helper, HomeModel.DataBean.NewsArrBean newsArrBean) {

        helper.setText(R.id.tv_title, newsArrBean.getTitle());
        //	type = 1 新闻 type = 2广告
//        int viewType = helper.getItemViewType();
//        switch (viewType) {
//
//            case 1://新闻
//
//                break;
//            case 2://广告
//
//                break;
//
//        }
        List<String> pic = newsArrBean.getPic();
        NoScrollGridView noScrollGridView = helper.getView(R.id.nsgv);
        noScrollGridView.setNumColumns(pic.size()>= 3 ? 3 : pic.size());
        GridAdapter gridAdapter = new GridAdapter(mContext);
        noScrollGridView.setAdapter(gridAdapter);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //	type = 1 新闻 type = 2广告
                String type = newsArrBean.getType();
                if("1".equals(type)){
                    int id = newsArrBean.getId();
                    String title = newsArrBean.getTitle();
                    String content = newsArrBean.getContent();
                    String focus = newsArrBean.getFocus();
                    ActivityUtils.startDetailsActivity(mContext,
                            String.valueOf(id),
                            title, content, focus);
                }else {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("Url",newsArrBean.getUrl());
                    mContext.startActivity(intent);
                }

            }
        });
        gridAdapter.setData(pic);

    }
}


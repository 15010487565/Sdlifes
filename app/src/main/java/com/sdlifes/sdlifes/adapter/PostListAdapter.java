package com.sdlifes.sdlifes.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.PostListModel;

import java.util.List;


/**
 * Created by gs on 2017/12/26.
 */

public class PostListAdapter extends BaseQuickAdapter<PostListModel.DataBean, BaseViewHolder> {

    public PostListAdapter(int layoutResId, @Nullable List<PostListModel.DataBean> data) {
        super(layoutResId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PostListModel.DataBean dataBean) {

        helper.setText(R.id.tv_title,dataBean.getContext());


        NoScrollGridView noScrollGridView = helper.getView(R.id.nsgv);
        GridAdapter gridAdapter = new GridAdapter(mContext);
        noScrollGridView.setAdapter(gridAdapter);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                int id = dataBean.getId();
//                String title = dataBean.getTitle();
//                String content = dataBean.getContent();
//                String focus = dataBean.getFocus();
//                ActivityUtils.startDetailsActivity(mContext,
//                        String.valueOf(id),
//                        title,content,focus);
            }
        });

        List<String> pic = dataBean.getPicArr();
        noScrollGridView.setNumColumns(pic.size()>= 3 ? 3 : pic.size());
        gridAdapter.setData(pic);

    }
}
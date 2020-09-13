package com.sdlifes.sdlifes.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.PostDetailsActivity;
import com.sdlifes.sdlifes.model.PostListModel;

import java.util.List;


/**
 * Created by gs on 2017/12/26.
 */

public class PostListAdapter extends BaseQuickAdapter<PostListModel.DataBean, BaseViewHolder> {

    private String title;

    public PostListAdapter(int layoutResId, @Nullable List<PostListModel.DataBean> data) {
        super(layoutResId,data);
    }

    public void setTitle(String title) {
        this.title = title;
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
                String content = dataBean.getContext();

                Intent intent = new Intent(mContext, PostDetailsActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("content",content);
                mContext.startActivity(intent);
            }
        });

        List<String> pic = dataBean.getPicArr();
        noScrollGridView.setNumColumns(pic.size()>= 3 ? 3 : pic.size());
        gridAdapter.setData(pic);

    }
}
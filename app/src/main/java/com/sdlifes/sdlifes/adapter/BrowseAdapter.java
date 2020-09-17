package com.sdlifes.sdlifes.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.BrowseModel;
import com.sdlifes.sdlifes.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gs on 2017/12/26.
 */

public class BrowseAdapter extends BaseQuickAdapter<BrowseModel.DataBean, BaseViewHolder> {

    private boolean isEditor;

    public BrowseAdapter(int layoutResId, @Nullable List<BrowseModel.DataBean> data) {
        super(layoutResId,data);
    }

    public void setEditor(boolean isEditor){
        this.isEditor = isEditor;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, BrowseModel.DataBean newsArrBean) {

        helper.setText(R.id.tv_title,newsArrBean.getTitle());


        NoScrollGridView noScrollGridView = helper.getView(R.id.nsgv);
        GridAdapter gridAdapter = new GridAdapter(mContext);
        noScrollGridView.setAdapter(gridAdapter);
        ArrayList<String> pic = newsArrBean.getPic();
        noScrollGridView.setNumColumns(pic.size()>= 3 ? 3 : pic.size());
        gridAdapter.setData(pic);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                int id = newsArrBean.getId();
//                String title = newsArrBean.getTitle();
//                String content = newsArrBean.getContent();
//                String focus = newsArrBean.getFocus();
//                ActivityUtils.startDetailsActivity(mContext,
//                        String.valueOf(id),
//                        title,content,focus);
                ActivityUtils.startImageCheckActivity(mContext,position,pic);
            }
        });

        LinearLayout llSelect= helper.getView(R.id.ll_Select);
        helper.addOnClickListener(R.id.ll_Select);

        ImageView ivSelect= helper.getView(R.id.iv_Select);
        if (isEditor){
            llSelect.setVisibility(View.VISIBLE);
            boolean check = newsArrBean.isCheck();
            if (check){
                ivSelect.setBackgroundResource(R.drawable.check);
            }else {
                ivSelect.setBackgroundResource(R.drawable.uncheck);
            }
        }else {
            llSelect.setVisibility(View.GONE);
        }
    }
}
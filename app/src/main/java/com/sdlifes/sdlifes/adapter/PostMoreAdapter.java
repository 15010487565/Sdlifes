package com.sdlifes.sdlifes.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.PostMoreModel;

import java.util.List;

/**
 * Created by gs on 21/11/2018.
 */

public class PostMoreAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<PostMoreModel.DataBean> list;
    private Context context;

    public PostMoreAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<PostMoreModel.DataBean> getData() {
        return list;
    }

    public void setData(List<PostMoreModel.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_grid_topicarr, null);
            viewHolder = new ViewHolder();
            viewHolder.tvNoGv = (TextView) convertView.findViewById(R.id.tv_topicArr);
            viewHolder.tv_topicArr_New = (TextView) convertView.findViewById(R.id.tv_topicArr_New);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PostMoreModel.DataBean topicArrBean = list.get(position);
        int isnew = topicArrBean.getIsnew();//是否显示new 0 不显示 1显示
        if (isnew == 0) {
            viewHolder.tv_topicArr_New.setVisibility(View.GONE);
        } else {
            viewHolder.tv_topicArr_New.setVisibility(View.VISIBLE);
        }
        viewHolder.tvNoGv.setText("#\t" + topicArrBean.getTitle());

        return convertView;

    }

    class ViewHolder {
        public TextView tvNoGv, tv_topicArr_New;
    }
}
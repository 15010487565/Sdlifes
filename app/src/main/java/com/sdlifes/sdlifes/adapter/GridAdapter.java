package com.sdlifes.sdlifes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.List;

import www.xcd.com.mylibrary.help.HelpUtils;

/**
 * Created by gs on 2018/1/3.
 */

public class GridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<String> list;
    private Context context;

    public GridAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<String> list) {
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
            convertView = mInflater.inflate(R.layout.item_grid_image, null);
            viewHolder = new ViewHolder();
            viewHolder.ivNoGv = (ImageView) convertView.findViewById(R.id.iv_noGv);

            //取控件当前的布局参数
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.ivNoGv.getLayoutParams();

            //设置宽度值
            int widthPixe = ImageUtils.getWidthPixel();

            if (list.size() == 1){//广告
                params.width = widthPixe - HelpUtils.imageDip2px(context,30);
                params.height = (int) (params.width * 0.5);
            }else
                if (list.size() == 2){//新闻
                params.width = (int) ((widthPixe - HelpUtils.imageDip2px(context,35))*  0.5);
                params.height = (int) (params.width * 0.666666);
            }else {//新闻
                params.width = (int) ((widthPixe - HelpUtils.imageDip2px(context,40))*0.333333);
                params.height = (int) ( params.width * 0.66666);
            }

            viewHolder.ivNoGv.setLayoutParams(params);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String url = list.get(position);
//        viewHolder.ivNoGv.setBackgroundResource(R.mipmap.ic_launcher);
        ImageUtils.setImage(viewHolder.ivNoGv, url, 0, R.mipmap.album_photo_default);

        return convertView;

    }

    class ViewHolder {
        public ImageView ivNoGv;
    }
}

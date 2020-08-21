package com.sdlifes.sdlifes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.List;

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
            convertView = mInflater.inflate(R.layout.item_grad_image, null);
            viewHolder = new ViewHolder();
            viewHolder.ivNoGv = (ImageView) convertView.findViewById(R.id.iv_noGv);

            //取控件当前的布局参数
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.ivNoGv.getLayoutParams();

            //设置宽度值
            int widthPixe = ImageUtils.getWidthPixel();

//            Log.e("TAG_SSS","width="+params.width);
            if (list.size() == 1){
                params.width = widthPixe - 80;
                params.height = (int) (params.width * 0.75);
            }else if (list.size() == 2){
                params.width = (int) ((widthPixe - 120)*  0.5);
                params.height = (int) (params.width * 0.75);
            }else {
                params.width = (int) ((widthPixe - 160)*0.333333);
                params.height = (int) ( params.width * 0.75);
            }
//            Log.e("TAG_SSS===",position+"====params="+params.width+"===="+params.height);
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

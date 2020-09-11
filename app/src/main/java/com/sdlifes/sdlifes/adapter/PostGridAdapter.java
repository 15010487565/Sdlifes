package com.sdlifes.sdlifes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.activity.PostActivity;
import com.sdlifes.sdlifes.util.ImageUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by gs on 2018/1/3.
 */

public class PostGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private LayoutInflater inflater;
    private HashMap<Integer, View> viewMap = null;
    /**
     * 最大图片数量，默认三张
     */
    private int MAX_SELECT_PIC_NUM = 9;

    private int width;
    private int height;

    NoScrollGridView gridview;


    public PostGridAdapter(NoScrollGridView gridview, Context mContext) {
        viewMap = new HashMap<Integer, View>();
        this.mContext = mContext;
        this.gridview = gridview;

        inflater = LayoutInflater.from(mContext);
        initMeasure(mContext, 3, 1.0);
    }

    public void setData(List<String> mList) {
//        if (this.mList !=null && this.mList.size() > 0){
//            this.mList.addAll(mList);
//        }else {
            this.mList = mList;
//        }

        notifyDataSetChanged();
    }

    public List<String> getData() {
        return mList;
    }

    /**
     * @param mContext
     * @param width_percentage 宽高比例 默认1:1(宽/高)
     */
    private void initMeasure(Context mContext, int numColumns, double width_percentage) {

        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        this.width = (width - convertDipOrPx(mContext, 15 * (numColumns + 1))) / numColumns;

        this.height = (int) (this.width / width_percentage);

    }

    @Override
    public int getCount() {
        //return mList.size() + 1;//因为最后多了一个添加图片的ImageView
        int count = mList == null ? 1 : mList.size() + 1;
        if (count > MAX_SELECT_PIC_NUM) {
            return mList.size();
        } else {
            return count;
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder = null;
        if (!viewMap.containsKey(position) || viewMap.get(position) != null) {
            mViewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_add_image, parent, false);

            mViewHolder.flImage = (FrameLayout) convertView.findViewById(R.id.fl_image);
            mViewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_gragview);
            mViewHolder.btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);

            convertView.setTag(mViewHolder);
            viewMap.put(position, convertView);
        } else {
            convertView = viewMap.get(position);
            mViewHolder = (ViewHolder) convertView.getTag();
        }


        if (mList != null && position < mList.size()) {

            mViewHolder.flImage.setLayoutParams(new FrameLayout.LayoutParams(width, height));
            mViewHolder.iv.setLayoutParams(new FrameLayout.LayoutParams(width - 4, height - 4));
            //代表+号之前的需要正常显示图片
            final String listDataBean = mList.get(position);
//            final String picUrl = listDataBean.getUrl(); //图片路径
            ImageUtils.setImage(mViewHolder.iv,listDataBean);

            mViewHolder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            mViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            mViewHolder.flImage.setLayoutParams(new FrameLayout.LayoutParams(width, width));
            mViewHolder.iv.setLayoutParams(new FrameLayout.LayoutParams(width - 4, width - 4));
            mViewHolder.iv.setImageResource(R.mipmap.add_image);//最后一个显示加号图片
            mViewHolder.btn_delete.setVisibility(View.GONE);
            mViewHolder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PostActivity) mContext).selectImage();
                }
            });

        }

        return convertView;
    }

    public static int convertDipOrPx(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    private class ViewHolder {
        FrameLayout flImage;
        ImageView iv, btn_delete;
    }
}

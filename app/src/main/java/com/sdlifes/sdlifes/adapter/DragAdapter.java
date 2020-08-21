package com.sdlifes.sdlifes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.ChannelModel;

import java.util.List;

/**
 * Created by gs on 2020/8/13.
 */
public class DragAdapter extends BaseAdapter {
    /** TAG*/
    private final static String TAG = "DragAdapter";
    /** 是否显示底部的ITEM */
    private boolean isItemShow = false;
    private Context context;
    /** 控制的postion */
    private int holdPosition;
    /** 是否改变 */
    private boolean isChanged = false;
    /** 列表数据是否改变 */
    private boolean isListChanged = false;
    /** 是否可见 */
    boolean isVisible = true;
    /** 可以拖动的列表（即用户选择的频道列表） */
    public List<ChannelModel> channelList;
    /** TextView 频道内容 */
    private TextView item_text;
    /** 要删除的position */
    public int remove_position = -1;
    /** 是否可编辑 */
    private boolean isEditor = false;
    public DragAdapter(Context context) {
        this.context = context;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean isEditor) {
        this.isEditor = isEditor;
        notifyDataSetChanged();
    }

    public void setData( List<ChannelModel> channelList) {
        this.channelList = channelList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelModel getItem(int position) {
        // TODO Auto-generated method stub
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_channel_me, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ImageView iv_channel_del = (ImageView) view.findViewById(R.id.iv_channel_del);
        ChannelModel channel = getItem(position);
        item_text.setText(channel.getName());
        if ((position == 0) || (position == 1)){
            iv_channel_del.setVisibility(View.GONE);
            item_text.setEnabled(false);
        }else {
            if(isEditor){
                iv_channel_del.setVisibility(View.VISIBLE);
                item_text.setEnabled(true);
            }else {
                iv_channel_del.setVisibility(View.GONE);
                item_text.setEnabled(false);
            }
        }

        if (isChanged && (position == holdPosition) && !isItemShow) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
            isChanged = false;
        }
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if(remove_position == position){
            item_text.setText("");
        }
        return view;
    }

    /** 添加频道列表 */
    public void addItem(ChannelModel channel) {
        channelList.add(channel);
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 拖动变更频道排序 */
    public void exchange(int dragPostion, int dropPostion) {
        holdPosition = dropPostion;
        ChannelModel dragItem = getItem(dragPostion);
        Log.d(TAG, "startPostion=" + dragPostion + ";endPosition=" + dropPostion);
        if (dragPostion < dropPostion) {
            channelList.add(dropPostion + 1, dragItem);
            channelList.remove(dragPostion);
        } else {
            channelList.add(dropPostion, dragItem);
            channelList.remove(dragPostion + 1);
        }
        isChanged = true;
        isListChanged = true;
        notifyDataSetChanged();
    }

    /** 获取频道列表 */
    public List<ChannelModel> getChannnelLst() {
        return channelList;
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        Log.e("TAG_删除有频道","remove_position="+remove_position);
        try {
            if (remove_position != -1)
            channelList.remove(remove_position);
            remove_position = -1;
            isListChanged = true;
            notifyDataSetChanged();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 设置频道列表 */
    public void setListDate(List<ChannelModel> list) {
        channelList = list;
    }

    /** 获取是否可见 */
    public boolean isVisible() {
        return isVisible;
    }

    /** 排序是否发生改变 */
    public boolean isListChanged() {
        return isListChanged;
    }

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    /** 显示放下的ITEM */
    public void setShowDropItem(boolean show) {
        isItemShow = show;
    }
}

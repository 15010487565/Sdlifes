package com.sdlifes.sdlifes.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdlifes.sdlifes.R;
import com.sdlifes.sdlifes.model.ChannelModel;

import java.util.List;

/**
 * Created by gs on 2020/8/13.
 */
public class OtherAdapter extends BaseAdapter {

    private Context context;
    public List<ChannelModel> channelList;
    private TextView item_text;
    /** 是否可见 在移动动画完毕之前不可见，动画完毕后可见*/
    boolean isVisible = true;
    /** 要删除的position */
    public int remove_position = -1;
    /** 是否是用户频道 */
    private boolean isUser = false;

    public OtherAdapter(Context context ,boolean isUser) {
        this.context = context;
        this.isUser = isUser;
    }

    public void setData( List<ChannelModel> channelList) {
        this.channelList = channelList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public ChannelModel getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_channel_other, null);
        item_text = (TextView) view.findViewById(R.id.text_item);
        ChannelModel channel = getItem(position);
        item_text.setText("+\t"+channel.getName());
        if(isUser){
            if ((position == 0) || (position == 1)){
                item_text.setEnabled(false);
            }
        }
        if (!isVisible && (position == -1 + channelList.size())){
            item_text.setText("");
            item_text.setSelected(true);
            item_text.setEnabled(true);
        }
        if(remove_position == position){
            item_text.setText("");
        }
        return view;
    }

    /** 获取频道列表 */
    public List<ChannelModel> getChannnelLst() {
        return channelList;
    }

    /** 添加频道列表 */
    public void addItem(ChannelModel channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    /** 设置删除的position */
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
        // notifyDataSetChanged();
    }

    /** 删除频道列表 */
    public void remove() {
        Log.e("TAG_删除有频道","remove_position="+remove_position);
        try {
            if (remove_position != -1)
            channelList.remove(remove_position);
            remove_position = -1;
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

    /** 设置是否可见 */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

}


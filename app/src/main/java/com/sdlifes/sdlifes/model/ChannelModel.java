package com.sdlifes.sdlifes.model;

import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by gs on 19/11/2018.
 */

public class ChannelModel implements Serializable {


    /**
     * name : 军事
     * categoryid : 7
     */

    private String name;
    private int categoryid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof  ChannelModel){
            ChannelModel channelModel = (ChannelModel) obj;
            return this.name.equals(channelModel.name)
                    &&this.categoryid == channelModel.categoryid;
        }
        return super.equals(obj);
    }
}

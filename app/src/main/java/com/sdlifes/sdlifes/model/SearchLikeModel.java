package com.sdlifes.sdlifes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2020/8/18.
 */
public class SearchLikeModel implements Serializable {

    /**
     * errno : 0
     * data : ["印度国产武直武器都没配全就被部署中印边境(图)","iPhone 12系列续航表现曝光：电池缩水续航反涨？","苹果或因工人生活环境问题而暂缓在越南组装iPhone","消息称华为成立部门做屏幕驱动芯片 进军屏幕行业 ","上半年保险理赔报告的数据分析","华为Mate 40/40 Pro爆料：标配90Hz曲面屏 LG/京东方供货"]
     * errmsg : 执行成功
     */

    private int errno;
    private String errmsg;
    private List<String> data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}

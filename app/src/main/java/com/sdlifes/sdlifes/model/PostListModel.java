package com.sdlifes.sdlifes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs on 19/11/2018.
 */

public class PostListModel implements Serializable {


    /**
     * errno : 0
     * data : [{"picArr":["https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg"],"context":"11asdasdasasdasdascxavcdsvsdv","id":1}]
     * errmsg : 执行成功
     */

    private int errno;
    private String errmsg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * picArr : ["https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg","https://n.sinaimg.cn/front20200901ac/226/w1280h546/20200901/32f6-iypetiv2924778.jpg"]
         * context : 11asdasdasasdasdascxavcdsvsdv
         * id : 1
         */

        private String context;
        private int id;
        private ArrayList<String> picArr;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ArrayList<String> getPicArr() {
            return picArr;
        }

        public void setPicArr(ArrayList<String> picArr) {
            this.picArr = picArr;
        }
    }
}

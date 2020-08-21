package com.sdlifes.sdlifes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 19/11/2018.
 */

public class UserAdModel implements Serializable{


    /**
     * errno : 0
     * data : [{"id":4,"state":1,"pic":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597255357871&di=25231d3d624faf040cf1718228148bec&imgtype=0&src=http%3A%2F%2Ffile.youboy.com%2Fa%2F105%2F81%2F6%2F2%2F11099982s.jpg","title":"广告1","url":"http://news.baidu.com/"},{"id":5,"state":1,"pic":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597255357871&di=25231d3d624faf040cf1718228148bec&imgtype=0&src=http%3A%2F%2Ffile.youboy.com%2Fa%2F105%2F81%2F6%2F2%2F11099982s.jpg","title":"广告2","url":"http://news.baidu.com/"},{"id":6,"state":1,"pic":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597255357871&di=25231d3d624faf040cf1718228148bec&imgtype=0&src=http%3A%2F%2Ffile.youboy.com%2Fa%2F105%2F81%2F6%2F2%2F11099982s.jpg","title":"广告3","url":"http://news.baidu.com/"}]
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
         * id : 4
         * state : 1
         * pic : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597255357871&di=25231d3d624faf040cf1718228148bec&imgtype=0&src=http%3A%2F%2Ffile.youboy.com%2Fa%2F105%2F81%2F6%2F2%2F11099982s.jpg
         * title : 广告1
         * url : http://news.baidu.com/
         */

        private int id;
        private int state;
        private String pic;
        private String title;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}

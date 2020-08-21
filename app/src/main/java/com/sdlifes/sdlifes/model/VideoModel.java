package com.sdlifes.sdlifes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 2020/8/21.
 */
public class VideoModel implements Serializable {

    /**
     * errno : 0
     * data : [{"id":48,"title":"釜底抽薪！科学家研发的DNA纳米机器人能\u201c饿死\u201d肿瘤","url":"http://122.114.222.233:8080/videos/釜底抽薪！科学家研发的DNA纳米机器人能\u201c饿死\u201d肿瘤.mp4","show":0,"pic":"http://image1.pearvideo.com/cont/20200817/15591790-151451-1.png"}]
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
         * id : 48
         * title : 釜底抽薪！科学家研发的DNA纳米机器人能“饿死”肿瘤
         * url : http://122.114.222.233:8080/videos/釜底抽薪！科学家研发的DNA纳米机器人能“饿死”肿瘤.mp4
         * show : 0
         * pic : http://image1.pearvideo.com/cont/20200817/15591790-151451-1.png
         */

        private int id;
        private String title;
        private String url;
        private int show;
        private String pic;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getShow() {
            return show;
        }

        public void setShow(int show) {
            this.show = show;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}

package com.sdlifes.sdlifes.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 19/11/2018.
 */

public class PostMoreModel implements Serializable{

    /**
     * errno : 0
     * data : [{"show":0,"create":"","id":6,"isnew":0,"title":"话题6","ishot":1},{"show":0,"create":"","id":5,"isnew":0,"title":"话题5","ishot":1},{"show":0,"create":"","id":4,"isnew":0,"title":"话题4","ishot":1},{"show":0,"create":"","id":3,"isnew":0,"title":"话题3","ishot":1},{"show":0,"create":"","id":2,"isnew":0,"title":"话题2","ishot":1},{"show":0,"create":"2020-09-09","id":1,"isnew":0,"title":"话题1","ishot":1}]
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
         * show : 0
         * create :
         * id : 6
         * isnew : 0
         * title : 话题6
         * ishot : 1
         */

        private int show;
        private String create;
        private int id;
        private int isnew;
        private String title;
        private int ishot;

        public int getShow() {
            return show;
        }

        public void setShow(int show) {
            this.show = show;
        }

        public String getCreate() {
            return create;
        }

        public void setCreate(String create) {
            this.create = create;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIsnew() {
            return isnew;
        }

        public void setIsnew(int isnew) {
            this.isnew = isnew;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getIshot() {
            return ishot;
        }

        public void setIshot(int ishot) {
            this.ishot = ishot;
        }
    }
}

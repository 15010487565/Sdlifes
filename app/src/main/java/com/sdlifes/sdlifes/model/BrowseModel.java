package com.sdlifes.sdlifes.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gs on 19/11/2018.
 */

public class BrowseModel implements Serializable{

    /**
     * errno : 0
     * data : [{"see":0,"id":1,"pic":"https://n.sinaimg.cn/eladies/transform/500/w300h200/20200415/bfa3-isehnnk7205034.jpg","title":"钟晓芹复婚：复婚之前 要想清楚这三点","content":"","focus":"1"}]
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
         * see : 0
         * id : 1
         * pic : https://n.sinaimg.cn/eladies/transform/500/w300h200/20200415/bfa3-isehnnk7205034.jpg
         * title : 钟晓芹复婚：复婚之前 要想清楚这三点
         * content :
         * focus : 1
         */

        private int see;
        private int id;
        private ArrayList<String> pic;
        private boolean isCheck;

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public ArrayList<String> getPic() {
            return pic;
        }

        public void setPic(ArrayList<String> pic) {
            this.pic = pic;
        }

        private String title;
        private String content;
        private String focus;

        public int getSee() {
            return see;
        }


        public void setSee(int see) {
            this.see = see;
        }

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }
    }
}

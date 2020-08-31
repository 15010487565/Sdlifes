package com.sdlifes.sdlifes.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gs on 19/11/2018.
 */

public class HomeModel implements Serializable{


    private int errno;
    private DataBean data;
    private String errmsg;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class DataBean {
        private List<String> searchArr;
        private List<NewsArrBean> newsArr;
        private List<CategoryArrBean> categoryArr;

        public List<String> getSearchArr() {
            return searchArr;
        }

        public void setSearchArr(List<String> searchArr) {
            this.searchArr = searchArr;
        }

        public List<NewsArrBean> getNewsArr() {
            return newsArr;
        }

        public void setNewsArr(List<NewsArrBean> newsArr) {
            this.newsArr = newsArr;
        }

        public List<CategoryArrBean> getCategoryArr() {
            return categoryArr;
        }

        public void setCategoryArr(List<CategoryArrBean> categoryArr) {
            this.categoryArr = categoryArr;
        }

        public static class NewsArrBean {


            private int id;
            private int state;
            private String title;
            private String type;
            private String url;
            private int see;
            private String focus;
            private String content;
            private List<String> pic;
            private String time;
            private String top;
            private String src;
            private int ostate;
            private String ourl;

            public int getOstate() {
                return ostate;
            }

            public void setOstate(int ostate) {
                this.ostate = ostate;
            }

            public String getOurl() {
                return ourl;
            }

            public void setOurl(String ourl) {
                this.ourl = ourl;
            }

            public String getTop() {
                return top;
            }

            public void setTop(String top) {
                this.top = top;
            }

            public String getSrc() {
                return TextUtils.isEmpty(src)?"":src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public String getTime() {
                return TextUtils.isEmpty(time)?"":time;
            }

            public void setTime(String time) {
                this.time = time;
            }

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

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getSee() {
                return see;
            }

            public void setSee(int see) {
                this.see = see;
            }

            public String getFocus() {
                return focus;
            }

            public void setFocus(String focus) {
                this.focus = focus;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public List<String> getPic() {
                return pic;
            }

            public void setPic(List<String> pic) {
                this.pic = pic;
            }
        }

        public static class CategoryArrBean {
            /**
             * name : 关注
             * categoryid : 17
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
        }
    }
}

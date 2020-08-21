package com.sdlifes.sdlifes.model;

import java.io.Serializable;

/**
 * Created by gs on 19/11/2018.
 */

public class UserinfoModel implements Serializable{


    /**
     * errno : 0
     * data : {"birthday":"","img":"","phone":"手机号","sex":1,"nickname":"","id":10001,"region":"","desc":""}
     * errmsg : 执行成功
     */

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
        /**
         * birthday :
         * img :
         * phone : 手机号
         * sex : 1
         * nickname :
         * id : 10001
         * region :
         * desc :
         */

        private String birthday;
        private String img;
        private String phone;
        private int sex;
        private String nickname;
        private int id;
        private String region;
        private String desc;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

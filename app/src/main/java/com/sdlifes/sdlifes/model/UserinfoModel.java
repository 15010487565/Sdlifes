package com.sdlifes.sdlifes.model;

import java.io.Serializable;

/**
 * Created by gs on 19/11/2018.
 */

public class UserinfoModel implements Serializable{

    /**
     * errno : 0
     * data : {"birthday":"25/08/2044","m_days":0,"b_name":"","img":"http://122.114.222.233:8080/uploaded/1598369045513hdImg_fedb7562c26409827a9b5e7f499673091584870266701.jpg","b_birthday":"","sex":2,"m_lasttime":"","phone":"15652166829","b_sex":1,"nickname":"零落","p_etime":"","id":10002,"state":3,"region":"北京市-北京市-昌平区","m_interval":0,"desc":"阿大使阿斯顿阿亲善大使的"}
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
         * birthday : 25/08/2044
         * m_days : 0
         * b_name :
         * img : http://122.114.222.233:8080/uploaded/1598369045513hdImg_fedb7562c26409827a9b5e7f499673091584870266701.jpg
         * b_birthday :
         * sex : 2
         * m_lasttime :
         * phone : 15652166829
         * b_sex : 1
         * nickname : 零落
         * p_etime :
         * id : 10002
         * state : 3
         * region : 北京市-北京市-昌平区
         * m_interval : 0
         * desc : 阿大使阿斯顿阿亲善大使的
         */

        private String birthday;
        private int m_days;
        private String b_name;
        private String img;
        private String b_birthday;
        private int sex;
        private String m_lasttime;
        private String phone;
        private int b_sex;
        private String nickname;
        private String p_etime;
        private int id;
        private int state;
        private String region;
        private int m_interval;
        private String desc;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getM_days() {
            return m_days;
        }

        public void setM_days(int m_days) {
            this.m_days = m_days;
        }

        public String getB_name() {
            return b_name;
        }

        public void setB_name(String b_name) {
            this.b_name = b_name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getB_birthday() {
            return b_birthday;
        }

        public void setB_birthday(String b_birthday) {
            this.b_birthday = b_birthday;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getM_lasttime() {
            return m_lasttime;
        }

        public void setM_lasttime(String m_lasttime) {
            this.m_lasttime = m_lasttime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getB_sex() {
            return b_sex;
        }

        public void setB_sex(int b_sex) {
            this.b_sex = b_sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getP_etime() {
            return p_etime;
        }

        public void setP_etime(String p_etime) {
            this.p_etime = p_etime;
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

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getM_interval() {
            return m_interval;
        }

        public void setM_interval(int m_interval) {
            this.m_interval = m_interval;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

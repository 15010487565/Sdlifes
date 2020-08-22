package com.sdlifes.sdlifes.network;

/**
 * Created by vic on 2016/7/13.
 */
public interface UrlAddr {
    String path = "http://122.114.222.233:8080/platform/";
    String LOGIN = path+"api/user/login";//登录

    String REGISTER_SMS = path+"api/sms/register/";//获取注册验证码
    String REGISTER = path+"api/user/register";//注册

    String REPWD_SMS = path+"api/sms/repwd";//获取修改密码验证码
    String REPWD = path+"api/user/repwd";//修改密码

    String HOME = path+"api/news/index/";//首页
    String VIDEO = path+"api/videos/list";//视频

    String SEARCH_LIKE = path+"api/search/item/";//搜索页猜你喜欢
    String SEARCH = path+"api/search/list/";//搜索关键字

    String CHANNEL = path+"api/channel/list/";//获取频道
    String CHANNEL_EDIT = path+"api/channel/edit/";//频道编辑

    String USERINFO = path+"api/user/info/";//个人信息
    String USERINFO_AD = path+"api/ad/list";//个人信息广告
    String USERINFO_LAUNCH_AD = path+"api/ad/initad";//欢迎页广告
    String AD_ADD = path+"api/ad/add";//广告

    String UPLOADIMG = path+"api/upload/img";//上传图片
    String USERINFO_EDIT = path+"api/user/edit";//修改头像

    String ATTENTION_SAVE = path+"api/focus/save";//增加关注
    String ATTENTION_REMOVE = path+"api/focus/remove";//移除关注
    String ATTENTION_REMOVE_All = path+"api/focus/remove";//移除全部关注
    String ATTENTION_LIST = path+"api/focus/history";//关注

    String BROWSE_LIST = path+"api/browse/history";//浏览
    String BROWSE_SAVE = path+"api/browse/save";//浏览新增
    String BROWSE_REMOVE_SELECT = path+"api/browse/remove";//移除选中浏览记录
    String BROWSE_REMOVE_REMOVEALL = path+"api/browse/removeAll";//移除所有浏览记录

    String RELATION = path+"api/platform/relation";//联系
}

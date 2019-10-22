package com.xuzhenfei.server.config;

import lombok.Data;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;

@Data
public class TestConfig {
    //登录接口uri
    public static String loginUrl;
    //添加用户接口uri
    public static String addUserUrl;
    //获取用户信息uri
    public static String getUserInfoUrl;
    //获取用户列表uri
    public static String getUserListUrl;
    //更新用户信息uri
    public static String updateUserInfoUrl;

    //声明http客户端
    public static DefaultHttpClient defaultHttpClient;
    //用来存储cookies信息的变量
    public static CookieStore cookieStore;
}

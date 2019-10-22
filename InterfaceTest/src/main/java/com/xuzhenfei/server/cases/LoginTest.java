package com.xuzhenfei.server.cases;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.xuzhenfei.server.config.TestConfig;
import com.xuzhenfei.server.model.InterfaceName;
import com.xuzhenfei.server.model.LoginCase;
import com.xuzhenfei.server.utils.ConfigFile;
import com.xuzhenfei.server.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginTest {
    @BeforeTest(groups = "loginTrue", description = "测试准备工作，获取httpclient对象和uri")
    public void beforeTest(){

        TestConfig.loginUrl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSER);
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);

        TestConfig.defaultHttpClient = new DefaultHttpClient();
    }

    @Test(groups = "loginTrue",description = "用户登录成功接口测试")
    public void loginTrue() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        //获取数据库中的id为1的测试用例
        LoginCase loginCase = sqlSession.selectOne("loginCase", 1);

        System.out.println(loginCase.toString());
        System.out.println("TestConfig.loginUrl=" + TestConfig.loginUrl);

        //发送一个请求，返回请求结果数据
        String result = getResult(loginCase);

        //判断返回的结果是否符合预期
        Assert.assertEquals(loginCase.getExpected(), result);
    }



    @Test(groups = "loginFalse",description = "用户登录失败接口测试")
    public void loginFalse() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        //获取数据库中的id为1的测试用例
        LoginCase loginCase = sqlSession.selectOne("loginCase", 2);

        System.out.println(loginCase.toString());
        System.out.println("TestConfig.loginUrl=" + TestConfig.loginUrl);

        //发送一个请求，返回请求结果数据
        String result = getResult(loginCase);

        //判断返回的结果是否符合预期
        Assert.assertEquals(loginCase.getExpected(), result);

    }

    //获取用例执行返回结果
    private String getResult(LoginCase loginCase) throws IOException {

        //实例化一个请求
        HttpPost post = new HttpPost(TestConfig.loginUrl);
        //设置参数，参数格式为JSONObject
        JSONObject param = new JSONObject();
        param.put("userName", loginCase.getUserName());
        param.put("password", loginCase.getPassword());
        //设置请求头信息
        post.setHeader("content-type", "application/json");
        //将参数信息添加到方法中
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        //声明一个字符串接收响应结果
        String result;
        //执行post方法
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("result=" + result);

        //给本地cookie赋值
        TestConfig.cookieStore = TestConfig.defaultHttpClient.getCookieStore();
        System.out.println("cookie=" + TestConfig.cookieStore);
        return result;

    }
}

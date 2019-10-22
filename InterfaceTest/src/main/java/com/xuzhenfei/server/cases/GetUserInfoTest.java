package com.xuzhenfei.server.cases;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.xuzhenfei.server.config.TestConfig;
import com.xuzhenfei.server.model.GetUserInfoCase;
import com.xuzhenfei.server.model.Users;
import com.xuzhenfei.server.utils.DatabaseUtil;
import jdk.nashorn.internal.scripts.JO;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserInfoTest {

    @Test(dependsOnGroups = "loginTrue", description = "添加用户接口测试")
    public void getUserInfo() throws IOException {

        //获取执行SQL的SqlSession
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase = sqlSession.selectOne("getUserInfoCase", 1);
        System.out.println("User=" + getUserInfoCase.toString());
        System.out.println("TestConfig.getUserInfoUrl=" + TestConfig.getUserInfoUrl);

        //通过测试的sql语句获取用户信息
        Users users = sqlSession.selectOne(getUserInfoCase.getExpected(), getUserInfoCase);
        //返回结果校验
        List userList = new ArrayList();
        userList.add(users);
        JSONArray jsonArray = new JSONArray(userList);
        System.out.println("用例预期返回结果:"+jsonArray.toString());

        //获取接口返回值
        JSONArray array = getResult(getUserInfoCase);
        System.out.println("接口实际返回结果:"+array.toString());
        Assert.assertEquals(array, jsonArray);
    }

    private JSONArray getResult(GetUserInfoCase getUserInfoCase) throws IOException {

        //初始化一个post请求
        HttpPost post = new HttpPost(TestConfig.getUserInfoUrl);

        //设置请求头
        post.setHeader("content-type", "application/json");

        //设置请求体/参数
        JSONObject param = new JSONObject();
        param.put("id", getUserInfoCase.getUser_id());
        System.out.println("获取接口结果传入条件:"+getUserInfoCase.getUser_id());

        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        //设置cookie
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);

        //执行post请求
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);

        //接收返回的结果
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
//        List resultList = Arrays.asList(result);
        JSONArray arrayList = new JSONArray(result);
        return arrayList;
    }
}

package com.xuzhenfei.server.cases;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.mongodb.util.JSON;
import com.xuzhenfei.server.config.TestConfig;
import com.xuzhenfei.server.model.GetUserListCase;
import com.xuzhenfei.server.model.Users;
import com.xuzhenfei.server.utils.DatabaseUtil;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetUserListTest {

    @Test(dependsOnGroups = "loginTrue", description = "查询用户列表接口测试，查询所有性别为男的用户")
    public void getUserList() throws IOException, InterruptedException {

        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase = sqlSession.selectOne("getUserListCase", 1);
        System.out.println("getUserListCase=" + getUserListCase.toString());
        System.out.println("TestConfig.getUserListUrl=" + TestConfig.getUserListUrl);

        //获取接口返回结果
        JSONArray jsonArray = getResult(getUserListCase);

        Thread.sleep(2000);
        //获取预期结果数据
        List<Users> usersList = sqlSession.selectList(getUserListCase.getExpected(), getUserListCase);
        System.out.println("获取用例预期传入条件："+getUserListCase.toString());
        JSONArray expectedJson = new JSONArray(usersList);
        for (Users u:usersList) {
            System.out.println("用例预期返回结果："+u.toString());
        }
        System.out.println("用例预期返回结果："+expectedJson.toString());
        System.out.println("接口实际返回结果:"+jsonArray.toString());

        Assert.assertEquals(jsonArray.length(), expectedJson.length());
        for (int i = 0;i <= jsonArray.length();i++) {
            JSONObject expectedObj = (JSONObject) expectedJson.get(i);
            JSONObject json = (JSONObject) jsonArray.get(i);
            Assert.assertEquals(json, expectedObj);
        }
    }

    private JSONArray getResult(GetUserListCase getUserListCase) throws IOException {

        //初始化post请求
        HttpPost post = new HttpPost(TestConfig.getUserListUrl);
        //设置请求头
        post.setHeader("content-type", "application/json");
        //设置请求体/参数
        JSONObject param = new JSONObject();
        param.put("gender", getUserListCase.getGender());
        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);
        //设置cookie
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);

        //发送请求
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);
        //接收返回结果
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
//        List list = Arrays.asList(result);
        JSONArray jsonArray = new JSONArray(result);
        return jsonArray;
    }
}

package com.xuzhenfei.server.cases;

import com.xuzhenfei.server.config.TestConfig;
import com.xuzhenfei.server.model.AddUserCase;
import com.xuzhenfei.server.model.Users;
import com.xuzhenfei.server.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class AddUserTest {
    /**
     * 登录成功后添加用户，依赖于登录成功组
     * @throws IOException
     */
    @Test(dependsOnGroups = "loginTrue", description = "添加用户接口测试")
    public void addUser() throws IOException, InterruptedException {

        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        //查询出需要添加的用户数据
        AddUserCase addUserCase = sqlSession.selectOne("addUserCase",1);
        System.out.println("addUserCase.toString()=" + addUserCase.toString());
        System.out.println("TestConfig.addUserUrl=" + TestConfig.addUserUrl);

        //发送请求，获取接口返回结果
        String result = getResult(addUserCase);
        System.out.println("预期 接口返回结果=" + addUserCase.getExpected());
        //校验返回的结果是否符合预期
        Assert.assertEquals(addUserCase.getExpected(), result);

        //需要等待一下，否则查询不出数据
        Thread.sleep(4000);
        //查询出添加成功的数据
        Users users = sqlSession.selectOne("addUser", addUserCase);
        System.out.println("实际 数据更新结果=" + users.toString());

    }

    private String getResult(AddUserCase addUserCase) throws IOException {

        //实例化一个post请求
        HttpPost post = new HttpPost(TestConfig.addUserUrl);
        //设置请求头
        post.setHeader("content-type", "application/json");

        //设置请求体/参数
        JSONObject param = new JSONObject();
        param.put("userName", addUserCase.getUserName());
        param.put("password", addUserCase.getPassword());
        param.put("age", addUserCase.getAge());
        param.put("gender", addUserCase.getGender());
        param.put("isDelete", addUserCase.getIsDelete());
        param.put("permission", addUserCase.getPermission());

        StringEntity entity = new StringEntity(param.toString(), "utf-8");
        post.setEntity(entity);

        //设置cookie
        TestConfig.defaultHttpClient.setCookieStore(TestConfig.cookieStore);

        //发送请求
        HttpResponse response = TestConfig.defaultHttpClient.execute(post);

        //获取返回结果的实体
        String result = EntityUtils.toString(response.getEntity(), "utf-8");
        System.out.println("实际 接口返回结果=" + result);
        return result;
    }

}

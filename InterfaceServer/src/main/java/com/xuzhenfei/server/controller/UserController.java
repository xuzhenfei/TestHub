package com.xuzhenfei.server.controller;

import com.xuzhenfei.server.model.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@Api(value = "v1", description = "用户管理系统-用户控制层")
@RestController
@RequestMapping(value = "v1")
public class UserController {

    @Autowired
    private SqlSessionTemplate template;
    /**
     * 用户登录
     */
    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody Users users){
        System.out.println("user：" + users.toString());
        int result = template.selectOne("login", users);
//        System.out.println("查询到的结果是：" + result);
        Cookie cookie = new Cookie("login", "true");
        response.addCookie(cookie);
        if(result == 1){
            return true;
        }
        return false;
    }

    /**
     * 添加用户
     * @description 输入json格式的用户信息
     */
    @ApiOperation(value = "添加用户接口", httpMethod = "POST")
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public Boolean addUser(HttpServletRequest request, @RequestBody Users users){

        //判断cookie是否验证通过
        Boolean x = verifyCookie(request);
        int result = 0;
        if(x != null){
            //验证成功，添加用户
            result = template.insert("addUser", users);
        }

        if (result > 0){
            return true;
        }
        return false;
    }

    /**
     * 查询用户信息
     */

    @ApiOperation(value = "查询用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.POST)
    public List<Users> getUserInfo(HttpServletRequest request, @RequestBody Users users){
        System.out.println("getUserInfo 接口传入条件“"+users.toString());
        //判断cookie是否验证通过
        Boolean x = verifyCookie(request);
        if(x == true){
            List<Users> usersList = template.selectList("getUserInfo", users);
            System.out.println("getUserInfo获取到的用户数量是" +usersList.size());
            return usersList;
        }

        return null;
    }

    /**
     * 修改/删除用户信息
     */
    @ApiOperation(value = "修改/删除用户信息接口", httpMethod = "POST")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public int updateUserInfo(HttpServletRequest request, @RequestBody Users users){
        //判断cookie是否验证通过
        Boolean x = verifyCookie(request);
        int i = 0;
        if(x == true){
            i = template.update("updateUserInfo", users);
            System.out.println("执行了更新操作：" + i);
        }
        System.out.println("数据更新的条数为：" + i);
        return i;

    }
    /**
     * 验证Cookie
     * @param request
     * @return
     */
    private Boolean verifyCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
//        System.out.println("cookies="+cookies.toString());

        //判断cookies是否为空
        if(Objects.isNull(cookies)){
            System.out.println("cookies为空！");
            return false;
        }
        for (Cookie cookie:cookies) {
//            System.out.println("cookie="+cookie.toString());
            if(cookie.getName().equals("login") && cookie.getValue().equals("true")){
                System.out.println("cookie验证通过！");
                return true;
            }
        }
        return false;
    }
}

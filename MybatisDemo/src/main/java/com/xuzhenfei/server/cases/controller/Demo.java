package com.xuzhenfei.server.cases.controller;

import com.xuzhenfei.server.cases.model.User;
import io.swagger.annotations.Api;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1")
@Api(value="v1", description = "这是我的第一个demo")
public class Demo {

    @Autowired
    private SqlSessionTemplate template;

    @RequestMapping(value="/getUserCount", method = RequestMethod.GET)
    public Integer getUserCount(){
        Integer count = template.selectOne("getUserCount");
        return count;
    }

    @RequestMapping(value="/addUser", method = RequestMethod.POST)
    public Integer addUser(@RequestBody User user){
        final int addUser = template.insert("addUser", user);
        return addUser;
    }


    @RequestMapping(value = "updateUser", method = RequestMethod.POST)
    public int updateUser(@RequestBody User user){
        return template.update("updateUser", user);
    }

    @RequestMapping(value = "deleteUser", method = RequestMethod.POST)
    public int delUser(@RequestParam int id){
        return template.delete("deleteUser", id);
    }

}

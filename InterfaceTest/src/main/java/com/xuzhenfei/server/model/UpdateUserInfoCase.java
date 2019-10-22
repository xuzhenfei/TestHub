package com.xuzhenfei.server.model;

import lombok.Data;

@Data
public class UpdateUserInfoCase {
    private int id;
    private int user_id;
    private String userName;
    private int gender;
    private int age;
    private int permission;
    private int isDelete;
    private String expected;

}

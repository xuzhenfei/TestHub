package com.xuzhenfei.server.model;

import lombok.Data;

@Data
public class Users {
    private int id;
    private String userName;
    private String password;
    private int age;
    private int gender;
    private int permission;
    private int isDelete;
}

package com.xuzhenfei.server.model;

import lombok.Data;

@Data
public class AddUserCase {
    private String userName;
    private String password;
    private int gender;
    private int age;
    private int permission;
    private int isDelete;
    private String expected;


}

package com.xuzhenfei.server.model;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;

@Data
public class GetUserListCase {

    private int id;
    private String userName;
    private int age;
    private int gender;
    private String expected;
}

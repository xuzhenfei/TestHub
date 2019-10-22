package com.xuzhenfei.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PreDestroy;

@EnableScheduling
@SpringBootApplication
public class ServerApplication {

    private static ConfigurableApplicationContext context;
    public static void main(String[] args) {

        ServerApplication.context = SpringApplication.run(ServerApplication.class, args);
    }

    @PreDestroy
    public void close(){
        ServerApplication.context.close();
    }
}

package com.example.demo12;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.example.demo12.dao")
@SpringBootApplication
public class Demo12Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo12Application.class, args);
    }

}

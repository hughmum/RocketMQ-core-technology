package com.mu.paya;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.mu.paya.mapper")
public class PayaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayaApplication.class, args);
    }
}

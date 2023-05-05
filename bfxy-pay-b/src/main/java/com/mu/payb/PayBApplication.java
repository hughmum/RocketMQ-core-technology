package com.mu.payb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mu.payb.mapper")
public class PayBApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayBApplication.class, args);
    }
}

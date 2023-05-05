package com.mu.pkg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Mr.xin
 */
@SpringBootApplication
@MapperScan("com.mu.pkg.mapper")
public class PkgAppliction {
    public static void main(String[] args) {
        SpringApplication.run(PkgAppliction.class,args);
    }
}

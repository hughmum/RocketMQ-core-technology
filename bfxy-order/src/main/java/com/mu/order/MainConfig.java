package com.mu.order;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr.xin
 */
@Configuration
@ComponentScan(basePackages = {"com.mu.order.*"})
@MapperScan(basePackages = {"com.mu.order.m"})
public class MainConfig {

}
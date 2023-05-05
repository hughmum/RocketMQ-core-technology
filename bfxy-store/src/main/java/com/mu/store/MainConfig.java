package com.mu.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.mu.store.mapper")
@ComponentScan({"com.mu.store"})
public class MainConfig {
}

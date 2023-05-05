package com.mu.store.service.provider;

import com.alibaba.dubbo.config.annotation.Service;

// 暴露服务
@Service(
        version="1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class HelloServiceProvider implements HelloServiceApi{

    @Override
    public String sayHello(String s) {
        System.out.println("========== " +
                s + " ==========");
        return "hello: "+ s;
    }
}

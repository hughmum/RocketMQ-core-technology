package com.mu.order.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mu.store.service.provider.HelloServiceApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("index")
    public String sayHello(){

        System.out.println(System.getProperty("classpath"));
        return "hello";
    }

    @Reference(
            version = "1.0.0",
            application = "${dubbo.application.id}",
            interfaceName = "com.mu.store.service.provider.HelloServiceApi",
            check = false,
            timeout = 3000,
            retries = 0
    )
    private HelloServiceApi helloServiceApi;
    @RequestMapping("/hello")
    public String sayHello(@RequestParam("name") String name){
        System.out.println("name");
        return helloServiceApi.sayHello(name);
    }
}

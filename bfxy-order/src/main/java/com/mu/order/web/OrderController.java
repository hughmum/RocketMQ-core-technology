package com.mu.order.web;

import com.mu.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Mr.xin
 */
@RestController
@RequestMapping("/order")
public class OrderController {


//    @HystrixCommand(
//            commandKey = "createOrder",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//            },
//            fallbackMethod = "createOrderFallBackMethod4Timeout"
//    )

//    @HystrixCommand(
//            commandKey = "createOrder",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
//            },
//            threadPoolKey = "createOrderThreadPool",
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "10"),
//                    @HystrixProperty(name = "maxQueueSize", value = "2000"),
//                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "10")
//            },
//            fallbackMethod = "createOrderFallBackMethod4Thread"
//    )

    @Autowired
    private OrderService orderService;

//    @HystrixCommand(
//            commandKey = "createOrder",
//            commandProperties= {
//						@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"),
//						@HystrixProperty(name="execution.isolation.semaphore.maxConcurrentRequests", value="3")
//				},
//				fallbackMethod = "createOrderFallbackMethod4semaphore"
//    )
    @PostMapping("/createOrder")
    public String createOrder(@RequestParam("cityId") String cityId,
                              @RequestParam("platformId") String platformId,
                              @RequestParam("userId") String userId,
                              @RequestParam("supplierId") String supplierId,
                              @RequestParam("goodsId") String goodsId) {

        System.out.println("====== 调用成功 ======");

        boolean result = orderService.createOrder(cityId, platformId, userId, supplierId, goodsId);

        return result ? "创建订单成功" : "创建订单失败";
    }


    public String createOrderFallBackMethod4Timeout(@RequestParam("cityId") String cityId,
                                                    @RequestParam("platformId") String platformId,
                                                    @RequestParam("userId") String userId,
                                                    @RequestParam("supplierId") String supplierId,
                                                    @RequestParam("goodsId") String goodsId) {

        System.out.println("======= timeout =======");
        return "timeout";
    }

    public String createOrderFallBackMethod4Thread(@RequestParam("cityId") String cityId,
                                                   @RequestParam("platformId") String platformId,
                                                   @RequestParam("userId") String userId,
                                                   @RequestParam("supplierId") String supplierId,
                                                   @RequestParam("goodsId") String goodsId) {

        return "thread 限流策略";
    }


    public String createOrderFallbackMethod4semaphore(@RequestParam("cityId") String cityId,
                                                   @RequestParam("platformId") String platformId,
                                                   @RequestParam("userId") String userId,
                                                   @RequestParam("supplierId") String supplierId,
                                                   @RequestParam("goodsId") String goodsId) {

        return "semaphore 限流方式";
    }


    }

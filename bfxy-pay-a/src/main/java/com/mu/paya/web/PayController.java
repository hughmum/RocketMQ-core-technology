package com.mu.paya.web;

import com.mu.paya.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayController {


    @Autowired
    private PayService payService;
    @PostMapping("/pay")
    public String pay(@RequestParam("accountId") String accountId,
                      @RequestParam("orderId") String orderId,
                      @RequestParam("userId") String userId,
                      @RequestParam("money") double money) {
         return payService.pay(accountId, orderId, userId, money);

    }

}

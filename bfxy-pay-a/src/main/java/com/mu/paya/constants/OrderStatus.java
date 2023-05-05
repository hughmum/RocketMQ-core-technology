package com.mu.paya.constants;

public enum OrderStatus {
    CREATED("0"),
    SUCCESS("1"),
    FAILURE("2");
    private String code;
    private OrderStatus(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
}

package com.rocketmq.spring.boot.service;

public interface RocketMQProviderService {

    //发送消息
    public void send(String topic, String tags, String keys, Object content);
}

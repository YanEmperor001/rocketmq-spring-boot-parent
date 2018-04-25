package com.rocketmq.spring.boot.service.impl;

import com.rocketmq.spring.boot.service.RocketMQProviderService;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "org.apache.rocketmq")
@Service("rocketMQProviderService")
public class RocketMQProviderServiceImpl implements RocketMQProviderService {

    private final Logger logger = LoggerFactory.getLogger(RocketMQProviderServiceImpl.class);
    protected final DefaultMQProducer producer = new DefaultMQProducer();

    @Value("${org.apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${org.apache.rocketmq.provier.instanceName}")
    private String instanceName;
    @Value("${org.apache.rocketmq.providerGroup}")
    private String providerGroup;

    /**
     * @author guofeng
     * @create 2018/4/25
     * @Description 初始化
     **/
    @PostConstruct
    public void init() {
        try {
            producer.setVipChannelEnabled(false);
            producer.setNamesrvAddr(this.namesrvAddr);
            producer.setInstanceName(this.instanceName);
            producer.setProducerGroup(this.providerGroup);
            producer.start();
        } catch (MQClientException e) {
            throw new RuntimeException("初始化异常！", e);
        }
    }

    /**
     * @author guofeng
     * @create 2018/4/25
     * @Description 发送消息
     **/
    @Override
    public void send(String topic, String tags, String keys, Object content) {
        try {
            Message message = new Message(topic, tags, keys, content.toString().getBytes());
            producer.send(message);
        } catch (Exception e) {
            throw new RuntimeException("消息发送异常！", e);
        }
    }

    /**
     * @author guofeng
     * @create 2018/4/25
     * @Description 销毁通道
     **/
    public void destory() {
        if (null != producer) {
            producer.shutdown();
        }
    }
}

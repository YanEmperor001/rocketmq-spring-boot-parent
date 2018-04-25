package com.rocketmq.spring.boot.listener;

import com.google.gson.Gson;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "org.apache.rocketmq")
public class RocketMQListener implements MessageListenerConcurrently {

    private final Gson gson = new Gson();
    private final Logger logger = LoggerFactory.getLogger(RocketMQListener.class);
    protected DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

    @Value("${org.apache.rocketmq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${org.apache.rocketmq.consumerGroup}")
    private String consumerGroup;
    @Value("${org.apache.rocketmq.customer.instanceName}")
    private String instanceName;

    /**
     * @author guofeng
     * @create 2018/4/25
     * @Description 初始化
     **/
    @PostConstruct
    public void init() {
        try {
            consumer.setVipChannelEnabled(false);
            consumer.registerMessageListener(this);
            consumer.setNamesrvAddr(this.namesrvAddr);
            consumer.setInstanceName(this.instanceName);
            consumer.setConsumerGroup(this.consumerGroup);
            consumer.subscribe("topicTest", "*");
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.start();
        } catch (MQClientException e) {
            throw new RuntimeException("初始化异常！", e);
        }
    }

    /**
     * @author guofeng
     * @create 2018/4/25
     * @Description 监听消息内容
     **/
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        if (!CollectionUtils.isEmpty(list)) {
            for (MessageExt messageExt : list) {
                logger.info("messageExt:" + gson.toJson(messageExt));
                byte[] message = messageExt.getBody();
                System.out.println("消息内容：" + new String(message));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }

    /**
     * @author guofeng
     * @create 2018/4/25
     * @Description 关闭通道
     **/
    public void destory() {
        if (null != consumer) {
            consumer.shutdown();
        }
    }
}

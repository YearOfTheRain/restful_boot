package com.restful.activemq;

import org.springframework.context.annotation.Configuration;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 订阅消息生产者
 * @date 2019-10-16 12:13
 */
@Configuration
public class TopicProducers {

    /*@Autowired
    private JmsMessagingTemplate messagingTemplate;

    *//**
     * 方法描述: 构建发布消息，供消费者订阅
     *
     * @param message 待发送的消息
     * @return void
     * @author LiShuLin
     * @date 2019/10/16
     *//*
    public void sendMessage(String message) {
        ActiveMQTopic mqTopic = new ActiveMQTopic("first");
        ActiveMQQueue mqQueue = new ActiveMQQueue("queue");
        messagingTemplate.convertAndSend(mqTopic, message);
        messagingTemplate.convertAndSend(mqQueue, message);
    }*/
}

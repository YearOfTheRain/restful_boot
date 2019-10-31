package com.restful.activemq;

import org.springframework.stereotype.Component;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 消息订阅 消费者
 * @date 2019-10-16 16:08
 */
@Component
public class TopicCustomer {
/*

    @JmsListener(destination = "first")
    public void subMessage(String message) {
        System.out.println("消费者 1 ：" + message);
    }

    @JmsListener(destination = "first")
    public void subMessage2(String message) {
        System.out.println("消费者 2 ：" + message);
    }

    @JmsListener(destination = "queue")
    public void subMessage3(String message) {
        System.out.println("消费者 3 ：" + message);
    }
*/

    /*@JmsListener(destination = "queue")
    public void subMessage4(String message) {
        System.out.println("消费者 4 ：" + message);
    }*/
}

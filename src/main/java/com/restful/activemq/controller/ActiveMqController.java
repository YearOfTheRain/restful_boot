package com.restful.activemq.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 消息订阅 控制层
 * @date 2019-10-16 16:12
 */
@RestController
@RequestMapping("/mq")
public class ActiveMqController {

  /*  @Autowired
    private TopicProducers producers;

    @GetMapping
    public ResponseEntity sendMessage(String message) {
        producers.sendMessage(message);
        return ResponseEntity.success(message);
    }*/
}

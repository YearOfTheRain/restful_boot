package com.restful.common.configbean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 线程池 bean
 * @date 2019-10-15 10:43
 */
@Component
@ConfigurationProperties(prefix = "thread")
@PropertySource(value = "classpath:config.properties")
@Data
public class ThreadBean {

    /*** 核心线程池大小*/
    private int corePoolSize;

    /*** 最大可创建的线程数*/
    private int maxPoolSize;

    /*** 队列最大长度*/
    private int queueCapacity;

    /*** 线程池维护线程所允许的空闲时间*/
    private int keepAliveSeconds;


}

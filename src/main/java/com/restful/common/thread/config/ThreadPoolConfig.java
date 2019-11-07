package com.restful.common.thread.config;

import com.restful.common.configbean.ThreadBean;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 线程池配置
 * @date 2019-10-10 11:30
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 线程池 配置 bean
     */
    private final ThreadBean threadBean;


    public ThreadPoolConfig(ThreadBean threadBean) {
        this.threadBean = threadBean;
    }

    /**
     * 方法描述: 开启固定条数线程，开启条数按服务器配置设置
     *
     * @return java.util.concurrent.ExecutorService
     * @author LiShuLin
     * @date 2019/10/15
     */
    @Bean(name = "fixedThreadPool")
    protected ExecutorService fixedThreadPool() {
        ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("fixedThread-pool-%d").daemon(true).build();
        return new ThreadPoolExecutor(threadBean.getCorePoolSize(), threadBean.getCorePoolSize(), 0L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(), threadFactory);
    }

}

package com.restful.common.thread.config;

import com.restful.common.configbean.ThreadBean;
import com.restful.common.thread.Threads;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
    @Autowired
    private ThreadBean threadBean;

    /**
     * 方法描述:  线程池配置
     *
     * @return org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
     * @author LiShuLin
     * @date 2019/10/10
     */
    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(threadBean.getMaxPoolSize());
        executor.setCorePoolSize(threadBean.getCorePoolSize());
        executor.setQueueCapacity(threadBean.getQueueCapacity());
        executor.setKeepAliveSeconds(threadBean.getKeepAliveSeconds());
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    /**
     * 方法描述: 执行周期性或定时任务
     *     由于该任务创建的实例对象比较多，这里指定 8 条线程执行
     *
     * @return java.util.concurrent.ScheduledExecutorService
     * @author LiShuLin
     * @date 2019/10/10
     */
    @Bean(name = "scheduledExecutorService")
    protected ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(threadBean.getCorePoolSize(),
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true)
                        .build()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }

    /**
     * 方法描述: 开的线程条数越多，所使用的资源就越多，所以需要根据服务器配置合理分配线程条数
     *
     * @return java.util.concurrent.ExecutorService
     * @author LiShuLin
     * @date 2019/10/15
     */
    @Bean(name = "fixedThreadPool")
    protected ExecutorService fixedThreadPool() {
        return new ThreadPoolExecutor(threadBean.getCorePoolSize() - 35, threadBean.getMaxPoolSize(),
                threadBean.getKeepAliveSeconds(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue(),
                new BasicThreadFactory.Builder().namingPattern("fixedThread-pool-%d").daemon(true)
                .build()){
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                Threads.printException(r, t);
            }
        };
    }


}

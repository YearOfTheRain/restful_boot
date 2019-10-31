package com.restful.common.thread.manage;

import com.restful.common.spring.SpringUtils;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 异步任务管理器
 * @date 2019-10-10 11:42
 */
public class AsyncManager {

    /**
     * 操作延迟10毫秒
     */
    private static final int OPERATE_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    private ExecutorService fixedThreadPool = SpringUtils.getBean("fixedThreadPool");

    private AsyncManager() {
    }

    /**
     * 静态内部类 完成单例模式加载
     */
    private static class AsyncStaticManager {
        private static AsyncManager asyncManager = new AsyncManager();
    }

    /**
     * 对外提供调用的单例实例
     * @return AsyncManager
     */
    public static AsyncManager getInstance() {
        return AsyncStaticManager.asyncManager;
    }

    /**
     * 方法描述: 开始执行任务
     *
     * @param timerTask 待执行的定时任务
     * @return void
     * @author LiShuLin
     * @date 2019/10/10
     */
    public void execute(TimerTask timerTask) {
        //延迟 10 毫秒执行该任务
        executor.schedule(timerTask, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    public void setFixedThreadPool(TimerTask timerTask) {
        fixedThreadPool.execute(timerTask);
        fixedThreadPool.shutdown();
    }
}

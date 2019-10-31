package com.restful.common.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description spring 工具类
 * @date 2019-10-10 11:50
 */
@Component
public final class SpringUtils implements BeanFactoryPostProcessor {

    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 根据 bean 的名字（name）获取该 bean 的实例
     *
     * @param name bean 的名字（name）
     * @throws BeansException BeansException
     */
    public static <T> T getBean(String name) throws BeansException {
        //noinspection unchecked
        return (T) beanFactory.getBean(name);
    }

    /**
     * 根据 bean 的类（class）获取该 bean 的实例
     * @param tClass bean 的类（class）
     * @throws BeansException BeansException
     */
    public static <T> T getBean(Class<T> tClass) throws BeansException {
        return beanFactory.getBean(tClass);
    }
}

package com.restful.common.configbean;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

/**
 * @author LiShuLin
 * @version v1.0
 * @program restful_boot
 * @description 读取自定义 yml 工厂类
 * @date 2019-10-15 11:50
 */
@Deprecated
public class YmlProperSourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (name != null) {
//            return new PropertySourcesLoader().load(resource.getResource(), name, null);
        }
//        return new PropertySourcesLoader().load(resource.getResource(), getNameForResource(resource.getResource()), null);
        return null;
    }

    private static String getNameForResource(Resource resource) {
        String name = resource.getDescription();
        if (!org.springframework.util.StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }
}

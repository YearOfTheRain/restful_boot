package com.restful.redis.config;

import com.restful.redis.utils.FastJson2JsonRedisSerializer;
import com.restful.redis.utils.RedisCommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @author Shulin Li
 * @version v1.0
 * @program restful_boot
 * @description redis 配置中心
 * @date 2019-12-03 16:26
 */
@Configuration
@PropertySource(value = "classpath:config.properties")
public class RedisConfig {

    /*** 配置连接库*/
    @Value("${redis.database}")
    private Integer database;

    /*** 主机地址*/
    @Value("${redis.host}")
    private String host;

    /*** 端口*/
    @Value("${redis.port}")
    private Integer port;

    /*** 密码*/
    @Value("${redis.password}")
    private String password;

    /*** 连接池中的最大连接数（负值表示无限制）*/
    @Value("${redis.jedis.pool.max-active}")
    private Integer maxActive;

    /*** 连接池中的阻塞最大等待时间（负值表示无限制）*/
    @Value("${redis.jedis.pool.max-wait}")
    private Integer maxWait;

    /*** 连接池中的最大空闲连接*/
    @Value("${redis.jedis.pool.max-idle}")
    private Integer maxIdle;

    /*** 连接池中的最小空闲连接*/
    @Value("${redis.jedis.pool.min-idle}")
    private Integer minIdle;

    /*** 连接超时时间*/
    @Value("${redis.timeout}")
    private Integer timeOut;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration ();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(database);
        configuration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisClientConfigurationBuilder clientConfigurationBuilder = JedisClientConfiguration.builder();
        clientConfigurationBuilder.connectTimeout(Duration.ofMillis(timeOut));
        return new JedisConnectionFactory(configuration,
                clientConfigurationBuilder.build());
    }

    @Bean("redisTemplate")
    public RedisTemplate functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    private void initDomainRedisTemplate(RedisTemplate redisTemplate, RedisConnectionFactory factory) {
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(fastJson2JsonRedisSerializer());
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(factory);
    }

    @Bean("commonUtil")
    public RedisCommonUtil redisUtil(RedisTemplate redisTemplate) {
        RedisCommonUtil redisUtil = new RedisCommonUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }

}

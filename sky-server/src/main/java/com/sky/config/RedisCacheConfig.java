package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Slf4j
@Configuration
public class RedisCacheConfig {
    /**
     * 配置redisTemplate bean，⾃定义数据的序列化的⽅式
     * @param redisConnectionFactory 连接redis的⼯⼚，底层有
    场景依赖启动时，⾃动加载
     * @return
     */
    @Bean("secondRedisTemplate")
    @Primary
    public RedisTemplate secondRedisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory){
        //1.构建RedisTemplate模板对象
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        log.info("正在创建 secondRedisTemplate Bean...");
        //2.为不同的数据结构设置不同的序列化⽅案
        //设置key序列化⽅式
        template.setKeySerializer(new StringRedisSerializer());
        //设置value序列化⽅式
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //设置hash中field字段序列化⽅式
        template.setHashKeySerializer(new StringRedisSerializer());
        //设置hash中value的序列化⽅式
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        //5.初始化参数设置
        template.afterPropertiesSet();
        return template;
    }
}


package org.example.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Shengke
 * @Date: 2023-06-06-17:15
 * @Description:
 */
@Configuration
public class RedissionConfig {


//  释放连接
    @Bean(destroyMethod="shutdown")
    public Redisson redisson(){
        // 单机模式
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0);
        return (Redisson) Redisson.create(config);
    }



}

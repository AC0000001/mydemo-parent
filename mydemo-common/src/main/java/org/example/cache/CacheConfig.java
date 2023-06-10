package org.example.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Shengke
 * @Date: 2023-06-10-16:00
 * @Description:
 */
@Configuration
public class CacheConfig {

    @Bean
    public Cache<String, Object> guavaCache() {
        return CacheBuilder.newBuilder().build();
    }
}

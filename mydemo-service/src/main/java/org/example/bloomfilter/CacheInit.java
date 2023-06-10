package org.example.bloomfilter;

/**
 * @Author: Shengke
 * @Date: 2023-06-10-15:54
 * @Description:
 */
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.example.service.cache.BloomFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Component
public class CacheInit implements ApplicationRunner {
    private final BloomFilterService bloomFilterService;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public CacheInit(BloomFilterService bloomFilterService) {
        this.bloomFilterService = bloomFilterService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //  加载所有需要缓存的数据 ID
        //  并在redis存储需要进行布隆过滤器判断的数据的本体
        List<String> cacheKeyList = bloomFilterService.loadCacheKeys();

        ZSetOperations<String, String> zSetOps = stringRedisTemplate.opsForZSet();

        // 初始化 Bloom Filter
        int expectedInsertions = cacheKeyList.size();
        double fpp = 0.03; // false positive probability
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")), expectedInsertions, fpp);
        /*
            在redis、bloomFilter存储需要进行布隆过滤器判断的数据
            注意：在redisZsort存的是key值，在bloomFilter存的是他的Hex值
        */

        for (String key : cacheKeyList) {
            bloomFilter.put(key);
            zSetOps.add("sortSetForBloom", key, 1.0);
        }



        // 将 Bloom Filter 对象放入缓存，在后续查询时使用
        bloomFilterService.putBloomFilter(bloomFilter);
    }
}

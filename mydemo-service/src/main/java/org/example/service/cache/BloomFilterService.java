package org.example.service.cache;

import com.google.common.cache.Cache;
import com.google.common.hash.BloomFilter;

import lombok.extern.slf4j.Slf4j;

import org.example.elasticRepository.ProductRepository;
import org.example.entity.Product.Product;
import org.example.mapper.ProductMapper;
import org.example.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @Author: Shengke
 * @Date: 2023-06-10-15:55
 * @Description:
 */
@Service
@Slf4j
public class BloomFilterService {
    private final Cache cache;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public BloomFilterService(Cache cache) {
        this.cache = cache;
    }

    /**
     * 加载所有需要缓存的数据 ID
     */
    public List<String> loadCacheKeys() {
        List<String> idList = new ArrayList<>();
//        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.termQuery("title", "小米"))
//                .withPageable(PageRequest.of(0, 1))
//                .build();
//
//        List<Product> products = elasticsearchRestTemplate.queryForList(searchQuery, Product.class, IndexCoordinates.of("shopping"));
//        String productId = products.get(0).getId().toString();

        Product product = productMapper.selectByName("小米");
        String productTitle = product.getTitle();

        log.info("BloomFilter获得的数据ID列表：" + productTitle);

        //在redis存储需要进行布隆过滤器判断的数据的本体
        redisUtil.set(productTitle, product);

        idList.add(productTitle);

        return idList;
    }

    /**
     * 将 Bloom Filter 对象放入缓存
     */
    public void putBloomFilter(BloomFilter<CharSequence> bloomFilter) {
        redisUtil.set("bloom_filter", bloomFilter);
    }

    /**
     * 通过缓存获取数据
     */

    public Object get(String key) throws ExecutionException {
        // 判断该 key 是否存在于 Bloom Filter 中
        BloomFilter<CharSequence> bloomFilter = (BloomFilter<CharSequence>) redisUtil.get("bloom_filter");
        if (bloomFilter == null || !bloomFilter.mightContain(key)) {
            // 如果不存在，则直接返回空结果
            // 从数据库查出数据存入redis，并更新BloomFilter(在Controller实现了)
            int flag = -1;
            return flag;
        }


        //如果可能存在，则从有序集合中获取对应的键（key），并继续查询缓存
        Set<String> keys = stringRedisTemplate.opsForZSet().rangeByLex("sortSetForBloom", RedisZSetCommands.Range.range().gte(key).lte(key));
        if (keys == null || keys.isEmpty()) {
            // 缓存中不存在对应的key
            int flag = -1;
            return flag;
        } else {
            // 缓存中存在对应的key，可以使用获取到的value进行后续操作
            return redisUtil.get(key);
        }
    }

    /**
     * 将数据加入缓存，并更新 Bloom Filter
     */
    public void put(String key, Object value) throws ExecutionException {
        //数据加入缓存
        redisUtil.set(key, value);

        // 更新 Bloom Filter
        BloomFilter<CharSequence> bloomFilter = (BloomFilter<CharSequence>) redisUtil.get("bloom_filter");
        if (bloomFilter != null) {
            bloomFilter.put(key);
        }
    }
}
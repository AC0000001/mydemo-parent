package org.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.Blog.Blog;
import org.example.entity.R.R;
import org.example.entity.User.Users;
import org.example.mapper.BlogMapper;
import org.example.service.BlogService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xin麒
 * @date 2023/3/1 15:34
 */
@Service
@Slf4j
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;
    @Override
    @Transactional
    public R likeBlog(Long articalId, Long userId) {
        // 1.获取登录用户
        Long thisUserId = userId;
        // 2.判断当前登录用户是否已经点赞
        String artical = "blog:like:" + articalId;

        //Redis分布式锁
        String likeLock = "likeLock";
        String value = UUID.randomUUID().toString();
/*
        分开写不是原子操作！！！
        Boolean unLock = stringRedisTemplate.opsForValue().setIfAbsent(likeLock, value);
        stringRedisTemplate.expire(likeLock,10L, TimeUnit.SECONDS);*/

        Boolean unLock = stringRedisTemplate.opsForValue().setIfAbsent(likeLock, value, 10L, TimeUnit.SECONDS);
        if(unLock){
            Double score = stringRedisTemplate.opsForZSet().score(artical, thisUserId.toString());
            if (score == null) {
                //3.如果未点赞，可以点赞
                //3.1 数据库点赞数+1
                boolean isSuccess = update().setSql("liked = liked + 1").eq("id", articalId).update();
                //3.2 保存用户到Redis的set集合
                if (isSuccess) {
                    //已当前时间戳为分数，实现排名
                    stringRedisTemplate.opsForZSet().add(artical, thisUserId.toString(),System.currentTimeMillis());
                    stringRedisTemplate.expire(artical, 7, TimeUnit.SECONDS);
                }
            } else {
                //4.如果已点赞，取消点赞
                //4.1 数据库点赞数-1
                boolean isSuccess = update().setSql("liked = liked - 1").eq("id", articalId).update();
                //4.2 把用户从Redis的set集合移除
                if (isSuccess) {
                    stringRedisTemplate.opsForZSet().remove(artical, thisUserId.toString());
                }
            }
            return R.success();
        }else{
            log.info("抢锁失败");
            return R.fail("抢锁失败");
        }

        //


    }

    @Override
    @Transactional
    public R likeBlogNoRedisLock(Long articalId, Long userId) {
        Long thisUserId = userId;
        // 2.判断当前登录用户是否已经点赞
        String artical = "blog:like:" + articalId;

        Double score = stringRedisTemplate.opsForZSet().score(artical, thisUserId.toString());
        if (score == null) {
            //3.如果未点赞，可以点赞
            //3.1 数据库点赞数+1
            boolean isSuccess = update().setSql("liked = liked + 1").eq("id", articalId).update();
            //3.2 保存用户到Redis的set集合
            if (isSuccess) {
                //已当前时间戳为分数，实现排名
                log.info("开始插入Redis————————————————————————————————");
                stringRedisTemplate.opsForZSet().add(artical, thisUserId.toString(),System.currentTimeMillis());
                stringRedisTemplate.expire(artical, 7, TimeUnit.SECONDS);
            }
        } else {
            //4.如果已点赞，取消点赞
            //4.1 数据库点赞数-1
            boolean isSuccess = update().setSql("liked = liked - 1").eq("id", articalId).update();
            //4.2 把用户从Redis的set集合移除
            if (isSuccess) {
                stringRedisTemplate.opsForZSet().remove(artical, thisUserId.toString());
            }
        }
        return R.success();
    }

    @Override
    public R queryBlogLikes(Long id) {
        String key = "blog:like:" + id;

        // 1.查询top5的点赞用户 zrange key 0 4

        Set<String> top5 = null;
        try {
            top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        } catch (Exception e) {
            log.info(e.toString());


            //调用数据库查询
            //#######################
            //#########################
            //return R.success(users);
            return null;
        }
        if (top5 == null || top5.isEmpty()) {
            return R.success(Collections.emptyList());
        }
        // 2.解析出其中的用户id
//        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
//        String idStr = StrUtil.join(",", ids);
        List<String> ids = new ArrayList<>();
        for (String str : top5) {
            ids.add(str);
        }
        String idStr = String.join(",", ids);
        // 3.根据用户id查询用户 WHERE id IN ( id1 , id2 ) ORDER BY FIELD(id, id1, id2)
        List<Users> users = userService.query()
                .in("id", ids)
                .last("ORDER BY FIELD(id," + idStr + ")")
                .list();
        // 4.返回
        return R.success(users);
    }
}

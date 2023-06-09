package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.entity.R.R;
import org.example.service.BlogService;
import org.example.service.UserService;
import org.example.token.PassToken;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author xin麒
 * @date 2023/3/1 15:33
 */
@RestController
@Slf4j
@RequestMapping("/blog")
public class BlogController {
    public static final String REDIS_LIKE_LOCK = "REDIS_LIKE_LOCK";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private BlogService blogService;

    @Autowired
    private UserService userService;


    @Resource
    private Redisson redisson;


    @PassToken
    @PutMapping("/likeWithRedis/{id}/{userId}")
    public R likeBlogWithRedis(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
/*
         //修改点赞数量
        return blogService.likeBlog(id, userId);

*/

//        Redission加锁

        Long thisUserId = userId;
        // 2.判断当前登录用户是否已经点赞
        String artical = "blog:like:" + id;

        RLock likeLock = redisson.getLock(REDIS_LIKE_LOCK);

//
        likeLock.lock();
        try {
            blogService.likeBlogNoRedisLock(id, userId);
            log.info("加锁成功，执行后续代码。线程 ID：" + Thread.currentThread().getId());

            return R.success("修改成功");


        } catch (Exception e) {
            //TODO
            log.info(e.toString());
            return R.fail(e);
        }finally {
            likeLock.unlock();
            log.info("锁已释放");
        }


    }

    @PassToken
    @PutMapping("/likeWithNoRedis/{id}/{userId}")
    public R likeBlogWithNoRedis(@PathVariable("id") Long id, @PathVariable("userId") Long userId){

        return blogService.likeBlogNoRedisLock(id, userId);
    }






    @GetMapping("/likes/{id}")
    public R queryBlogLikes(@PathVariable("id") Long id){
        return blogService.queryBlogLikes(id);
    }
}

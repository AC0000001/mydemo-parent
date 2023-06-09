package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.entity.R.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import okhttp3.RequestBody;

import java.io.IOException;

/**
 * @Author: Shengke
 * @Date: 2023-06-07-10:55
 * @Description:    测试多线程访问共享资源--点赞数
 */
@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {



    @GetMapping("/2httpToGetLock/Redis")
    public R likeBlogWithRedis() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        String requestBody = "{\"name\": \"test\", \"age\": 20}"; // 请求体内容
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        //1000个用户并行点赞
        for(int i = 0; i < 3000; i ++){

            int j =i;
            new Thread(() -> {
                Request request = new Request.Builder()
                        .url("http://localhost:8081/blog/likeWithRedis/1/" + j)
                        .put(RequestBody.create(mediaType, requestBody))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return R.success("1000个用户并行点赞--RedisLock");
    }

    @GetMapping("/2httpToGetLock/NoRedis")
    public R likeBlogWithNoRedis() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();

        String requestBody = "{\"name\": \"test\", \"age\": 20}"; // 请求体内容
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        //1000个用户并行点赞
        for(int i = 0; i < 3000; i ++){

            int j =i;
            new Thread(() -> {
                Request request = new Request.Builder()
                        .url("http://localhost:8081/blog/likeWithNoRedis/1/" + j)
                        .put(RequestBody.create(mediaType, requestBody))
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        return R.success("1000个用户并行点赞--无RedisLock");
    }
}

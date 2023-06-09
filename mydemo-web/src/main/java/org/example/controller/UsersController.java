package org.example.controller;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.example.VO.LoginResultVO;
import org.example.entity.R.R;
import org.example.entity.User.Users;
import org.example.intcepter.token.TokenService;
import org.example.redis.enums.RedisPreEnum;
import org.example.service.UserService;
import org.example.token.PassToken;
import org.example.utils.IpUtils;
import org.example.utils.RedisUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;


/**
 * @Author: Shengke
 * @Date: 2023-06-07-18:58
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/users")
public class UsersController {

    @Resource
    private UserService usersService;
    @Resource
    TokenService tokenService;
    @Resource
    private RedisUtil redisUtil;
    //登录
    @PassToken
    @PostMapping("/login")
    public R login(@RequestBody @Validated Users user, HttpServletRequest request) {

        LoginResultVO loginResult = new LoginResultVO();
        //根据用户名查询用户信息
        Users userForBase = usersService.findByUsername(user);
        //ipV6
        String ipAddr = IpUtils.getIpAddr(request);

        if (userForBase == null) {
            return new R(200, "登录失败，用户不存在");

        } else {
            if (!userForBase.getPassword().equals(user.getPassword())) {
                return new R(200, "登录失败，密码错误");
            } else {
                String token = tokenService.getToken(userForBase);
                String key = RedisPreEnum.JWT_TOKEN_PRE.getPre() + userForBase.getId();
                Set<String> keys = redisUtil.keys(key + "*");
                if (CollectionUtils.isEmpty(keys)) {
                    redisUtil.set(key + ipAddr, userForBase, RedisPreEnum.JWT_TOKEN_PRE.getExpired());
                } else {
                    //清空之前的key
                    for (String k : keys) {
                        redisUtil.del(k);
                    }
                    //重新设置key
                    redisUtil.set(key + ipAddr, userForBase, RedisPreEnum.JWT_TOKEN_PRE.getExpired());

                    //用fastJson反序列化
                    String jsonString = redisUtil.get(key + ipAddr).toString();
                    Users parsedUser = JSON.parseObject(jsonString, Users.class);
                    boolean equals = parsedUser.equals(userForBase);
                    log.info(String.valueOf(equals),"反序列为Java对象成功！@！");

                }

                loginResult.setToken(token);
                loginResult.setUser(userForBase);
                return R.success(loginResult);
            }
        }
    }



    @PostMapping("/insertUser")
    public R insert(@RequestBody @Validated Users user, HttpServletRequest request) {



        Users user1 = usersService.saveUser(user);
        return R.success(user1);
    }


    @GetMapping("/getMessage")
    public String getMessage(){
        return "你已通过验证";
    }
}

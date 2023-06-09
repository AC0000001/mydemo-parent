package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.example.elasticRepository.UserRepository;
import org.example.entity.User.Users;
import org.example.mapper.UserMapper;
import org.example.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xin麒
 * @date 2023/3/2 11:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, Users> implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    UserRepository userRepository;

    @Override
    public Users findByUsername(Users user) {
        String userName = user.getName();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", userName);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Users saveUser(Users user) {
        return userRepository.save(user);
    }
}

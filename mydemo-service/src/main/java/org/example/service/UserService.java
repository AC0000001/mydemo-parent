package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.User.Users;


/**
 * @author xin麒
 * @date 2023/3/2 11:01
 */
public interface UserService extends IService<Users> {
    Users findByUsername(Users user);

    Users saveUser(Users user);
}

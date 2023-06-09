package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


import org.apache.ibatis.annotations.Mapper;
import org.example.entity.User.Users;

/**
 * @author xin麒
 * @date 2023/3/2 11:02
 */
@Mapper
public interface UserMapper extends BaseMapper<Users> {
}

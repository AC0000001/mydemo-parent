package org.example.redis.enums;

/**
 * @Author: Shengke
 * @Date: 2023-06-07-14:19
 * @Description:
 */

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * redis key前缀
 */
@AllArgsConstructor
@Getter
public enum RedisPreEnum {
    JWT_TOKEN_PRE("JWT_TOKEN_","token前缀",60*60*24);

    private String pre;
    private String desc;
    private Integer expired;
}

package org.example.intcepter.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import org.example.entity.User.Users;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @Author: Shengke
 * @Date: 2023-06-07-18:59
 * @Description:
 */
@Component
public class TokenService {

    private final static String SIGN = "";

    public String getToken(Users user) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,1);
        String token="";
        token= JWT.create().withAudience(String.valueOf(user.getId()))
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    public String verifyToken(String token){
        return null;
    }
}


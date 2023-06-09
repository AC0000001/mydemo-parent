package org.example.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.User.Users;

import java.io.Serializable;

/**
 * @Author: Shengke
 * @Date: 2023-06-07-22:49
 * @Description:
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResultVO implements Serializable {

    private static final long serialVersionUID = 5169608890496903041L;

    private String token;
    private Users user;

    //getters and setters
}
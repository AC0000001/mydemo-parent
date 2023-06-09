package org.example.entity.User;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @Author: Shengke
 * @Date: 2023-06-07-14:18
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_user")
@Document(indexName = "user")
@NoArgsConstructor(force = true)
public class Users implements Serializable {

    private static final long serialVersionUID = -271515877116090794L;

    @Id
    @TableId(type = IdType.ID_WORKER)
    private Long id;


    @NotNull("用户名不能为空")
    @Field(store = true, type = FieldType.Keyword)
    private String name;

    @NotNull("密码不能为空")
    @Field(store = true, type = FieldType.Keyword)
    private String password;
}


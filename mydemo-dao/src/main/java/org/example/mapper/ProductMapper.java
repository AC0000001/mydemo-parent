package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Product.Product;

/**
 * @Author: Shengke
 * @Date: 2023-06-09-10:45
 * @Description:
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}

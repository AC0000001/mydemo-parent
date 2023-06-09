package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Blog.Blog;

/**
 * @author xin麒
 * @date 2023/3/1 15:35
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

}

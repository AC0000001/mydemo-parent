package org.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.Blog.Blog;
import org.example.entity.R.R;

/**
 * @author xin麒
 * @date 2023/3/1 15:34
 */
public interface BlogService extends IService<Blog> {
    R likeBlog(Long id, Long userId);

    R likeBlogNoRedisLock(Long id, Long userId);
    R queryBlogLikes(Long id);
}

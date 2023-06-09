package org.example.elasticRepository;


import org.example.entity.User.Users;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Shengke
 * @Date: 2023-06-09-0:43
 * @Description:
 */

@Repository
public interface UserRepository extends ElasticsearchRepository<Users, String> {
}


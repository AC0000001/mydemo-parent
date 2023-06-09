package org.example.entity.Product;

/**
 * @Author: Shengke
 * @Date: 2023-06-09-9:47
 * @Description:
 */
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor(force = true)
@Document(indexName = "shopping", shards = 3, replicas = 1)
public class Product implements Serializable {
    private static final long serialVersionUID = -4382645998178020780L;
    //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ID_WORKER)
    private Long id;//商品唯一标识
    /**
     *type : 字段数据类型
     *analyzer : 分词器类型
     *index : 是否索引(默认:true)
     *Keyword : 短语,不进行分词
     */

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;//商品名称

    @Field(type = FieldType.Keyword)
    private String category;//分类名称

    @Field(type = FieldType.Double)
    private Double price;//商品价格

    //不被索引=不会被搜索
    @Field(type = FieldType.Keyword, index = false)
    private String images;//图片地址



}

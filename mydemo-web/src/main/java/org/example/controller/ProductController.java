package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.example.elasticRepository.ProductRepository;
import org.example.entity.Product.Product;
import org.example.entity.R.R;
import org.example.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Shengke
 * @Date: 2023-06-09-9:50
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    //注入 ElasticsearchRestTemplate
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductRepository productRepository;

    //创建索引并增加映射配置
    @GetMapping("/create")
    public void createIndex(){
        //创建索引，系统初始化会自动创建索引
        log.info("系统初始化会自动创建索引");
    }

    @GetMapping("/delete")
    public void deleteIndex(){
        //创建索引，系统初始化会自动创建索引
        boolean flg = elasticsearchRestTemplate.deleteIndex(Product.class);
        log.info("删除索引 = " + flg);
    }

    /**
     * 新增
     */

    @GetMapping("/insertProduct")
    public R save(@RequestBody(required = true)  Product product){


        Product savedProduct = productRepository.save(product);
        int insert = productMapper.insert(product);
        log.info("save: " + savedProduct);

        return R.success(savedProduct);
    }
    //修改


    /*
    * SpringDataESSearch
    *   文档搜索
    * */

    @GetMapping("/termQuery")
    public R termQuery(){
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "小米");
        Iterable<Product> products = productRepository.search(termQueryBuilder);
        for (Product product : products) {
            log.info(product.toString());
        }

        return R.success(products);
    }
    /**
     * term 查询加分页
     */
    @GetMapping("/termQueryByPage")
    public R termQueryByPage(){
        int currentPage= 0 ;
        int pageSize = 5;
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize);
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "小米");
        Iterable<Product> products = productRepository.search(termQueryBuilder,pageRequest);
        for (Product product : products) {
            log.info(product.toString());
        }
        return R.success(products);
    }



    public void update(){
        Product product = new Product();
        product.setId(1L);
        product.setTitle("小米 2 手机");
        product.setCategory("手机");
        product.setPrice(9999.0);
        product.setImages("http://www.atguigu/xm.jpg");
        Product save = productRepository.save(product);
        System.out.println("update: " + save);
    }
    //根据 id 查询

    public void findById(){
        Product product = productRepository.findById(1L).get();
        System.out.println(product);
    }

    //查询所有

    public void findAll(){
        Iterable<Product> products = productRepository.findAll();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    //删除

    public void delete(){
        Product product = new Product();
        product.setId(1L);
        productRepository.delete(product);
    }

    //批量新增

    public void saveAll(){
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setId(Long.valueOf(i));
            product.setTitle("["+i+"]小米手机");
            product.setCategory("手机");
            product.setPrice(1999.0+i);
            product.setImages("http://www.atguigu/xm.jpg");
            productList.add(product);
        }
        productRepository.saveAll(productList);
    }

    //分页查询

    public void findByPageable(){
        //设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        int currentPage=0;//当前页，第一页从 0 开始，1 表示第二页
        int pageSize = 5;//每页显示多少条
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
        //分页查询
        Page<Product> productPage = productRepository.findAll(pageRequest);
        for (Product Product : productPage.getContent()) {
            System.out.println(Product);
        }
    }

}

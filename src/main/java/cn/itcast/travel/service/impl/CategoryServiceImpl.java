package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao=new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {

        //获取jedis客户端
        Jedis jedis=JedisUtil.getJedis();
//        Set<String> categorySet=jedis.zrange("category",0,-1);
        Set<Tuple> tuples = jedis.zrangeWithScores("category", 0, -1);
        List<Category> categoryList=new ArrayList<>();


        //若redis缓存没有数据，则从数据库查询
        if(tuples==null||tuples.size()==0){
            System.out.println("从数据库查询...");
            categoryList.addAll(categoryDao.findAll());
            for (Category category : categoryList) {
                jedis.zadd("category",category.getCid(),category.getCname());
            }
        } else { //若redis缓存有数据，则直接返回
            System.out.println("从redis中查询...");
            for (Tuple tuple : tuples) {
                Category category=new Category();
                category.setCid((int) tuple.getScore());
                category.setCname(tuple.getElement());
                categoryList.add(category);
            }
        }
        return categoryList;
    }
}

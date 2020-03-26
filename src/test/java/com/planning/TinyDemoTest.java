package com.planning;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.planning.modules.app.dao.UserDao;
import com.planning.modules.app.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 小代码块 测试
 * @author planning
 * @since 2020-02-25 11:05
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class TinyDemoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void testUser(){
        Wrapper wrapper = new QueryWrapper<UserEntity>();
        List selectList = userDao.selectList(wrapper);
        System.out.println("TinyDemoTest testUser selectList" + JSON.toJSONString(selectList));

        UserEntity entity = new UserEntity();
        int randomKey = 5;
        // entity.setUserId(3L + randomKey);
        entity.setUsername("jack" + randomKey);
        entity.setPassword("456" + randomKey);
        entity.setMobile("1871618891" + randomKey);
        entity.setCreateTime(new Date());
        int resultId = userDao.insertUser(entity);
        System.out.println("TinyDemoTest testUser insert resultId is " + entity.getUserId());
    }

    @Test
    public void testFunc(){
        for (int i = 0; i < 10; i++) {
            System.out.println(Math.abs(new Random().nextLong()));
        }
    }

}
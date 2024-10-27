package com.tarequlrobin.journalapp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    public void sendEmail(){
        redisTemplate.opsForValue().set("email","test@gmail.com");
        Object o = redisTemplate.opsForValue().get("email");
        System.out.println(o);
    }
}

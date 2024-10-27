package com.tarequlrobin.journalapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void set(String key, Object object, Long timeToLive) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            redisTemplate.opsForValue().set(key, mapper.writeValueAsString(object), timeToLive, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(), clazz);
        }catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}

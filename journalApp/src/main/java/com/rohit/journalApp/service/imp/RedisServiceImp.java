package com.rohit.journalApp.service.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisServiceImp {
    @Autowired
    private RedisTemplate redisTemplate;

    public void set(String key, Object o, long ttl) {
        log.info("inside redis set method");
        log.info("get:Pune "+(String) redisTemplate.opsForValue().get("Pune"));
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, json, ttl, TimeUnit.SECONDS);
            log.info("get: "+(String) redisTemplate.opsForValue().get(key));
        } catch (Exception e) {
            log.error("an error ocured during serialization: "+e);
        }
    }

    public <T> T get(String key, Class<T> entity) {
        try {
            log.info("key: "+key);
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            if(o!=null) {
                return mapper.readValue(o.toString(), entity);
            }
            return null;
        } catch (JsonProcessingException e) {
            log.info("error during fething from redis");
            log.error("exception during get {}", e);
            return null;
        }
    }
}

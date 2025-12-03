package com.rohit.journalApp;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Disabled
    @Test
    public void testRedis(){
//        redisTemplate.opsForValue().set("email","rohitcsenitd@mial.com");
        redisTemplate.opsForValue().get("name");

        System.out.println(redisTemplate.opsForValue().get("name"));

    }
}

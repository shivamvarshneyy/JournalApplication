package com.example.practiceJournal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("name","ram");
        String name = redisTemplate.opsForValue().get("salary");
        System.out.println(name);
    }
}

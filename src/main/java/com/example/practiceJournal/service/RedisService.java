package com.example.practiceJournal.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) return null;
            return mapper.readValue(value, entityClass);
        } catch (Exception e) {
            log.error("Failed to get key {} from Redis", key, e);
            return null;
        }
    }

    public <T> void set(String key, T object, Duration ttl) {
        try {
            String value = mapper.writeValueAsString(object);
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception e) {
            log.error("Failed to set key {} in Redis", key, e);
        }
    }
}

package com.elco.system.platform.resources.utils;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.elco.system.platform.resources.entity.vo.StatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author kay
 * @date 2021/9/18
 */
@Component
public class RedisUtil {
    private RedisTemplate redisTemplate;
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate){
        RedisSerializer serializer=new FastJsonRedisSerializer(StatusVo.class);
        redisTemplate.setDefaultSerializer(serializer);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        this.redisTemplate=redisTemplate;
    }

}

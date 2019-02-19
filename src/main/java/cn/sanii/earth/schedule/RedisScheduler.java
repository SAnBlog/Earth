package cn.sanii.earth.schedule;

import cn.sanii.earth.model.Request;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Optional;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/19 17:31
 * @Description: Redis调度中心 支持分布式
 */
public class RedisScheduler implements Scheduler {

    private JedisPool pool;
    private String FIELD_NAME = "default_";

    public RedisScheduler(String fieldName, String host) {
        this(fieldName, new JedisPool(new JedisPoolConfig(), host));
        this.FIELD_NAME = fieldName;
    }

    public RedisScheduler(String fieldName, JedisPool pool) {
        this.FIELD_NAME = fieldName;
        this.pool = pool;
    }

    @Override
    public void push(Request request) {
        Jedis jedis = pool.getResource();
        try {
            jedis.rpush(request.getName(), request.getUrl());
        } finally {
            jedis.close();
        }
    }

    @Override
    public Request poll() {
        Jedis jedis = pool.getResource();
        try {
            Optional<String> url = Optional.ofNullable(jedis.lpop(this.FIELD_NAME));
            if (url.isPresent()) {
                return new Request(url.get());
            }
        } finally {
            jedis.close();
        }
        return null;
    }

}

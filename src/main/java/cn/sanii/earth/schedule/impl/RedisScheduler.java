package cn.sanii.earth.schedule.impl;

import cn.sanii.earth.model.Request;
import cn.sanii.earth.schedule.IScheduler;
import cn.sanii.earth.schedule.SchedulerName;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Optional;

/**
 * @Author: shouliang.wang
 * @Date: 2019/2/19 17:31
 * @Description: Redis调度队列
 */
public class RedisScheduler extends SchedulerName implements IScheduler {

    private JedisPool pool;
    private static final String PREFIX = "queue_";

    public RedisScheduler(String host) {
        this(new JedisPool(new JedisPoolConfig(), host));
    }

    public RedisScheduler(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public void push(Request request) {
        Jedis jedis = pool.getResource();
        try {
            jedis.rpush(PREFIX.concat(request.getName()), request.getUrl());
        } finally {
            jedis.close();
        }
    }

    @Override
    public Request poll() {
        Jedis jedis = pool.getResource();
        try {
            Optional<String> url = Optional.ofNullable(jedis.lpop(PREFIX.concat(this.fieldName)));
            if (url.isPresent()) {
                return new Request(url.get(), this.fieldName);
            }
        } finally {
            jedis.close();
        }
        return null;
    }

}

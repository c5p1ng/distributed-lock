package com.c5p1ng.distributed.lock.impl;

import com.c5p1ng.distributed.lock.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * 更安全的分布式锁
 */
@Slf4j
public class RedisDistributedRedLock implements DistributedLock {
    private RedissonClient redissonClient;

    /**
     * 分布式锁的键值
     */
    private String lockKey;

    private RLock redLock;

    /**
     * 锁的有效时间 5s
     */
    int expireTime = 5 * 1000;

    /**
     * 获取锁的超时时间
     */
    int acquireTimeout  = 500;

    public RedisDistributedRedLock(RedissonClient redissonClient, String lockKey) {
        this.redissonClient = redissonClient;
        this.lockKey = lockKey;
    }

    @Override
    public String acquire() {
        redLock = redissonClient.getLock(lockKey);
        boolean isLock;
        try {
            isLock = redLock.tryLock(acquireTimeout, expireTime, TimeUnit.MILLISECONDS);
            if(isLock) {
                log.info("{}-{}获得了锁", Thread.currentThread().getName(), lockKey);
                return lockKey;
            }
        } catch (Exception e) {
            log.error("try lock error.", e);
        }
        return null;
    }

    @Override
    public boolean release(String identify) {
        if(null != redLock) {
            redLock.unlock();
            return true;
        }
        return false;
    }
}

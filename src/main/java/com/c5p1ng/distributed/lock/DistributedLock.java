package com.c5p1ng.distributed.lock;

/**
 * 分布式锁接口
 */
public interface DistributedLock {
    /**
     * 获取锁
     * @author c5p1ng
     * @return 锁标识
     */
    String acquire();

    /**
     * 释放锁
     * @author c5p1ng
     * @param identify
     * @return
     */
    boolean release(String identify);
}

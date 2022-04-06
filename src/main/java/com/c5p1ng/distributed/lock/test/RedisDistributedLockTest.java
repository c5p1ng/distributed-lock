package com.c5p1ng.distributed.lock.test;

import com.c5p1ng.distributed.lock.impl.RedisDistributedLock;
import redis.clients.jedis.Jedis;

public class RedisDistributedLockTest {
    private static int n = 100;
    public static void doHandle() {
        System.out.println(--n);
    }

    public static void main(String[] args) {
        Runnable runnable = () -> {
            RedisDistributedLock lock = null;
            String unLockIdentify = null;
            try {
                Jedis conn = new Jedis("192.168.146.11",6379);
                lock = new RedisDistributedLock(conn, "c5p1ng.test1");
                unLockIdentify = lock.acquire();
                System.out.println(Thread.currentThread().getName() + "正在运行");
                doHandle();
            } finally {
                if (lock != null) {
                    lock.release(unLockIdentify);
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(runnable);
            t.start();
        }
    }
}

package com.bsoft.iot.bed.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zzx
 */
@Component
public class CuratorClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(CuratorClient.class);

    private CuratorFramework curatorFramework;

    public void init() {
        if (curatorFramework != null) {
            return;
        }

        LOGGER.info("zk 初始化");

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString("10.8.0.82:2181")
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("admin")
                .build();

        curatorFramework.start();
        LOGGER.info("连接状态 is {}", curatorFramework.isStarted());
        try {
            addChildWatch("/bgm");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createNode() {
        curatorFramework = curatorFramework.usingNamespace("admin");
        try {
            if (curatorFramework.checkExists().forPath("/bsoft/iot/bed/") == null) {
                curatorFramework.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/bsoft/iot/bed/");
                LOGGER.info("创建节点");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToken(String key, String token) {
        try {
            curatorFramework.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/bsoft/iot/bed/" + key, token.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addChildWatch(String nodePath) throws Exception {
        final PathChildrenCache childrenCache = new PathChildrenCache(curatorFramework, nodePath, true);
        childrenCache.start();
        childrenCache.getListenable().addListener((client, event) -> {
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                LOGGER.info("节点增加, path is {}, data is {}", event.getData().getPath(), event.getData().getData());
            }
            if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                LOGGER.info("节点删除, path is {}, data is {}", event.getData().getPath(), event.getData().getData());
            }
        });
    }
}
